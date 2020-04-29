package com.ixuea.courses.mymusic.adapter;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.ixuea.courses.mymusic.R;
import com.ixuea.courses.mymusic.domain.Sheet;
import com.ixuea.courses.mymusic.util.ImageUtil;

import androidx.annotation.NonNull;

/**
 * 歌单列表适配器
 */
public class SheetAdapter extends BaseQuickAdapter<Sheet, BaseViewHolder> {
    public SheetAdapter(int layoutResId) {
        super(layoutResId);
    }

    @Override
    protected void convert(@NonNull BaseViewHolder helper, Sheet data) {
        //显示封面
        ImageUtil.show(mContext, helper.getView(R.id.iv_banner), data.getBanner());

        //标题
        helper.setText(R.id.tv_title, data.getTitle());

        //显示音乐数
        helper.setText(R.id.tv_info, mContext.getString(R.string.song_count, data.getSongsCount()));
    }
}
