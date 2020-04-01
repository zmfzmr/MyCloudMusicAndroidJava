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

    /**
     * 获取当前播放时间对应 该行第几个字（播放 该行字的索引）
     * <p>
     * 00:38.914', '00:42.164', '那通失去希望', '290,373,348,403,689,1147'
     * <p>
     * 比如：播放进度progress为：38   累加时间为38.914 + 290 = 39.204（290： 换成秒s是： 0.290）
     * <p>
     * 38 < 39.204   所有就字  在数组里面的索引  return i；直接中断方法的运行
     * <p>
     * 因为这个方法只有一直播放，就会一直调用的的
     * <p>
     * 进度           累加时间                  对应索引
     * 38             39.204                    0
     * 39             39.204+0.373 = 39.577     1
     * 40             39.577 + 0.348 = 39.925   2
     * 41             39.925 + 0.403 =  40.328  3
     * 42             40.328 + 0.689 = 41.017   4
     * 43            41.017 + 1.147 = 42.164    5
     * <p>
     * <p>
     * 38 < 39.204 return i  这时i = 0
     * 39 < 39.204 return i  这时i = 1
     * 40 < 40.328 return i  这时i = 3  进度对应这个字的 索引为3
     * 41 < 41.017 return i  这时i = 4
     * 42 < 42.164 return i  这时i = 5
     */
    public static int getWordIndex(Line data, long progress) {
        long startTime = data.getStartTime();//这行的开始时间

        //循环所有字
        for (int i = 0; i < data.getWords().length; i++) {
            //累加时间
            startTime += data.getWordDurations()[i];

            if (progress < startTime) {//这个开始时间是 累加过了
                //如果进度小于累加的时间
                //就是这个索引
                return i;
            }
        }

        //默认返回 -1
        return -1;
    }

    /**
     * 获取当前字播放的时间
     * <p>
     * data.getWordDurations()[i] - (startTime - progress)
     * <p>
     * 00：27.487 ', '00:00:32.068','一时失志不免怨叹','347,373,1077,320,344,386,638,1096'
     * <p>
     * progress = 29   29000
     * <p>
     * startTime = 27  27000
     * <p>
     * 27000+347 + 373 + 1077 + 320 = 29117
     * <p>
     * 29000 < 29117    所以这个字的索引在 时间为320 这个的索引上
     * <p>
     * 320（这个播放的总时间）
     * <p>
     * 累加时间 - 进度
     * 320 - (29117 - 29000) = 203 这个字播放的时间
     * 203/320 = 0.6
     * 假设这个字宽度100 那么0.6*100 就是唱过的宽度
     * <p>
     * 举例
     * <p>
     * 累加时间
     * 1 2 3 4 5
     * <p>
     * 进度
     * 1 2 3 4
     * <p>
     * 累加时间 - 进度 = 就是这个右边还没有播放的那段时间
     * <p>
     * 然后320 - (累加时间 - 进度) = 播放的时间
     */
    public static float getWordPlayedTime(Line data, long progress) {
        // 这一行歌词开始的时间
        long startTime = data.getStartTime();
        //循环所有字
        for (int i = 0; i < data.getData().length(); i++) {
            startTime += data.getWordDurations()[i];
            //计算当前字已经播放的时间
            if (progress < startTime) {//这个开始时间是 累加过了
                return data.getWordDurations()[i] - (startTime - progress);
            }
        }
        //默认值
        return -1;
    }

    /**
     * 获取当前时间对应的歌词行
     */
    public static Line getLyricLine(Lyric data, long progress) {
        //获取当前时间的行
        int lineNumber = LyricUtil.getLineNumber(data, progress);//这个方法就是当前类的
        //Lyric 保存了行对象Line对象的集合
        //根据当前时间的行 返回这行的对象Line（获取当前时间歌词行）
        return data.getDatum().get(lineNumber);
    }
}
