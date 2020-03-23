package com.ixuea.courses.mymusic.util.lyric;

import com.ixuea.courses.mymusic.domain.lyric.Line;
import com.ixuea.courses.mymusic.domain.lyric.Lyric;
import com.ixuea.courses.mymusic.util.TimeUtil;

import java.util.ArrayList;
import java.util.List;

/**
 * LRC类型解析器
 */
class LRCLyricParser {
    /**
     * 解析歌词
     *
     * 思路:  Lyric   line集合datum（解析到的line add到里面） 然后Lyric（里面设置datum）
     */
    public static Lyric parse(String data) {
        //创建歌词解析后的对象
        Lyric result = new Lyric();

        //初始化一个列表
        List<Line> datum = new ArrayList<>();

        //使用\n拆分歌词
        String[] strings = data.split("\n");

        for (String lineString : strings) {
            //解析每一行歌词
            Line line = parseLine(lineString);

            if (line != null) {
                //过滤了元数据歌词
                datum.add(line);
            }
        }
        //将歌词行列表(line集合)设置到歌词对象
        result.setDatum(datum);

        return result;//返回解析后的歌词
    }

    /**
     * 解析一行歌词
     * 例如：[00:00.300]爱的代价 - 李宗盛
     *
     * @param data
     * @return
     */
    private static Line parseLine(String data) {

        //过滤元数据
        //由于这里用不到所以过滤了
        if (data.startsWith("[0")) {
            //创建歌词行
            Line line = new Line();

            data = data.substring(1);//从索引1截取到最后

            String[] commands = data.split("]", -1);//-1表示不限制拆分限制（这个限制，可以限制拆分几个，-1表示不限制）

            //Line里面设置开始时间和行数据
            //开始时间
            line.setStartTime(TimeUtil.parseToInt(commands[0]));
            //歌词
            line.setData(commands[1]);

            //返回解析后的这行歌词
            return line;

        }
        return null;
    }
}
