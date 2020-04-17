package com.ixuea.courses.mymusic.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.ixuea.courses.mymusic.R;
import com.ixuea.courses.mymusic.domain.Comment;

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
        public CommentViewHolder(@NonNull View itemView) {
            super(itemView);

            //保存上下文

        }

        /**
         * 绑定数据
         */
        public void bindData(Comment data) {

        }
    }
}
