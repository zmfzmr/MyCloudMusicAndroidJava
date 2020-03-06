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

    /**
     * 获取上一个
     */
    Song previous();

    /**
     * 获取下一个
     */
    Song next();

    int changeLoopModel();//更改循环模式 return model，可能以后需要这个model循环模式，所以写上

    int getLoopModel();//获取循环模式

    Song getData();//获取当前播放的音乐

    void delete(int index);//删除音乐

    void deleteAll();//删除所有列表音乐
}
