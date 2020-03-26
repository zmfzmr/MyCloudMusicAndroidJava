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
     * 选中索引
     */
    private int selectedIndex;

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

        //出来选中状态
        if (selectedIndex == helper.getAdapterPosition()) {
            helper.setTextColor(R.id.tv, mContext.getResources().getColor(R.color.colorPrimary));
        } else {
            //未选中(未选中的，相当于白颜色的)
            helper.setTextColor(R.id.tv, mContext.getResources().getColor(R.color.lyric_text_color));
        }
    }

    /**
     * 设置选中索引
     *
     * @param selectedIndex
     */
    public void setSelectedIndex(int selectedIndex) {
        //先刷新原来的（就是把原来的颜色取消掉）（调用这方法会走上面的convert方法）
        notifyItemChanged(this.selectedIndex);
        this.selectedIndex = selectedIndex;
        notifyItemChanged(this.selectedIndex);//刷新当前的item
    }
}
