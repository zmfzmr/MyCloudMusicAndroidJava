package com.ixuea.courses.mymusic.listener;

import android.media.MediaPlayer;

import com.ixuea.courses.mymusic.domain.Song;

/**
 * 播放器接口（回调接口）
 */
public interface MusicPlayerListener {
    void onPaused(Song data);//已经暂停了

    void onPlaying(Song data);//已经播放了

    /**
     * 播放器准备完毕了
     *
     * @param mp   MediaPlayer
     * @param data Song
     */
    void onPrepared(MediaPlayer mp, Song data);

    /**
     * 播放进度回调
     *
     * @param data Song
     */
    void onProgress(Song data);

    /**
     * 播放完成后回调
     * <p>
     * 申明为默认方法，那么就可以不重写他。
     *
     * @param mp
     */
    default void onCompletion(MediaPlayer mp) {

    }

    /**
     * 歌词数据改变了
     * 定义default，并不是每个页面都需要通知歌词改变了
     */
    default void onLyricChanged(Song data) {

    }
}
