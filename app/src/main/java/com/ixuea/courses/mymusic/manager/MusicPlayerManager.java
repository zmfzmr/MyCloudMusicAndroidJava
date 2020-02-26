package com.ixuea.courses.mymusic.manager;

import com.ixuea.courses.mymusic.domain.Song;
import com.ixuea.courses.mymusic.listener.MusicPlayerListener;

/**
 * 音乐播放器对外暴露的接口
 */
public interface MusicPlayerManager {
    /**
     * @param uri  播放音乐的绝对地址
     * @param data 音乐对象（把音乐的总时长 播放进度，放到这个对象里面）
     */
    void play(String uri, Song data);//播放

    /**
     * 释放在播放
     */
    boolean isPlaying();

    /**
     * 暂停
     */
    void pause();

    void resume();//继续播放

    /**
     * 添加播放监听器
     *
     * @param listener MusicPlayerListener
     */
    void addMusicPlayerListener(MusicPlayerListener listener);

    /**
     * 移除播放监听器
     *
     * @param listener MusicPlayerListener
     */
    void removeMusicPlayerListener(MusicPlayerListener listener);
}
