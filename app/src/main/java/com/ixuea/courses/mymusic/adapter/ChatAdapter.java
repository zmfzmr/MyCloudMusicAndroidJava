package com.ixuea.courses.mymusic.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import cn.jpush.im.android.api.model.Message;

/**
 * 聊天界面适配器
 * <p>
 * 注意： Message 就是：cn.jpush 包下的
 */
public class ChatAdapter extends BaseRecyclerViewAdapter<Message, ChatAdapter.ViewHolder> {
    /**
     * 构造方法
     *
     * @param context
     */
    public ChatAdapter(Context context) {
        super(context);
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return null;
    }

    /**
     * 绑定数据
     *
     * @param holder
     * @param position
     */
    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        super.onBindViewHolder(holder, position);

        holder.bindData(getData(position));
    }

    /**
     * 聊天消息公共ViewHolder
     * 例如头像
     * <p>
     * 注意： Message 就是：cn.jpush 包下的
     */
    abstract class ViewHolder extends BaseRecyclerViewAdapter.ViewHolder<Message> {

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
        }

        /**
         * 绑定数据
         *
         * @param data D 是 类class ViewHolder<D> 中的D数据
         */
        @Override
        public void bindData(Message data) {
            super.bindData(data);
        }
    }
}
