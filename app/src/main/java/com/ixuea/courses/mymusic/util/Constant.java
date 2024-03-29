package com.ixuea.courses.mymusic.util;

import android.provider.MediaStore;

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
     * int值 id
     */
    public static String INT = "int";

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
     * 用户
     */
    public static final int TYPE_USER = 3;
    /**
     * 评论
     */
    public static final int TYPE_COMMENT = 4;

    /**
     * 视频
     */
    public static final int TYPE_VIDEO = 5;


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
     * 聊天消息通知点击
     */
    public static final String ACTION_MESSAGE = "com.ixuea.courses.mymusic.ACTION_MESSAGE";

    /**
     * 音乐播放通知-点击
     */
    public static final String ACTION_MUSIC_PLAY_CLICK = "com.ixuea.courses.mymusic.ACTION_MUSIC_PLAY_CLICK";

    /**
     * 保持播放进度间隔(毫秒)
     */
    public static final int SAVE_PROGRESS_TIME = 1000;

    /**
     * LRC歌词
     */
    public static final int LRC = 0;
    /**
     * KSC歌词
     */
    public static final int KSC = 10;
    //黑胶唱片指针旋转
    /**
     * 黑胶唱片指针暂停的角度
     * 记得加上个F
     */
    public static final float THUMB_ROTATION_PAUSE = -25F;
    /**
     * 黑胶唱片指针旋转的角度（播放时候的角度）
     */
    public static final float THUMB_ROTATION_PLAY = 0F;
    /**
     * 黑胶唱片指针动画时间 毫秒
     */
    public static final long THUMB_DURATION = 300;
    //end黑胶唱片指针旋转

    /**
     * 隐藏歌词拖拽时间 毫秒
     */
    public static final long LYRIC_HIDE_DRAG_TIME = 4000;
    /**
     * 请求获取overlay权限请求码（悬浮窗权限请求码）
     */
    public static final int REQUEST_OVERLAY_PERMISSION = 100;
    /**
     * 扫描二维码界面请求码
     */
    public static final int REQUEST_SCAN_CODE = 300;

    /**
     * 解锁全局歌词Id
     */
    public static final int NOTIFICATION_UNLOCK_LYRIC_ID = 10001;
    /**
     * @ 开头
     */
    public static final String MENTION = "@";
    /**
     * HASH_TAG 开头（以#开头）
     */
    public static final String HASH_TAG = "#";
    /**
     * 列表接口默认返回数量(默认10条数据) 评论列表下拉刷新默认10条
     */
    public static final int DEFAULT_PAGE_SIZE = 10;

    /**
     * 评论排序字段
     */
    public static final String ORDER = "order";
    /**
     * 评论排序 最热
     */
    public static final int ORDER_HOT = 10;
    /**
     * 分页参数字段
     */
    public static final String PAGE = "page";

    /**
     * 用户id（和服务器协商好的 比如user_id = 2   中的key）
     */
    public static final String USER_ID = "user_id ";

    /**
     * 查询条件 selection
     * Android媒体库本地音乐查询条件
     * 这里是 查询是音乐
     * 并且大于1M
     * 时长大于60秒的文件
     * <p>
     * MediaStore.Audio.AudioColumns.IS_MUSIC = "is_music"
     * <p>
     * 因为String.format 这种格式中，如果有问号？的话，可能会出问题，所以我们用拼接的方式
     * <p>
     * //这个查询条件是注意中间的空格
     */
    public static final String MEDIA_AUDIO_SELECTION =
            MediaStore.Audio.AudioColumns.IS_MUSIC + " != 0 AND " +
                    MediaStore.Audio.AudioColumns.SIZE + " >= ? AND " +
                    MediaStore.Audio.AudioColumns.DURATION + " >= ?";
    /**
     * 1M (1KB = 1*1024字节  1M = 1kB * 1024)
     */
    public static final int MUSIC_FILTER_SIZE = 1 * 1024 * 1024;

    /**
     * 60s (单位：毫秒)
     */
    public static final int MUSIC_FILTER_DURATION = 60 * 1000;

    /**
     * 扫描本地音乐放大镜圆周半径(可以理解为dp值或者px值，目前还不清楚)
     */
    public static final double DEFAULT_RADIUS = 30;

    /**
     * 阿里云OSS Bucket
     * <p>
     * //AK 和SK 权限是非常大的，可以访问阿里云的全局资源
     * （但是这里老师 做了限定，只能访问bucket(简单理解为阿里云上一个文件夹，并不能真正的理解为文件夹)）
     */
    public static final String ALIYUN_OSS_BUCKET_NAME = "dev-courses-misuc";

    /**
     * 阿里云OSS AK
     */
    public static final String ALIYUN_OSS_AK = "LTAI4Fr1njmWpE4E5uGrMtqk";

    /**
     * 阿里云OSS SK
     */
    public static final String ALIYUN_OSS_SK = "XLCBiAGLN1ad1DWUE3ExAux9lxmRue";

    /**
     * QQ第三方登录
     */
    public static final int PLATFORM_QQ = 20;//这个值是和服务端协商好，不能随便更改
    /**
     * 微博第三方登录
     */
    public static final int PLATFORM_WEIBO = 40;//这个值是和服务端协商好，不能随便更改
    /**
     * 二维码地址
     * 因为我们没有做网页所有随便写一个地址
     * 真实项目可能会使用主页，或者App下载界面
     * <p>
     * 我们这里的二维码的数据
     * 就是一个网址
     * 真实的数据在网址的查询参数里面
     * http://dev-my-cloud-music-api-rails.ixuea.com/v1/monitors/version?u=
     * <p>
     * ENDPOINT:  http://dev-my-cloud-music-api-rails.ixuea.com
     */
    public static final String QRCODE_URL = String.format("%s/v1/monitors/version?u=", ENDPOINT);
    /**
     * 搜索接口查询关键字key （query = 123 中的  key：query）
     * 这个key是和服务器协商好，不能随便更改
     */
    public static final String QUERY = "query";
    /**
     * 签名算法字符串
     * %%转义%
     * <p>
     * 这个是和服务器协商好的
     * <p>
     * wyZlmvYHoS^UU7#q  和 kNPd#3NRB%%84A!CF 这串都是盐
     *
     *    注意： %% 有一个是转义的，所以拷贝出去的话，需要删除一个
     * <p>
     * %s： 格式化的值
     */
    public static final String SIGN_FORMAT = "wyZlmvYHoS^UU7#q%skNPd#3NRB%%84A!CF";

    /**
     * AES128算法key
     */
    public static final String AES128_KEY = "wqfrwOSH*gN%I2v6";

    /**
     * AES128算法IV
     */
    public static final String AES128_IV = "VO*1sxQO5nDkcMyj";

    /**
     * 签名头名称
     */
    public static final String SIGN = "Sign";

    /**
     * 腾讯Bugly
     */
    public static final String BUGLY_APP_ID = "9299b31647";

    /**
     * 网络缓存目录大小
     * 100M： 1024字节    1024*1024 = 1M    1024*1024*100： 100M
     * <p>
     * 这个100M的时候 目前来说足够用了，因为网络请求一次也就几kb
     */
    public static final long NETWORK_CACHE_SIZE = 1024 * 1024 * 100;

    /**
     * 左侧(其他人) 文本消息
     */
    public static final int TYPE_TEXT_LEFT = 100;
    /**
     * 右侧(我的)  文本消息
     */
    public static final int TYPE_TEXT_RIGHT = 110;

    /**
     * 左侧(其他人) 图片消息
     */
    public static final int TYPE_IMAGE_LEFT = 120;
    /**
     * 右侧(我的)   图片消息
     */
    public static final int TYPE_IMAGE_RIGHT = 130;
}
