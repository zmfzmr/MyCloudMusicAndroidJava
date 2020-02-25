package com.ixuea.courses.mymusic.manager.impl;

import android.content.Context;

import com.ixuea.courses.mymusic.manager.MusicPlayerManager;

/**
 * 播放管理器默认实现
 */
public class MusicPlayerManagerImpl implements MusicPlayerManager {
    private static MusicPlayerManagerImpl instance;//实例对象
    private final Context context;//上下文

    /**
     * 构造方法
     *
     * @param context Context
     */
    private MusicPlayerManagerImpl(Context context) {
        this.context = context.getApplicationContext();
    }

    /**
     * 获取播放管理器
     * <p>
     * * getInstance：方法名可以随便取
     * * 只是在Java这边大部分项目都取这个名字
     * <p>
     * synchronized:这里用了同步
     * <p>
     * 音乐播放管理器（全局）只需要有一个，同时由于音乐播放用到了（硬件资源），所以也只能同时播放一个音乐，
     * 所以需要实现为单例；像游戏他可能同时播放多个音乐，其实是使用了其他API。
     * <p>
     * 实现单例设计模式
     * 单例的实现方法有很多中，这里实现的很简单，只添加了同步，是因为在手机上不能出现像服务端那样的高并发，所以不用写那么严谨：
     */
    public static synchronized MusicPlayerManagerImpl getInstance(Context context) {
        if (instance == null) {
            instance = new MusicPlayerManagerImpl(context);
        }
        return instance;
    }
}
