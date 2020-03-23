package com.ixuea.courses.mymusic.util;

/**
 * 日期时间工具类
 */
public class TimeUtil {
    /**
     * 将毫秒格式化为分:秒 例如： 150:11
     *
     * @param data
     * @return
     */
    public static String formatMinuteSecond(int data) {
        if (data == 0) {
            return "00:00";
        }

        //转为秒(总秒数)
        data /= 1000;//这里就是data = data / 1000;

        //计算分钟
        int minute = data / 60;

        //秒（剩余秒，比如1:10中10秒，总秒数是70秒，70 - （1*60） = 10秒  （70/60 = 1））
        int second = data - (minute * 60);
        // %02d: 先不看02 先%d:表示一个数字  2表示保留2位，不足2位的用0代替，所以前面加0
        //比如：01:10中的1是不足2位的，所以加个0，即01
        return String.format("%02d:%02d", minute, second);
    }

    /**
     * 将分秒毫秒数据转为毫秒
     *
     * @param data 格式为：00:06.429
     * @return
     */
    public static long parseToInt(String data) {
        //将:替换成.
        data = data.replace(":", ".");

        //使用.拆分
        String[] strings = data.split("\\.");
        //分别取出分 秒 毫秒
        int m = Integer.parseInt(strings[0]);//分
        int s = Integer.parseInt(strings[1]);//秒
        int ms = Integer.parseInt(strings[2]);//  毫秒  字符串转成int
        //转为毫秒
        return (m * 60 + s) * 1000 + ms;
    }
}
