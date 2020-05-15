package com.ixuea.courses.mymusic.adapter;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.ixuea.courses.mymusic.domain.Video;

import androidx.annotation.NonNull;

/**
 * 视频适配器
 */
public class VideoAdapter extends BaseQuickAdapter<Video, BaseViewHolder> {

    public VideoAdapter(int layoutResId) {
        super(layoutResId);
    }

    @Override
    protected void convert(@NonNull BaseViewHolder helper, Video item) {

    }

}
