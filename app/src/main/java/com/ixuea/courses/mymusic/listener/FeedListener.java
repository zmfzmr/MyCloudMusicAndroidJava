package com.ixuea.courses.mymusic.listener;

import java.util.List;

import androidx.recyclerview.widget.RecyclerView;

/**
 * 动态监听器
 */
public interface FeedListener {
    /**
     * 点击了动态图片回调
     *
     * @param rv
     * @param imageUris
     * @param index
     */
    void onImageClick(RecyclerView rv, List<String> imageUris, int index);
}
