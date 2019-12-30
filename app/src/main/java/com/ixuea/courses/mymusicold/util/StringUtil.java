package com.ixuea.courses.mymusicold.util;

import static com.ixuea.courses.mymusicold.util.Constant.REGEX_PHONE;

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
}
