package com.ixuea.courses.mymusic.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;

import com.ixuea.courses.mymusic.R;
import com.ixuea.courses.mymusic.domain.Video;

import androidx.annotation.NonNull;

import static com.ixuea.courses.mymusic.util.Constant.TYPE_COMMENT;
import static com.ixuea.courses.mymusic.util.Constant.TYPE_TITLE;
import static com.ixuea.courses.mymusic.util.Constant.TYPE_VIDEO;

/**
 * 视频详情适配器
 */
public class VideoDetailAdapter extends BaseRecyclerViewAdapter<Object, BaseRecyclerViewAdapter.ViewHolder> {
    /**
     * 构造方法
     *
     * @param context
     */
    public VideoDetailAdapter(Context context) {
        super(context);
    }

    /**
     * 返回ViewHolder
     *
     * @param parent
     * @param viewType
     * @return
     */
    @NonNull
    @Override
    public BaseRecyclerViewAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if (TYPE_TITLE == viewType) {
            //创建标题ViewHolder

            //可以复用评论里面的标题的ViewHolder(复用CommentAdapter.TitleViewHolder)
            return new CommentAdapter.TitleViewHolder(
                    getInflater().inflate(R.layout.item_title_small, parent, false));
        } else if (TYPE_VIDEO == viewType) {
            //创建视频ViewHolder
            return new VideoViewHolder(
                    getInflater().inflate(R.layout.item_video_list, parent, false));

        }
        //创建评论适配器ViewHolder
        return new CommentAdapter.CommentViewHolder(
                getInflater().inflate(R.layout.item_comment, parent, false),
                //我们没有值，所以直接传入null就行了
                null);
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

        //获取当前位置的数据
        Object data = getData(position);

        //绑定数据
        //这就是为什么我们要定义bindData
        holder.bindData(data);
    }

    /**
     * 返回view类型
     */
    @Override
    public int getItemViewType(int position) {
        //获取当前位置的数据
        Object data = getData(position);

        if (data instanceof String) {
            //标题
            return TYPE_TITLE;
        } else if (data instanceof Video) {
            //相关推荐视频
            return TYPE_VIDEO;
        }

        //默认返回评论类型
        return TYPE_COMMENT;
    }

    /**
     * 视频ViewHolder
     * <p>
     * private: 我们目前没有在其他地方用到
     */
    private class VideoViewHolder extends BaseRecyclerViewAdapter.ViewHolder {
        public VideoViewHolder(@NonNull View itemView) {
            super(itemView);
        }

        /**
         * 绑定数据
         *
         * @param data D 是 类class ViewHolder<D> 中的D数据
         */
        @Override
        public void bindData(Object data) {
            super.bindData(data);
        }
    }

}
