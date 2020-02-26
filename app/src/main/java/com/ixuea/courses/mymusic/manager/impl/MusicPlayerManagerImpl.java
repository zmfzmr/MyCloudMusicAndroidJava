package com.ixuea.courses.mymusic.manager.impl;

import android.content.Context;
import android.media.MediaPlayer;

import com.ixuea.courses.mymusic.domain.Song;
import com.ixuea.courses.mymusic.manager.MusicPlayerManager;

import java.io.IOException;

/**
 * 播放管理器默认实现
 */
public class MusicPlayerManagerImpl implements MusicPlayerManager {
    private static MusicPlayerManagerImpl instance;//实例对象
    private final Context context;//上下文
    private final MediaPlayer player;//播放器
    private Song data;//当前播放的音乐对象

    /**
     * 私有构造方法
     *
     * 这里外部就不能通过new方法来创建对象了
     * @param context Context
     */
    private MusicPlayerManagerImpl(Context context) {
        this.context = context.getApplicationContext();

        //初始化播放器
        player = new MediaPlayer();
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

    @Override
    public void play(String uri, Song data) {
        try {
            //保存音乐对象
            this.data = data;

            //是否播放器
            player.reset();

            //设置数据源
            player.setDataSource(uri);//可能找不到这个uri，会发生异常，所以捕获异常

            //同步准备
            //真实项目中可能会使用异步
            //因为如果网络不好
            //同步可能会卡主
            player.prepare();

            //开始播放
            player.start();
        } catch (IOException e) {
            e.printStackTrace();
            //TODO 错误了
        }

    }

    @Override
    public boolean isPlaying() {
        return player.isPlaying();
    }

    @Override
    public void pause() {
        if (isPlaying()) {
            //如果在播放就暂停
            player.pause();
        }
    }

    @Override
    public void resume() {
        if (!isPlaying()) {
            //如果没有在播放就播放
            player.start();
        }
    }
}
