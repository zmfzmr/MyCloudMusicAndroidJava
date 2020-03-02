package com.ixuea.courses.mymusic.adapter;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.ixuea.courses.mymusic.domain.Song;

import androidx.annotation.NonNull;

/**
 * 注意：这里Song对应到的是item对象
 */
public class SimplePlayerAdapter extends BaseQuickAdapter<Song, BaseViewHolder> {
    /**
     * 构造方法
     */
    public SimplePlayerAdapter(int layoutResId) {
        super(layoutResId);
    }

    /**
     * 显示数据
     */
    @Override
    protected void convert(@NonNull BaseViewHolder helper, Song item) {
        //显示标题
        helper.setText(android.R.id.text1, item.getTitle());
    }
}
