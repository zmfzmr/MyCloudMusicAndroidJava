package com.ixuea.courses.mymusic.adapter;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.ixuea.courses.mymusic.R;
import com.ixuea.courses.mymusic.domain.Song;

import androidx.annotation.NonNull;

/**
 * 歌单详情-歌曲适配器
 */
public class SongAdapter extends BaseQuickAdapter<Song, BaseViewHolder> {
    /**
     * 选中索引
     */
    private int selectedIndex = -1;

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
     * @param data Song 因为继承泛型的时候已经传入了Song类型，所以这里不需要向下转型了
     */
    @Override
    protected void convert(@NonNull BaseViewHolder helper, Song data) {
        //显示位置 helper.getAdapterPosition():获取集合数据的索引（所以从0开始，所以需要+1）
        helper.setText(R.id.tv_position, String.valueOf(helper.getAdapterPosition()));
        //显示标题
        helper.setText(R.id.tv_title, data.getTitle());
        //显示信息(歌曲名称)
        helper.setText(R.id.tv_info, data.getSinger().getNickname());

        //处理选中状态
        if (selectedIndex == helper.getAdapterPosition()) {
            //选中行
            helper.setTextColor(R.id.tv_title, mContext.getResources().getColor(R.color.colorPrimary));
        } else {
            //未选中
            helper.setTextColor(R.id.tv_title, mContext.getResources().getColor(R.color.text));
        }

    }

    /**
     * 选中音乐
     *
     * @param index index
     */
    public void setSelectedIndex(int index) {
        selectIndex();

        //保存选中索引
        selectedIndex = index;

        selectIndex();
    }


    /**
     * 选中当前位置(刷新当前行)
     */
    private void selectIndex() {
        //刷新当前位置
        if (selectedIndex != -1) {
            notifyItemChanged(selectedIndex);
        }
    }
}
