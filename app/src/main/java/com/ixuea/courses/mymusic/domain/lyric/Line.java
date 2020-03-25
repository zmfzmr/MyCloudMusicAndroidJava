package com.ixuea.courses.mymusic.domain.lyric;

import com.ixuea.courses.mymusic.domain.BaseModel;

/**
 * 一行歌词
 *                开始时间       结束时间      整行歌词 和 字数组     每个字持续的时间数组
 * KSC:karaoke.add('00:19.662', '00:22.609', '有没有一扇窗',   '313,419,732,299,348,836');
 */
public class Line extends BaseModel {
    private String data;//整行歌词

    private long startTime;//开始时间 单位毫秒

    private long endTime;//结束时间 单位毫秒

    //注意：下面2个是个字符数组
    private String[] words;//每一个子（字数组）

    private Integer[] wordDurations;//每个字持续的时间数组

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

    public long getEndTime() {
        return endTime;
    }

    public void setEndTime(long endTime) {
        this.endTime = endTime;
    }

    public String[] getWords() {
        return words;
    }

    public void setWords(String[] words) {
        this.words = words;
    }

    public Integer[] getWordDurations() {
        return wordDurations;
    }

    public void setWordDurations(Integer[] wordDurations) {
        this.wordDurations = wordDurations;
    }
}
