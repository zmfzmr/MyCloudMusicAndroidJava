package com.ixuea.courses.mymusic.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;

import com.ixuea.courses.mymusic.listener.OnItemClickListener;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import butterknife.ButterKnife;

/**
 * 通用RecyclerViewAdapter
 * 主要实现了一些通用方法
 *
 * @param <D>
 * @param <VH> onCreateViewHolder: 需要在子类实现
 */
public abstract class BaseRecyclerViewAdapter<D, VH extends BaseRecyclerViewAdapter.ViewHolder> extends RecyclerView.Adapter<VH> {

    protected Context context;
    private final LayoutInflater inflater;//创建布局加载器
    private List<D> datum = new ArrayList<>();//数据列表

    private OnItemClickListener onItemClickListener;//item点击监听器

    /**
     * 构造方法
     *
     * @param context
     */
    public BaseRecyclerViewAdapter(Context context) {
        this.context = context;

        //创建布局加载器
        inflater = LayoutInflater.from(context);
    }

    @Override
    public void onBindViewHolder(@NonNull VH holder, int position) {
        //处理item点击监听器
        if (onItemClickListener != null) {
            //itemView 是个RecyclerView.ViewHolder里面的View itemView 成员变量：是个View对象
            //其实就是Adapter对应的那个布局
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //回调监听接口
                    onItemClickListener.onItemClick(holder, position);
                }
            });
        }
    }

    @Override
    public int getItemCount() {
        return datum.size();
    }

    /**
     * 获取当前位置数据
     *
     * @param position position
     */
    public D getData(int position) {
        return datum.get(position);
    }


    /**
     * 设置数据
     *
     * @param datum
     */
    public void setDatum(List<D> datum) {
        //清除原来的数据
        this.datum.clear();

        //添加数据
        this.datum.addAll(datum);

        //通知数据改变了
        //还有其他的通知方法
        //性能更好
        //详细的就不在深入讲解了
        //在《详解RecyclerView》课程中讲解了
        notifyDataSetChanged();
    }


    /**
     * 获取数据（获取集合List）
     *
     * @return
     */
    public List<D> getDatum() {
        return datum;
    }

    /**
     * 添加数据列表（和setDatum区别，setDatum会清除原来的数据，这个不会）
     *
     * @param datum
     */
    public void addDatum(List<D> datum) {
        //添加数据
        this.datum.addAll(datum);

        //刷新数据
        notifyDataSetChanged();
    }

    /**
     * 添加数据（添加一条数据（添加单个对象））
     *
     * @param data
     */
    public void addData(D data) {
        //添加数据
        this.datum.add(data);

        //刷新数据
        notifyDataSetChanged();
    }

    /**
     * 删除指定位置的数据
     * <p>
     * 注意：这里是notifyItemRemoved ，而不是notifyItemChanged
     *
     * @param position
     */
    public void removeData(int position) {
        datum.remove(position);
        notifyItemRemoved(position);
    }

    /**
     * 清除数据
     */
    public void clearData() {
        datum.clear();
        notifyDataSetChanged();
    }

    /**
     * 设置点击监听器
     */
    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }

    /**
     * 获取布局填充器
     */
    public LayoutInflater getInflater() {
        return inflater;
    }

    /**
     * 通用ViewHolder(注意：是抽象的，不能直接使用)
     * 主要是添加实现一些公共的逻辑
     *
     * 这里是abstract static 抽象静态类
     */
    public abstract static class ViewHolder<D> extends RecyclerView.ViewHolder {
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            //可以在这里加入ButterKnife这样的框架
            //也可以不加入

            //初始化ButterKnife
            //使用ButterKnife
            //1:绑定当前对象
            //2：绑定哪个view（这个view是之前inflater.inflate(R.layout.item_comment, parent, false)加载进来的）
            ButterKnife.bind(this, itemView);
        }

        /**
         * 绑定数据
         *
         * @param data D 是 类class ViewHolder<D> 中的D数据
         */
        public void bindData(D data) {

        }
    }
}
