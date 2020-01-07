package com.ixuea.courses.mymusicold.util;

import com.ixuea.courses.mymusicold.BuildConfig;

/**
 * 常量类
 */
public class Constant {

    /**
     * 资源端点
     * 哪天不需要BuildConfig.ENDPOINT啦，直接更改Constant这个类就可以，这样就和BuildConfig没有关系
     * 替换更加容易
     */
    public static final String ENDPOINT = BuildConfig.ENDPOINT;

    public static final String ID = "ID";

    /**
     * 手机号正则表达式
     * 移动：134 135 136 137 138 139 147 150 151 152 157 158 159 178 182 183 184 187 188 198
     * 联通：130 131 132 145 155 156 166 171 175 176 185 186
     * 电信：133 149 153 173 177 180 181 189 199
     * 虚拟运营商: 170
     * <p>
     * ^：匹配一行的开头
     * $:匹配一行的结尾
     * <p>
     * \\d{8}$：匹配后面8位数字
     */
    public static final String REGEX_PHONE = "^(13[0-9]|14[579]|15[0-3,5-9]|16[6]|17[0135678]|18[0-9]|19[89])\\d{8}$";

    /**
     * 邮箱正则表达式
     * ^([a-z0-9_\.-]+)：^匹配一行的开头     [a-z0-9_\.-]： a到z或者0-9或者 _ . -中的任意字符；后面加个+表示后面的字符重复一次或多次
     * {n,m}	重复n到m次 比如：{2,6} 重复2次到6次
     */
    public static final String REGEX_EMAIL = "^([a-z0-9_\\.-]+)@([\\da-z\\.-]+)\\.([a-z\\.]{2,6})$";

    /**
     * 用户详情昵称查询字段
     */
    public static final String NICKNAME = "nickname";
}
