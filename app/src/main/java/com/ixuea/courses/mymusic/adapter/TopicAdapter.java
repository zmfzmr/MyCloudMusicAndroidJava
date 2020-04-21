package com.ixuea.courses.mymusic.adapter;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.ixuea.courses.mymusic.domain.Topic;

import androidx.annotation.NonNull;

/**
 * 好友话题适配器
 */
public class TopicAdapter extends BaseQuickAdapter<Topic, BaseViewHolder> {
    /**
     * 构造方法
     *
     * @param layoutResId
     */
    public TopicAdapter(int layoutResId) {
        super(layoutResId);
    }

    @Override
    protected void convert(@NonNull BaseViewHolder helper, Topic item) {

    }
}
