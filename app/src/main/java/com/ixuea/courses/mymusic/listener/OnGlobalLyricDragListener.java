package com.ixuea.courses.mymusic.listener;

/**
 * 全局歌词拖拽接口
 */
public interface OnGlobalLyricDragListener {
    /**
     * //拖拽的方法
     *
     * @param y y轴方向上移动的距离
     */
    void onGlobalLyricDrag(int y);
}
