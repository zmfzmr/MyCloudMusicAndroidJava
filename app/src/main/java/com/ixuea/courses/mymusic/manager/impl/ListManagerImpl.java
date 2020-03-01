package com.ixuea.courses.mymusic.manager.impl;

import android.content.Context;

import com.ixuea.courses.mymusic.domain.Song;
import com.ixuea.courses.mymusic.manager.ListManager;
import com.ixuea.courses.mymusic.manager.MusicPlayerManager;
import com.ixuea.courses.mymusic.service.MusicPlayerService;
import com.ixuea.courses.mymusic.util.LogUtil;
import com.ixuea.courses.mymusic.util.ResourceUtil;

import java.util.LinkedList;
import java.util.List;

/**
 * 列表管理器默认实现
 */
public class ListManagerImpl implements ListManager {
    private static final String TAG = "ListManagerImpl";
    private static ListManagerImpl instance;//实例对象
    private final Context context;//上下文
    /**
     * 列表
     * LinkedList:增删高效
     * ArrayList：遍历高效
     */
    private List<Song> datum = new LinkedList<>();
    //其实列表管理器中主要用到了播放管理器（就是让播放管理利器实现）
    private final MusicPlayerManager musicPlayerManager;//音乐播放管理器
    private Song data;//当前音乐对象

    /**
     * 构造刚刚
     *
     * @param context Context
     */
    private ListManagerImpl(Context context) {
        this.context = context.getApplicationContext();
        //初始化音乐播放管理器
        musicPlayerManager = MusicPlayerService.getMusicPlayerManager(context);
    }

    /**
     * 获取单例对象(获取列表管理器)
     *
     * @param context Context
     * @return ListManagerImpl
     */
    public static synchronized ListManagerImpl getInstance(Context context) {
        if (instance == null) {
            //只有没有初始化才创建对象
            instance = new ListManagerImpl(context);
        }
        return instance;
    }

    @Override
    public void setDatum(List<Song> datum) {
        LogUtil.d(TAG, "setDatum");

        //清空原来的数据
        this.datum.clear();
        //添加新的数据
        this.datum.addAll(datum);
    }

    @Override
    public List<Song> getDatum() {
        LogUtil.d(TAG, "setDatum");
        return datum;
    }

    @Override
    public void play(Song data) {
        LogUtil.d(TAG, "play");
        //保存数据
        this.data = data;
        //播放音乐
        //这里是从SheetDetailActivity中，通过play(position)传递过来的Song
        musicPlayerManager.play(ResourceUtil.resourceUri(data.getUri()), data);
    }

    @Override
    public void pause() {
        LogUtil.d(TAG, "pause");
    }

    @Override
    public void resume() {
        LogUtil.d(TAG, "resume");
    }
}
