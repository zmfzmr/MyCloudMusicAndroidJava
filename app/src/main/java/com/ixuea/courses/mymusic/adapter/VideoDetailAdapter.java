package com.ixuea.courses.mymusic.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.ixuea.courses.mymusic.R;
import com.ixuea.courses.mymusic.domain.Video;
import com.ixuea.courses.mymusic.util.ImageUtil;
import com.ixuea.courses.mymusic.util.TimeUtil;

import androidx.annotation.NonNull;
import butterknife.BindView;

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
    public class VideoViewHolder extends BaseRecyclerViewAdapter.ViewHolder {
        /**
         * 封面
         */
        @BindView(R.id.iv_banner)
        ImageView iv_banner;

        /**
         * 标题
         */
        @BindView(R.id.tv_title)
        TextView tv_title;

        /**
         * 信息
         */
        @BindView(R.id.tv_info)
        TextView tv_info;

        public VideoViewHolder(@NonNull View itemView) {
            super(itemView);
        }

        /**
         * 绑定数据
         *
         * @param d  D 是 类class ViewHolder<D> 中的D数据
         */
        @Override
        public void bindData(Object d) {
            super.bindData(d);

            Video data = (Video) d;

            //封面 (context父类的 外界通过VideoDetailAdapter构造方法传递进来的)
            ImageUtil.show(context, iv_banner, data.getBanner());

            //标题
            tv_title.setText(data.getTitle());
            //注意: 这里是data.getDuration() 是毫秒，调用的是formatMinuteSecond2
            String timeString = TimeUtil.s2ms((int) data.getDuration());
            String info = context.getResources().getString(R.string.video_info,
                    timeString,
                    data.getUser().getNickname());
            tv_info.setText(info);
        }
    }

}
