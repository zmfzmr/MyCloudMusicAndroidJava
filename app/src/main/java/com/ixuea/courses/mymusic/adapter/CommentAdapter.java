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
import butterknife.OnClick;

import static com.ixuea.courses.mymusic.util.Constant.TYPE_COMMENT;
import static com.ixuea.courses.mymusic.util.Constant.TYPE_TITLE;

/**
 * 我们要添加多种类型的数据，所以这类数据对象是Object
 */
//public class CommentAdapter extends BaseRecyclerViewAdapter<Comment, CommentAdapter.CommentViewHolder> {
//public class CommentAdapter extends BaseRecyclerViewAdapter<Object, CommentAdapter.CommentViewHolder> {
public class CommentAdapter extends BaseRecyclerViewAdapter<Object, BaseRecyclerViewAdapter.ViewHolder> {
    private static final String TAG = "CommentAdapter";
    /**
     * 评论适配器 监听器
     */
    private CommentAdapterListener commentAdapterListener;

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
    public BaseRecyclerViewAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        //inflate:  参数1.布局id 2：父容器（也就是根布局） 3：是否附加进去，这里是：不附加传入false
        //getInflater():父类里面的方法

        if (TYPE_TITLE == viewType) {
            //创建标题ViewHolder
            return new CommentAdapter.TitleViewHolder(getInflater().inflate(R.layout.item_title_small, parent, false));
        }
        //创建评论ViewHolder
        return new CommentViewHolder(getInflater().inflate(R.layout.item_comment, parent, false));
    }

    /**
     * 绑定数据
     *
     * @param holder
     * @param position
     */
    @Override
    public void onBindViewHolder(@NonNull BaseRecyclerViewAdapter.ViewHolder holder, int position) {
        super.onBindViewHolder(holder, position);

        //获取当前位置数据
//        Comment data = getData(position);
        Object data = getData(position);

        //绑定数据
        holder.bindData(data);
    }

    /**
     * 返回类型
     */
    @Override
    public int getItemViewType(int position) {
        //获取当前位置数据
        Object data = getData(position);

        if (data instanceof String) {
            //返回标题类型
            return TYPE_TITLE;
        }
        //评论类型
        return TYPE_COMMENT;
    }

    /**
     * 设置评论适配器 监听器
     *
     * @param commentAdapterListener CommentAdapterListener
     */
    public void setCommentAdapterListener(CommentAdapterListener commentAdapterListener) {
        this.commentAdapterListener = commentAdapterListener;
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

        @BindView(R.id.iv_like)
        ImageView iv_like;//点赞图标

        @BindView(R.id.tv_content)
        TextView tv_content;//评论内容

        @BindView(R.id.replay_container)
        View replay_container;//被恢复评论的容器

        @BindView(R.id.tv_reply_content)
        TextView tv_reply_content;//被恢复评论的内容
        private Comment data;//item评论对象

        /**
         * 头像点击了
         */
        @OnClick(R.id.iv_avatar)
        public void onAvatarClick() {
            if (commentAdapterListener != null) {
                commentAdapterListener.onAvatarClick(data);
            }
        }

        public CommentViewHolder(@NonNull View itemView) {
            super(itemView);
        }

        /**
         * 绑定数据
         */

//        public void bindData(Comment data) {
        public void bindData(Object d) {
            this.data = (Comment) d;//进行转换
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

                //data.getParent().getUser().getNickname():恢复评论的用户名
                //data.getParent().getContent():回复的内容
                //上面2个（用户名和回复内容）没有包含关系
                String content = context.getString(R.string.reply_comment,
                        data.getParent().getUser().getNickname(),
                        data.getParent().getContent());
                //设置内容
                tv_reply_content.setText(processContent(content));
            }

            //显示点赞状态(data:Comment对象)
            if (data.isLiked()) {
                //点赞了

                //设置图片
                iv_like.setImageResource(R.drawable.ic_comment_liked);
                //设置点赞数 颜色
                tv_like_count.setTextColor(context.getResources().getColor(R.color.colorPrimary));
            } else {
                //没有点赞

                //设置图标
                iv_like.setImageResource(R.drawable.ic_comment_like);
                //设置点赞数 颜色
                tv_like_count.setTextColor(context.getResources().getColor(R.color.light_grey));
            }

            //设置点赞容器事件
            ll_like_container.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (commentAdapterListener != null) {
                        commentAdapterListener.onLikeClick(data);
                    }
                }
            });
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

    /**
     * 标题ViewHolder
     */
    public class TitleViewHolder extends BaseRecyclerViewAdapter.ViewHolder {
        @BindView(R.id.tv_title)
        TextView tv_title;//标题

        public TitleViewHolder(View itemView) {
            super(itemView);
        }

        @Override
        public void bindData(Object data) {
            super.bindData(data);
            //设置标题
            tv_title.setText((String) data);
        }
    }
}
