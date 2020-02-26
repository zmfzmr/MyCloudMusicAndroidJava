package com.ixuea.courses.mymusic.listener;

import com.ixuea.courses.mymusic.domain.Song;

/**
 * 播放器接口（回调接口）
 */
public interface MusicPlayerListener {
    void onPause(Song data);//已经暂停了

    void onPlaying(Song data);//已经播放了
}
