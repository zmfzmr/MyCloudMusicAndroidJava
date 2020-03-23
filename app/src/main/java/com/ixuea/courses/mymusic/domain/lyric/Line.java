package com.ixuea.courses.mymusic.domain.lyric;

import com.ixuea.courses.mymusic.domain.BaseModel;

/**
 * 一行歌词
 */
public class Line extends BaseModel {
    private String data;//整行歌词

    private long startTime;//开始时间 单位毫秒

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }

    public long getStartTime() {
        return startTime;
    }

    public void setStartTime(long startTime) {
        this.startTime = startTime;
    }
}
