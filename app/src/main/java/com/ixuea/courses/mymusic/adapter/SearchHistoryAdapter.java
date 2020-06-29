package com.ixuea.courses.mymusic.adapter;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.ixuea.courses.mymusic.R;
import com.ixuea.courses.mymusic.domain.SearchHistory;

import androidx.annotation.NonNull;

/**
 * 搜索历史适配器
 */
public class SearchHistoryAdapter extends BaseQuickAdapter<SearchHistory, BaseViewHolder> {

    public SearchHistoryAdapter(int layoutResId) {
        super(layoutResId);
    }

    @Override
    protected void convert(@NonNull BaseViewHolder helper, SearchHistory data) {
        //标题
        helper.setText(R.id.tv_title, data.getContent());
    }
}
