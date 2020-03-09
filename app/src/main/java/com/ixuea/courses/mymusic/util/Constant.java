package com.ixuea.courses.mymusic.util;

import com.ixuea.courses.mymusic.BuildConfig;

/**
 * 常量类
 */
public class Constant {

    /**
     * 端点
     * 哪天不需要BuildConfig.ENDPOINT啦，直接更改Constant这个类就可以，这样就和BuildConfig没有关系
     * 替换更加容易
     */
    public static final String ENDPOINT = BuildConfig.ENDPOINT;

    /**
     * 资源端点
     */
    public static final String RESOURCE_ENDPOINT = BuildConfig.RESOURCE_ENDPOINT;

    //放在一起方便管理
    public static final String ID = "ID";

    /**
     * 用户详情昵称查询字段
     */
    public static final String NICKNAME = "nickname";
    /**
     * 传递data key
     */
    public static final String DATA = "DATA";
    public static final String TITLE = "TITLE";//传递标题
    public static final String URL = "URL";//传递url key

    /**
     * 歌单Id
     */
    public static final String SHEET_ID = "SHEET_ID";

    /**
     * 歌单
     */
    public static final String SHEET = "SHEET";

    /**
     * 音乐
     */
    public static final String SONG = "SONG";

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
     * 广告点击
     * 包名+命名
     * 防止重复
     */
    public static final String ACTION_AD = "com.ixuea.courses.mymusic.ACTION_AD";

    /**
     * 标题
     */
    public static final int TYPE_TITLE = 0;

    /**
     * 歌单
     */
    public static final int TYPE_SHEET = 1;

    /**
     * 单曲
     */
    public static final int TYPE_SONG = 2;

    /**
     * 播放进度通知
     */
    public static final int MESSAGE_PROGRESS = 0;

    /**
     * 16毫秒 音乐通知进度回调间隔
     */
    public static final long DEFAULT_TIME = 16;

    /**
     * 列表循环
     */
    public static final int MODEL_LOOP_LIST = 0;

    /**
     * 单曲循环
     */
    public static final int MODEL_LOOP_ONE = 1;

    /**
     * 随机循环
     */
    public static final int MODEL_LOOP_RANDOM = 2;

    /**
     * 音乐播放通知id（通知栏里面的）
     */
    public static final int NOTIFICATION_MUSIC_ID = 10000;

    /**
     * 音乐播放通知-播放
     */
    public static final String ACTION_PLAY = "com.ixuea.courses.mymusic.ACTION_PLAY";
    public static final String ACTION_PREVIOUS = "com.ixuea.courses.mymusic.ACTION_PREVIOUS";
    public static final String ACTION_NEXT = "com.ixuea.courses.mymusic.ACTION_NEXT";
    public static final String ACTION_LIKE = "com.ixuea.courses.mymusic.ACTION_LIKE";
    public static final String ACTION_LYRIC = "com.ixuea.courses.mymusic.ACTION_LYRIC";
    public static final String ACTION_UNLOCK_LYRIC = "com.ixuea.courses.mymusic.ACTION_UNLOCK_LYRIC";

    /**
     * 音乐播放通知-点击
     */
    public static final String ACTION_MUSIC_PLAY_CLICK = "com.ixuea.courses.mymusic.ACTION_MUSIC_PLAY_CLICK";

}
