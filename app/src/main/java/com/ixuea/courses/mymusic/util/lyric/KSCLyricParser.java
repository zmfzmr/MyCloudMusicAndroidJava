package com.ixuea.courses.mymusic.util.lyric;

import com.ixuea.courses.mymusic.domain.lyric.Line;
import com.ixuea.courses.mymusic.domain.lyric.Lyric;
import com.ixuea.courses.mymusic.util.StringUtil;
import com.ixuea.courses.mymusic.util.TimeUtil;

import java.util.ArrayList;
import java.util.List;

/**
 * KSC歌词解析器
 */
public class KSCLyricParser {
    /**
     * 解析歌词
     */
    public static Lyric parse(String data) {

        Lyric result = new Lyric(); //创建结果对象

        result.setAccurate(true);//设置精确到歌词每个字

        List<Line> datum = new ArrayList<>();

        String[] strings = data.split("\n");//把数据分割放到数据里面

        for (String lineString : strings) {
            Line line = parseLine(lineString);//解析每行歌词，返回Line对象
            if (line != null) {
                datum.add(line);
            }
        }
        //将歌词列表设置到歌词对象
        result.setDatum(datum); //设置到Lyric中的List集合中（Lyric是集合的外层包裹）

        return result;// //返回解析后的歌词
    }

    /**
     * 解析每一行歌词
     * 例如：karaoke.add('00:27.487', '00:32.068', '一时失志不免怨叹', '347,373,1077,320,344,386,638,1096');
     */
    private static Line parseLine(String data) {
        if (data.startsWith("karaoke.add")) {
            //过滤了前面的元数据

            //创建结果对象
            Line result = new Line();
            //移除字符串前面的karaoke.add('
            //移除字符串后面的');
            //data=00:27.487', '00:32.068', '一时失志不免怨叹', '347,373,1077,320,344,386,638,1096
            data = data.substring(13, data.length() - 3);//length-1 :最后一位的索引 倒数第3位的索引是length-3，而这里截取的是不包括倒数第3位的

            //使用', '拆分字符串
            String[] commands = data.split("', '", -1);

            //Line添加 开始时间  结束时间 整行歌词
            //TimeUtil.parseToInt:这个方法，就是解析成（总毫秒）
            result.setStartTime(TimeUtil.parseToInt(commands[0]));
            result.setEndTime(TimeUtil.parseToInt(commands[1]));
            result.setData(commands[2]);

            //Line添加 字数组 持续时间数组
            //result.getData() ：其实就是commands[2] 整行歌词 只不过是设置到Line对象里面，然后获取出来
            result.setWords(StringUtil.words(result.getData()));

            //下面的这部分，跟StringUtil.words是类似的，因为只有这个地方用到，就不放到工具类中了
            List<Integer> wordDurations = new ArrayList<>();

            String lyricTimeString = commands[3];

            String[] lyricTimeWords = lyricTimeString.split(",");

            for (String string : lyricTimeWords) {
                //转为int时方便后面的计算
                wordDurations.add(Integer.valueOf(string));
            }
            //集合转换成数组添加到Line对象里面
            result.setWordDurations(wordDurations.toArray(new Integer[wordDurations.size()]));

            return result;
        }
        return null;
    }

}
