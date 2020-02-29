package com.ixuea.courses.mymusic.manager;

import com.ixuea.courses.mymusic.domain.Song;

import java.util.List;

/**
 * 列表管理器
 * 主要是封装了列表相关的操作
 * 例如：上一曲，下一曲，循环模式
 */
public interface ListManager {
    /**
     * 设置播放列表
     *
     * @param datum datum
     */
    void setDatum(List<Song> datum);

    List<Song> getDatum();//获取播放列表

    /**
     * 播放
     */
    void play(Song data);

    void pause();//暂停

    void resume();//继续播放
}
