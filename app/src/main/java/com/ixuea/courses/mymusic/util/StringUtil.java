package com.ixuea.courses.mymusic.util;

import java.util.ArrayList;
import java.util.List;

import static com.ixuea.courses.mymusic.util.Constant.REGEX_EMAIL;
import static com.ixuea.courses.mymusic.util.Constant.REGEX_PHONE;

/**
 * 字符串工具类
 * 注意：这个和StringUtils的区别，StringUtils是导入依赖commons-lang3 包中的类
 */
public class StringUtil {


    /**
     * 是否是手机号
     *
     * @param value Value
     * @return 返回是否符合手机号  默认返回false
     */
    public static boolean isPhone(String value) {
        //看传入的值和正则表达式，是否匹配的上，如果可以的话，就返回true，否则false
        return value.matches(REGEX_PHONE);
    }

    /**
     * 是否是邮箱
     *
     * @param value Value
     * @return 是否匹配邮箱，匹配返回true，否则返回false
     */
    public static boolean isEmail(String value) {
        return value.matches(REGEX_EMAIL);
    }

    /**
     * 是否符合密码格式
     *
     * @param value 传入的值
     * @return 是否匹配密码格式，这个可以不用正则表达式
     */
    public static boolean isPassword(String value) {
        return value.length() >= 6 && value.length() <= 15;
    }

    /**
     * 是否符合昵称格式
     *
     * @return true 符合昵称格式
     */
    public static boolean isNickName(String value) {
        return value.length() >= 2 && value.length() <= 10;
    }

    /**
     * 是否符合验证码格式
     */
    public static boolean isCode(String value) {
        return value.length() == 4;
    }

    /**
     * 将一行字符串 拆分为单个字
     * <p>
     * 只实现了中文
     * 英文是有问题
     * 大家感兴趣可以在继续实现
     * <p>
     * 拆分字符串的时候，不知道有多少个，整个数组变动起来非常麻烦，不高效
     * 数组传递的时候更高效，因为不可变更，所以创建一个列表集合，等拆分完成后把整个列表变成一个 数组
     * 简单：列表list变动容易，数组传递的时候高效
     */
    public static String[] words(String data) {
        //创建一个列表
        List<String> results = new ArrayList<>();

        //转为char数组（把这一行歌词 转为每个字符 放到字符数组里面）
        char[] chars = data.toCharArray();
        //遍历每一个字符串
        for (char c : chars) {
            //转为字符串
            //并添加到列表
            results.add(String.valueOf(c));
        }
        //转为数组
        return results.toArray(new String[results.size()]);
    }
}
