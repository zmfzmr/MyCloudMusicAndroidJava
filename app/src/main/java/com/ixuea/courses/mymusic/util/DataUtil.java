package com.ixuea.courses.mymusic.util;

import com.ixuea.courses.mymusic.domain.Song;

import java.util.List;

/**
 * 数据处理工具类
 */
public class DataUtil {
    /**
     * 更改是否在播放列表字段
     */
    public static void changePlayListFlag(List<Song> datum, boolean value) {
        //遍历播放列表所有Song，更改其标志（是否在播放列表）
        for (Song data : datum) {
            //如果传入true（标志flag为：true），则datum（播放列表）里面的Song都在播放列表；
            // 否则不在播放列表
            data.setPlayList(value);
        }
    }
}
