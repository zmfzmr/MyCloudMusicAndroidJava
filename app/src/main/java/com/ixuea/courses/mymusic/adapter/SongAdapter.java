package com.ixuea.courses.mymusic.adapter;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.ixuea.courses.mymusic.domain.Song;

import androidx.annotation.NonNull;

/**
 * 歌单详情-歌曲适配器
 */
public class SongAdapter extends BaseQuickAdapter<Song, BaseViewHolder> {
    /**
     * 构造方法
     *
     * @param layoutResId 布局Id
     */
    public SongAdapter(int layoutResId) {
        super(layoutResId);
    }

    /**
     * 显示数据
     *
     * @param helper BaseViewHolder
     * @param item   Song
     */
    @Override
    protected void convert(@NonNull BaseViewHolder helper, Song item) {

    }
}
