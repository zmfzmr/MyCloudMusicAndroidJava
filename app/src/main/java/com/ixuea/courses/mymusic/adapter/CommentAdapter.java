package com.ixuea.courses.mymusic.adapter;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.ixuea.courses.mymusic.R;
import com.ixuea.courses.mymusic.domain.Comment;
import com.ixuea.courses.mymusic.util.ImageUtil;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class CommentAdapter extends RecyclerView.Adapter<CommentAdapter.CommentViewHolder> {
    private Context context;
    private List<Comment> datum = new ArrayList<>();//数据列表
    private final LayoutInflater inflater;//创建布局加载器

    /**
     * 构造方法
     *
     * @param context
     */
    public CommentAdapter(Context context) {
        this.context = context;

        //创建布局加载器
        inflater = LayoutInflater.from(context);
    }

    @NonNull
    @Override
    public CommentViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        //inflate:  参数1.布局id 2：父容器（也就是根布局） 3：是否附加进去，这里是：不附加传入false
        return new CommentViewHolder(inflater.inflate(R.layout.item_comment, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull CommentViewHolder holder, int position) {
        //获取当前位置数据
        Comment data = getData(position);

        //绑定数据
        holder.bindData(data);
    }

    @Override
    public int getItemCount() {
        return datum.size();
    }

    /**
     * 获取当前位置数据
     */
    private Comment getData(int position) {
        return datum.get(position);
    }

    /**
     * 设置数据
     */
    public void setDatum(List<Comment> datum) {
        this.datum.clear();//先清除原来的数据

        //添加数据
        this.datum.addAll(datum);

        notifyDataSetChanged();
    }

    /**
     * 评论ViewHolder
     */
    public class CommentViewHolder extends RecyclerView.ViewHolder {
        //item布局中用到的是CircleImageView 继承ImageView ；我们这里没有用到CircleImageView里面的方法，
        // 所以没有什么影响
        private final ImageView iv_avatar;//头像
        private final TextView tv_nickname;//昵称
        private final TextView tv_time;//时间
        private final View ll_like_container;//点赞容器
        private final TextView tv_like_count;//点赞数
        private final TextView tv_content;//评论内容

        public CommentViewHolder(@NonNull View itemView) {
            super(itemView);

            //头像
            iv_avatar = itemView.findViewById(R.id.iv_avatar);

            //昵称
            tv_nickname = itemView.findViewById(R.id.tv_nickname);

            //时间
            tv_time = itemView.findViewById(R.id.tv_time);

            //点赞容器
            ll_like_container = itemView.findViewById(R.id.ll_like_container);

            //点赞数
            tv_like_count = itemView.findViewById(R.id.tv_like_count);

            //评论
            tv_content = itemView.findViewById(R.id.tv_content);
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

            //时间(这里后面实现)
//            tv_time.setText(TimeUtil.);

            //点赞数 (这类Comment类中 是long类型，转换成String类型)
            tv_like_count.setText(String.valueOf(data.getLikes_count()));

            //评论
            tv_content.setText(data.getContent());

        }
    }
}
