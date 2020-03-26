package com.ixuea.courses.mymusic.domain.lyric;

import java.util.List;

public class LyricUtil {
    /**
     * 计算当前播放时间是那一行歌词
     * 00:50.281', '00:55.585', '人生可比是海上的波浪', '628,1081,376,326,406,371,375,1045,378,318'
     *
     * @param lyric    歌词对象
     * @param position 播放时间
     */
    public static int getLineNumber(Lyric lyric, long position) {
        List<Line> datum = lyric.getDatum();//先获取歌词列表
        //倒序遍历每一行歌词
        Line line;
        for (int i = datum.size() - 1; i >= 0; i--) {//这里用的是倒序
            line = datum.get(i);
            //如果当前时间正好大于等于改行开始时间
            //就是该行
            if (position >= line.getStartTime()) {
                return i;//返回，直接中断方法的运行(i是第几行歌词的 索引)
            }
        }
        return 0;//默认第0行
    }
}
