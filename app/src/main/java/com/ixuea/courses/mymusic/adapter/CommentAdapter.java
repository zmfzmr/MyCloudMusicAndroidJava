package com.ixuea.courses.mymusic.adapter;

import android.app.Activity;
import android.content.Context;
import android.text.SpannableString;
import android.text.method.LinkMovementMethod;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.ixuea.courses.mymusic.R;
import com.ixuea.courses.mymusic.activity.TopicDetailActivity;
import com.ixuea.courses.mymusic.activity.UserDetailActivity;
import com.ixuea.courses.mymusic.domain.Comment;
import com.ixuea.courses.mymusic.listener.OnTagClickListener;
import com.ixuea.courses.mymusic.util.ImageUtil;
import com.ixuea.courses.mymusic.util.LogUtil;
import com.ixuea.courses.mymusic.util.MatchResult;
import com.ixuea.courses.mymusic.util.StringUtil;
import com.ixuea.courses.mymusic.util.TimeUtil;

import androidx.annotation.NonNull;
import butterknife.BindView;

public class CommentAdapter extends BaseRecyclerViewAdapter<Comment, CommentAdapter.CommentViewHolder> {
    private static final String TAG = "CommentAdapter";

    /**
     * 构造方法
     *
     * @param context
     */
    public CommentAdapter(Context context) {
        super(context);
    }

    @NonNull
    @Override
    public CommentViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        //inflate:  参数1.布局id 2：父容器（也就是根布局） 3：是否附加进去，这里是：不附加传入false
        //getInflater():父类里面的方法
        return new CommentViewHolder(getInflater().inflate(R.layout.item_comment, parent, false));
    }

    /**
     * 绑定数据
     *
     * @param holder
     * @param position
     */
    @Override
    public void onBindViewHolder(@NonNull CommentViewHolder holder, int position) {
        super.onBindViewHolder(holder, position);

        //获取当前位置数据
        Comment data = getData(position);

        //绑定数据
        holder.bindData(data);
    }

    /**
     * 评论ViewHolder
     */
    public class CommentViewHolder extends BaseRecyclerViewAdapter.ViewHolder {
        //item布局中用到的是CircleImageView 继承ImageView ；我们这里没有用到CircleImageView里面的方法，
        // 所以没有什么影响
        @BindView(R.id.iv_avatar)
        ImageView iv_avatar;//头像

        @BindView(R.id.tv_nickname)
        TextView tv_nickname;//昵称

        @BindView(R.id.tv_time)
        TextView tv_time;//时间

        @BindView(R.id.ll_like_container)
        View ll_like_container;//点赞容器

        @BindView(R.id.tv_like_count)
        TextView tv_like_count;//点赞数
        @BindView(R.id.tv_content)
        TextView tv_content;//评论内容

        @BindView(R.id.replay_container)
        View replay_container;//被恢复评论的容器

        @BindView(R.id.tv_reply_content)
        TextView tv_reply_content;//被恢复评论的内容

        public CommentViewHolder(@NonNull View itemView) {
            super(itemView);
        }

        /**
         * 绑定数据
         */

        public void bindData(Comment data) {
            //显示头像(里面需要传入Activity，向下转型)
            //因为是CircleImageView 圆形头像, 所以用showAvatar
            ImageUtil.showAvatar((Activity) context, iv_avatar, data.getUser().getAvatar());

            //昵称
            tv_nickname.setText(data.getUser().getNickname());

            //时间
            tv_time.setText(TimeUtil.commentFormat(data.getCreated_at()));

            //点赞数 (这类Comment类中 是long类型，转换成String类型)
            tv_like_count.setText(String.valueOf(data.getLikes_count()));

            //评论
//            tv_content.setText(data.getContent());
            //StringUtil.processHighlight 传入评论内容，方法找到的是里面是有高亮的文本
//            tv_content.setText(StringUtil.processHighlight(context, data.getContent()));

            //这2个方法（setMovementMethod和setLinkTextColor重构到一个方法里面，方便重用）
//            //设置后才可以点击（MovementMethod：翻译：移动方法 ），这里传入的是：LinkMovementMethod：链接移动方法
//            tv_content.setMovementMethod(LinkMovementMethod.getInstance());
            //这里返回SpannableString（富文本：里面包含 @12和#123#和其他没有高亮的文本）
            //必须同时设置setMovementMethod(LinkMovementMethod.getInstance());才能实现点击
            tv_content.setText(processContent(data.getContent()));

//            //因为这里设置了点击，所以不能用上面的那种方法来设置高亮颜色，使用下面的这种
//            tv_content.setLinkTextColor(context.getResources().getColor(R.color.text_highlight));

            //被恢复的评论
            //data.getParent(): 获取的是Comment（评论对象）里面的 Comment对象（回复的对象）
            if (data.getParent() == null) {
                //没有被恢复的评论
                replay_container.setVisibility(View.GONE);
            } else {
                //有回复的评论
                replay_container.setVisibility(View.VISIBLE);

                //设置后才可以的点击
                setCommon(tv_reply_content);

                //内容（\@%1$s：%2$s）这个是拼接后 以@爱学啊 等昵称开头
                //  \@: 需要用斜杆转义  因为这里字符串放到xml中，所以一个斜杠就可以了
                // %1$s:第一个是字符串 %2$s：第二个是字符串
                String content = context.getString(R.string.reply_comment,
                        data.getParent().getUser().getNickname(),
                        data.getParent().getContent());
                //设置内容
                tv_reply_content.setText(processContent(content));
            }
        }
    }

    /**
     * 设置文本通用配置
     *
     * @param tv TextView
     */
    private void setCommon(TextView tv) {
        //设置后才可以点击（MovementMethod：翻译：移动方法 ），这里传入的是：LinkMovementMethod：链接移动方法
        tv.setMovementMethod(LinkMovementMethod.getInstance());

        //因为这里设置了点击，所以不能用上面的那种方法来设置高亮颜色，使用下面的这种
        tv.setLinkTextColor(context.getResources().getColor(R.color.text_highlight));
    }

    /**
     * * 处理文本点击事件
     * * 这部分可以用监听器回调到Activity中处理
     * <p>
     * 这个方法放在CommentAdapter外面
     *
     * @param content
     */
    private SpannableString processContent(String content) {
        SpannableString result = StringUtil.processContent(context, content, new OnTagClickListener() {
            @Override
            public void onTagClick(String data, MatchResult matchResult) {
                String clickText = StringUtil.removePlaceHolderString(data);
                LogUtil.d(TAG, "processContent mention click:" + clickText);

                //跳转到用户详情
                //参数1：上下文 2：点击的文本（没有@）
                UserDetailActivity.startWithNickname(context, clickText);
            }
        }, (data, matchResult) -> {
            String clickText = StringUtil.removePlaceHolderString(data);
            LogUtil.d(TAG, "processContent hash tag click:" + clickText);

            //跳转到话题详情
            TopicDetailActivity.startWithTitle(context, clickText);
        });
        //SpannableString结果(富文本：里面包含 @12和#123#和其他没有高亮的文本)
        return result;
    }

}
