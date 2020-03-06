package com.ixuea.courses.mymusic.adapter;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.ixuea.courses.mymusic.R;
import com.ixuea.courses.mymusic.domain.Song;
import com.ixuea.courses.mymusic.manager.ListManager;

import androidx.annotation.NonNull;

/**
 * 迷你控制器 播放列表adapter
 */
public class PlayListAdapter extends BaseQuickAdapter<Song, BaseViewHolder> {
    private final ListManager listManager;

    //播放列表管理器
    /**
     * 构造方法
     *
     * @param layoutResId 布局Id
     * @param listManager ListManager
     */
    public PlayListAdapter(int layoutResId, ListManager listManager) {
        super(layoutResId);
        this.listManager = listManager;
    }

    /**
     * 显示数据
     */
    @Override
    protected void convert(@NonNull BaseViewHolder helper, Song data) {
        //显示标题(连接用format)
        String title = String.format("%s - %s", data.getTitle(), data.getSinger().getNickname());
        helper.setText(R.id.tv_title, title);

        //处理选中状态
        //因为点击播放列表之前，播放迷你控制器已经显示（并且已经播放音乐了），这个时候listManager.getData()！=null
        if (data.getId().equals(listManager.getData().getId())) {
            //选中

            //颜色设置为主色调
            helper.setTextColor(R.id.tv_title, mContext.getResources().getColor(R.color.colorPrimary));

        } else {
            //未选中

            //颜色设置为黑色
            helper.setTextColor(R.id.tv_title, mContext.getResources().getColor(R.color.text));
        }

        //删除按钮点击事件(这里可以添加多个参数)
        helper.addOnClickListener(R.id.iv_remove);
    }

}
