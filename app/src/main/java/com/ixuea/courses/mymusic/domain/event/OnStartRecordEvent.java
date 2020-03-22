package com.ixuea.courses.mymusic.domain.event;

import com.ixuea.courses.mymusic.domain.Song;

/**
 * 黑胶唱片开始滚动事件
 */
public class OnStartRecordEvent {
    private Song data;//当前音乐

    public OnStartRecordEvent(Song data) {
        this.data = data;
    }

    public Song getData() {
        return data;
    }

    public void setData(Song data) {
        this.data = data;
    }
}
