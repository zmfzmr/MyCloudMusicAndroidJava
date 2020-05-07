package com.ixuea.courses.mymusic.adapter;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.ixuea.courses.mymusic.R;
import com.ixuea.courses.mymusic.domain.Song;

import androidx.annotation.NonNull;

/**
 * 下载完成界面适配器
 */
public class DownloadedAdapter extends BaseQuickAdapter<Song, BaseViewHolder> {
    /**
     * 构造方法
     *
     * @param layoutResId
     */
    public DownloadedAdapter(int layoutResId) {
        super(layoutResId);
    }

    /**
     * 显示数据
     */
    @Override
    protected void convert(@NonNull BaseViewHolder helper, Song data) {

        //位置
        helper.setText(R.id.tv_position, String.valueOf(helper.getAdapterPosition() + 1));

        //标题
        helper.setText(R.id.tv_title, data.getTitle());

        //信息(这里是获取 歌手名称)
        helper.setText(R.id.tv_info, data.getSinger().getNickname());

    }
}
