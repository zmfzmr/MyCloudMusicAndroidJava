package com.ixuea.courses.mymusic.manager.impl;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.media.MediaPlayer;

import com.ixuea.courses.mymusic.domain.Song;
import com.ixuea.courses.mymusic.listener.MusicPlayerListener;
import com.ixuea.courses.mymusic.manager.MusicPlayerManager;
import com.ixuea.courses.mymusic.service.MusicPlayerService;
import com.ixuea.courses.mymusic.util.Constant;
import com.ixuea.courses.mymusic.util.LogUtil;
import com.ixuea.courses.mymusic.util.NotificationUtil;

/**
 * 在第一版课程中，我们是将通知写到播放列表中的，虽然可以运行，但其他项目却不是很好复用，
 * 因为一个应用可能需要播放列表，但不一定需要音乐通知，现在将音乐通知写到单独的类中，
 * 好处是，将逻辑分散开了便于维护和重用；
 * <p>
 * 坏处是，会增加代码量；
 * 但我们这里还是放到单独的类中。
 */
public class MusicNotificationManager implements MusicPlayerListener {

    private static final String TAG = "MusicNotificationManager";
    private static MusicNotificationManager instance;//实例
    private final Context context;//上下文
    private final MusicPlayerManager musicPlayerManager;

    /**
     * 构造方法
     *
     * @param context Context
     */
    public MusicNotificationManager(Context context) {
        this.context = context.getApplicationContext();

        //获取播放管理器 注意:这里是用本对象的context
        musicPlayerManager = MusicPlayerService.getMusicPlayerManager(this.context);

        //添加播放管理器监听器
        musicPlayerManager.addMusicPlayerListener(this);
        //初始化音乐通知广播接收器
        initMusicNotificationReceiver();

    }

    /**
     * 初始化音乐通知广播接收器
     */
    private void initMusicNotificationReceiver() {
        //创建一个广播接受者
        BroadcastReceiver musicNotificationBroadcastReceiver = new BroadcastReceiver() {
            /**
             * 发生了广播（这个方法里面接收发送的广播）
             */
            @Override
            public void onReceive(Context context, Intent intent) {
                //获取action
                String action = intent.getAction();

                if (Constant.ACTION_LIKE.equals(action)) {
                    //点赞
                    LogUtil.d(TAG, "like");
                } else if (Constant.ACTION_PREVIOUS.equals(action)) {
                    //上一曲
                    LogUtil.d(TAG, "previous");
                } else if (Constant.ACTION_PLAY.equals(action)) {
                    //播放
                    LogUtil.d(TAG, "play");
                } else if (Constant.ACTION_NEXT.equals(action)) {
                    //下一曲
                    LogUtil.d(TAG, "next");
                } else if (Constant.ACTION_LYRIC.equals(action)) {
                    //歌词
                    LogUtil.d(TAG, "lyric");
                }
            }
        };

        //创建过滤器
        //目的是接收这些事件(意思说：通过过滤器 接收 到底发送了哪个广播)
        IntentFilter intentFilter = new IntentFilter();

        //添加一些动作
        intentFilter.addAction(Constant.ACTION_LIKE);
        intentFilter.addAction(Constant.ACTION_PREVIOUS);
        intentFilter.addAction(Constant.ACTION_PLAY);
        intentFilter.addAction(Constant.ACTION_NEXT);
        intentFilter.addAction(Constant.ACTION_LYRIC);

        //注册广播接受者
        context.registerReceiver(musicNotificationBroadcastReceiver, intentFilter);
    }

    /**
     * 获取通知管理器实例
     */
    public static MusicNotificationManager getInstance(Context context) {
        if (instance == null) {
            instance = new MusicNotificationManager(context);
        }
        return instance;
    }

    @Override
    public void onPaused(Song data) {
        //显示通知 参数3：是否播放
        NotificationUtil.showMusicNotification(context, data, false);
    }

    @Override
    public void onPlaying(Song data) {
        //显示通知 参数3：是否播放
        NotificationUtil.showMusicNotification(context, data, true);
    }

    @Override
    public void onPrepared(MediaPlayer mp, Song data) {
        //显示通知
        //已经准备完成后，就是播放，所以传入true
//        NotificationUtil.showMusicNotification(context,data,true);
    }

    @Override
    public void onProgress(Song data) {

    }
}