package com.ixuea.courses.mymusic.util;

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
}
