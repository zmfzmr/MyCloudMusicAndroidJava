package com.ixuea.courses.mymusic.domain.event;

import com.ixuea.courses.mymusic.domain.Song;

/**
 * 收藏歌曲到歌单按钮点击事件
 */
public class CollectSongClickEvent {

    private Song data;//音乐

    /**
     * 构造方法
     */
    public CollectSongClickEvent(Song data) {
        this.data = data;
    }

    public Song getData() {
        return data;
    }

    public void setData(Song data) {
        this.data = data;
    }
}
