package com.ixuea.courses.mymusic.adapter;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.ixuea.courses.mymusic.R;
import com.ixuea.courses.mymusic.domain.lyric.Line;

import androidx.annotation.NonNull;

/**
 * 播放界面-歌词列表适配器
 */
public class LyricAdapter extends BaseQuickAdapter<Line, BaseViewHolder> {

    /**
     * 构造方法
     *
     * @param layoutResId
     */
    public LyricAdapter(int layoutResId) {
        super(layoutResId);
    }

    /**
     * 绑定数据
     */
    @Override
    protected void convert(@NonNull BaseViewHolder helper, Line data) {
        //使用TextView实现 data.getData() ：获取到的是整行歌词
        //显示歌词
        helper.setText(R.id.tv, data.getData());
    }
}
