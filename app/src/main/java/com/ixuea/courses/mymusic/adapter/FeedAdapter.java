package com.ixuea.courses.mymusic.adapter;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.ixuea.courses.mymusic.domain.Feed;

import androidx.annotation.NonNull;

/**
 * 动态适配器
 */
public class FeedAdapter extends BaseQuickAdapter<Feed, BaseViewHolder> {
    /**
     * 构造方法
     */
    public FeedAdapter(int layoutResId) {
        super(layoutResId);
    }

    /**
     * 显示数据
     */
    @Override
    protected void convert(@NonNull BaseViewHolder helper, Feed item) {

    }
}
