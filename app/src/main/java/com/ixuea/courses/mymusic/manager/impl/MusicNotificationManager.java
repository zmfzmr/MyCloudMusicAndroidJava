package com.ixuea.courses.mymusic.manager.impl;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.media.MediaPlayer;

import com.ixuea.courses.mymusic.domain.Song;
import com.ixuea.courses.mymusic.listener.MusicPlayerListener;
import com.ixuea.courses.mymusic.manager.GlobalLyricManager;
import com.ixuea.courses.mymusic.manager.ListManager;
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
    private final MusicPlayerManager musicPlayerManager;//音乐播放管理器
    private BroadcastReceiver musicNotificationBroadcastReceiver;//音乐通知广播接收器
    private final ListManager listManager;//列表管理器
    /**
     * GlobalLyricManager:这里用的是接口
     */
    private final GlobalLyricManager globalLyricManager;//全局歌词管理器

    /**
     * 构造方法
     *
     * @param context Context
     */
    public MusicNotificationManager(Context context) {
        this.context = context.getApplicationContext();

        //注意：这类是this.context
        listManager = MusicPlayerService.getListManager(this.context);

        //获取播放管理器 注意:这里是用本对象的context
        musicPlayerManager = MusicPlayerService.getMusicPlayerManager(this.context);

        //添加播放管理器监听器
        musicPlayerManager.addMusicPlayerListener(this);

        //获取全局歌词管理器(这里并没有 用MusicPlayerService来获取)
        //用的是this.context
        globalLyricManager = GlobalLyricManagerImpl.getInstance(this.context);

        //初始化音乐通知广播接收器
        initMusicNotificationReceiver();

        //判断是否显示音乐通知
        //(之前都是点击播放后才会显示音乐通知，这样可能体验没有那么好，所以这里设置播放列表有歌曲后就会显示音乐通知)
        if (listManager.getDatum().size() > 0) {
            //显示通知
            //参数3：传入false（可能这个播放器初始化的时候，不是在播放音乐，所以这类传入false）
            NotificationUtil.showMusicNotification(context,
                    listManager.getData(),
                    false,
                    globalLyricManager.isShowing()
            );
        }
    }

    /**
     * 初始化音乐通知广播接收器
     */
    private void initMusicNotificationReceiver() {
        //创建一个广播接受者
        musicNotificationBroadcastReceiver = new BroadcastReceiver() {
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
                    //listManager.previous():上一曲的Song对象
                    listManager.play(listManager.previous());
                } else if (Constant.ACTION_PLAY.equals(action)) {
                    //播放
                    LogUtil.d(TAG, "play");

                    if (musicPlayerManager.isPlaying()) {
                        listManager.pause();
                    } else {
                        //因为这个通知栏出现的是偶，音乐已经播放量，所以可以直接调用resume方法
                        listManager.resume();
                    }

                } else if (Constant.ACTION_NEXT.equals(action)) {
                    //下一曲
                    LogUtil.d(TAG, "next");

                    listManager.play(listManager.next());
                } else if (Constant.ACTION_LYRIC.equals(action)) {
                    //歌词
                    LogUtil.d(TAG, "lyric");

                    //隐藏或显示全局歌词控件
                    showOrHideGlobalLyric();
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
     * 隐藏或显示全局歌词控件
     */
    private void showOrHideGlobalLyric() {
        if (globalLyricManager.isShowing()) {
            globalLyricManager.hide();//隐藏
        } else {
            globalLyricManager.show();//显示
        }

        //重新显示通知
        //这样才能看到歌词图标状态的变更

        //为啥要再次显示通知呢

        //因为第一次显示通知的时候，是音乐播放的时候（这个时候点击桌面歌词按钮是没有效果，
        // 因为这个通知已经出来了；想要刷新通知里面的数据，除非再次显示通知覆盖原来的这个才可以）

        //这里是在点击通过点击发放广播 回来的广播接受者处理（重新发送通知）
        NotificationUtil.showMusicNotification(context,
                listManager.getData(),
                musicPlayerManager.isPlaying(),
                globalLyricManager.isShowing());
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
        //1.因为偏好设置isShowGlobalLyric() 保存的是否显示桌面歌词控件的boolean值
        //2.而这里globalLyricManager.isShowing()：控件不为null为true（也就是显示为true）；否则为false
        //上面1 2 结果都是一样的
        NotificationUtil.showMusicNotification(context, data, false, globalLyricManager.isShowing());
    }

    @Override
    public void onPlaying(Song data) {
        //显示通知 参数3：是否播放
        NotificationUtil.showMusicNotification(context, data, true, globalLyricManager.isShowing());
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
