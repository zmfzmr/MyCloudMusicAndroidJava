package com.ixuea.courses.mymusic.domain.event;

import com.ixuea.courses.mymusic.domain.Song;

/**
 * 黑胶唱片停止通知
 */
public class OnStopRecordEvent {
    private Song data;//音乐

    public OnStopRecordEvent(Song data) {
        this.data = data;
    }

    public Song getData() {
        return data;
    }

    public void setData(Song data) {
        this.data = data;
    }
}
