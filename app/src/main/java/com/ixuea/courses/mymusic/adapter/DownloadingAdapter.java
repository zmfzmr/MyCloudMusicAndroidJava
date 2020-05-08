package com.ixuea.courses.mymusic.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.ixuea.android.downloader.domain.DownloadInfo;
import com.ixuea.courses.mymusic.R;
import com.ixuea.courses.mymusic.domain.Song;
import com.ixuea.courses.mymusic.util.ORMUtil;

import androidx.annotation.NonNull;
import butterknife.BindView;

/**
 * 下载中适配器
 * 注意：这类的对象数据是：DownloadInfo
 */
public class DownloadingAdapter extends BaseRecyclerViewAdapter<DownloadInfo, DownloadingAdapter.ViewHolder> {


    private final ORMUtil orm;//数据库工具类

    /**
     * 构造方法
     *
     * @param context
     */
    public DownloadingAdapter(Context context, ORMUtil orm) {
        super(context);
        this.orm = orm;
    }

    /**
     * 返回ViewHolder
     */
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        //getInflater(): 父类的
        return new ViewHolder(getInflater().inflate(R.layout.item_downloading, parent, false));
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

        //获取当前位置数据
        DownloadInfo data = getData(position);

        //获取业务数据(也就是保存到Realm数据库里面的数据)
        //DownloadInfo:保存了下载对象（Song对象）的id，用realm通过这个id查找song
        Song song = orm.querySongById(data.getId());
        holder.bindBaseData(song);

        //显示下载信息(这里传入的是 ：DownloadInfo )
        holder.bindData(data);
    }

    /**
     * 下载ViewHolder
     */
    public class ViewHolder extends BaseRecyclerViewAdapter.ViewHolder {
        @BindView(R.id.tv_title)
        TextView tv_title;//标题

        /**
         * 构造方法
         */
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
        }

        /**
         * 显示基础数据
         *
         * @param song Song
         */
        public void bindBaseData(Song song) {
            //标题
            tv_title.setText(song.getTitle());
        }

        /**
         * 显示下载信息
         */
        @Override
        public void bindData(Object data) {
            super.bindData(data);
        }
    }
}
