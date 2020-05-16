package com.ixuea.courses.mymusic.util;

import org.joda.time.DateTime;

import java.util.Date;

/**
 * 日期时间工具类
 */
public class TimeUtil {
    /**
     * 这里是单位毫秒
     * 1分种毫秒数 = 60秒 * 1000毫秒  后面的L是long型
     */
    private static final long ONE_MINUTE = 60000L;
    private static final long ONE_HOUR = 3600000L;//1天毫秒数, 60分*60秒*1000毫秒
    private static final long ONE_DAY = 86400000L;//1星期毫秒数
    //这个没用到就注释掉
//    private static final long ONE_WEEK = 604800000L;//一周毫秒数

    /**
     * 将(毫秒)格式化为分:秒 例如： 150:11
     * @param data 毫秒
     */
    public static String formatMinuteSecond(int data) {
        if (data == 0) {
            return "00:00";
        }
        //转为秒(总秒数)
        data /= 1000;//这里就是data = data / 1000;

//        //计算分钟
//        int minute = data / 60;
//
//        //秒（剩余秒，比如1:10中10秒，总秒数是70秒，70 - （1*60） = 10秒  （70/60 = 1））
//        int second = data - (minute * 60);
//        // %02d: 先不看02 先%d:表示一个数字  2表示保留2位，不足2位的用0代替，所以前面加0
//        //比如：01:10中的1是不足2位的，所以加个0，即01
//        return String.format("%02d:%02d", minute, second);

        //重构，删除的那部分重新调用下面的方法
        return formatMinuteSecond2(data);
    }

    /**
     * 将(秒)格式化为分:秒 例如： 150:11
     *
     * @param data 秒
     */
    public static String formatMinuteSecond2(int data) {
        if (data == 0) {
            return "00:00";
        }

//        //转为秒(总秒数) (因为这类的是秒，所以就不需要转换成秒了)
//        data /= 1000;//这里就是data = data / 1000;

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

    /**
     * 将ISO8601字符串转为项目中通用的格式(就是解析ISO8601字符串、return 几秒前等和 默认其他时间格式化)
     * 几秒钟前
     * 几天前
     * <p>
     * 在企业级/商业项目中，经常会用到时间传递，已经格式化，有的用时间戳，有些时间戳是秒，有些又是毫秒，
     * 但时间戳不好处理时区。
     * <p>
     * 所以我们项目中所有的日期时间格式，都是ISO8601这种表示方法，例如：如下时间：
     * <p>
     * 年   月  日 时 分  秒 毫秒 时区
     * 2200-04-10T00:28:49.000Z
     * <p>
     * 但这样的日期时间格式，如果自己解析不太好解析，所以我们可以使用joda-time框架，他是为Java日期和时间类
     * 提供了一个高质量的替代品；他可以很方便的处理ISO8601格式，有比Java 自带api更强大的功能，易用的api;
     * 同时相同的功能，该框架能明显缩减代码。
     *
     * @param date
     * @return
     */
    public static String commentFormat(String date) {
        //将字符串置为DataTime DateTime:是添加的依赖joda.time 里面的
        //ISO8601这种表示方法: 例如： 2200-04-10T00:28:49.000Z
        //将 2200-04-10T00:28:49.000Z这种格式的字符串 用DateTime（joda.time依赖库）对象来解析
        DateTime dateTime = new DateTime(date);//传入进行一个解析

        //计算和现在时间的差
        //单位毫秒
        //Date() 为java util里面的         这个DateTime对象需要转换成Date ，然后才能获取时间
        //就是现在的 减去  原来的时间 = 时间差
        long value = new Date().getTime() - dateTime.toDate().getTime();

        if (value < 1L * ONE_MINUTE) {
            //小于1分钟

            //显示多少秒前(把value转换成多少秒)
            long data = toSeconds(value);
            //data：表示秒   秒小于0 那就是1秒前，否则就是显示data
            return String.format("%d秒前", data <= 0 ? 1 : data);
        } else if (value < 60L * ONE_MINUTE) {
            //小于1小时(60 * 1分钟（ONE_MINUTE的值是用毫秒表示的）)

            //显示多少分钟前
            long data = toMinutes(value);
            return String.format("%d分钟前", data);
        } else if (value < 24 * ONE_HOUR) {
            //小于1天(24 * 1小时（ONE_HOUR：用毫秒表示）)

            //显示多少小时前
            long data = toHours(value);
            return String.format("%d小时前", data);

        } else if (value < 30 * ONE_DAY) {
            //小于30天(1月)

            //显示多少天前
            long data = toDays(value);
            return String.format("%d天前", data);
        }

        //其他时间
        //格式化为yyyyMMddHHmm(y:年 M：月 d：天 H：小时 m：分钟  M和H为大写)
        return yyyyMMddHHmm(dateTime);
    }

    /**
     * 将DateTime(ISO8601字符串格式)转为yyyy-MM-dd HH:mm
     *
     * @param dateTime DateTime:是添加的依赖joda.time 里面的
     * @return
     */
    private static String yyyyMMddHHmm(DateTime dateTime) {
        return dateTime.toString("yyyy-MM-dd-HH:mm");//转成2020-04-18-15:32这种形式
    }

    //辅助方法

    /**
     * 转为秒
     *
     * @param date
     * @return
     */
    private static long toSeconds(long date) {
        return date / 1000L;
    }

    /**
     * 转为分钟
     *
     * 传入的date是:毫秒,先转换成秒， 然后秒 / 60 那么就是分钟
     * 简单就是: 毫秒->秒->分钟
     * @param date
     * @return
     */
    private static long toMinutes(long date) {
        return toSeconds(date) / 60L;
    }

    /**
     * 转为小时
     * 毫秒->分钟->小时
     * @param date
     * @return
     */
    private static long toHours(long date) {
        return toMinutes(date) / 60L;
    }

    /**
     * 转为天
     * 毫秒->小时->天
     * @param date
     * @return
     */
    private static long toDays(long date) {
        return toHours(date) / 24L;
    }

    /**
     * 转为月
     *
     * @param date
     * @return
     */
    private static long toMonths(long date) {
        return toDays(date) / 30L;
    }

    //end 辅助方法
}
