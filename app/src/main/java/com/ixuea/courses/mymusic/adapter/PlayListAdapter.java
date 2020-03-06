package com.ixuea.courses.mymusic.adapter;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.ixuea.courses.mymusic.R;
import com.ixuea.courses.mymusic.domain.Song;

import androidx.annotation.NonNull;

/**
 * 迷你控制器 播放列表adapter
 */
public class PlayListAdapter extends BaseQuickAdapter<Song, BaseViewHolder> {

    /**
     * 构造方法
     *
     * @param layoutResId 布局Id
     */
    public PlayListAdapter(int layoutResId) {
        super(layoutResId);
    }

    /**
     * 显示数据
     */
    @Override
    protected void convert(@NonNull BaseViewHolder helper, Song data) {
        //显示标题(连接用format)
        String title = String.format("%s - %s", data.getTitle(), data.getSinger().getNickname());
        helper.setText(R.id.tv_title, title);
    }

}
