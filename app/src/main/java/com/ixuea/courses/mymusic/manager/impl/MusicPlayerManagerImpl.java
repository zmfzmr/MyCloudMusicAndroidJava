package com.ixuea.courses.mymusic.manager.impl;

import android.content.Context;
import android.media.MediaPlayer;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;

import com.ixuea.courses.mymusic.domain.Song;
import com.ixuea.courses.mymusic.listener.Consumer;
import com.ixuea.courses.mymusic.listener.MusicPlayerListener;
import com.ixuea.courses.mymusic.manager.MusicPlayerManager;
import com.ixuea.courses.mymusic.util.ListUtil;
import com.ixuea.courses.mymusic.util.LogUtil;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import androidx.annotation.NonNull;

import static com.ixuea.courses.mymusic.util.Constant.DEFAULT_TIME;
import static com.ixuea.courses.mymusic.util.Constant.MESSAGE_PROGRESS;

/**
 * 播放管理器默认实现
 */
public class MusicPlayerManagerImpl
        implements MusicPlayerManager, MediaPlayer.OnCompletionListener {
    private static final String TAG = "MusicPlayerManagerImpl";
    private static MusicPlayerManagerImpl instance;//实例对象
    private final Context context;//上下文
    private final MediaPlayer player;//播放器
    private Song data;//当前播放的音乐对象

    /**
     * 播放器状态监听器
     */
    private List<MusicPlayerListener> listeners = new ArrayList<>();

    /**
     * 定时器任务
     */
    private TimerTask timerTask;
    private Timer timer;

    /**
     * 私有构造方法
     * <p>
     * 这里外部就不能通过new方法来创建对象了
     *
     * @param context Context
     */
    private MusicPlayerManagerImpl(Context context) {
        this.context = context.getApplicationContext();

        //初始化播放器
        player = new MediaPlayer();

        //设置播放器监听
        initListeners();
    }

    /**
     * 设置播放器监听
     */
    private void initListeners() {
        //设置播放器准备监听器
        player.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
            /**
             * 播放器准备开始播放
             * <p>
             * 这里可以获取到音乐时长
             * 如果是视频还能获取到视频宽高等信息
             *
             * @param mp 音乐播放器对象
             */
            @Override
            public void onPrepared(MediaPlayer mp) {
                LogUtil.d(TAG, "onPrepared");

                //将音乐总时长保存到音乐对象
                data.setDuration(mp.getDuration());

                //回调监听器(通知activity)
                //函数式接口中到的方法参数，这个可以随便明明都可以
                ListUtil.eachListener(listeners, listener -> listener.onPrepared(mp, data));
            }
        });

        //设置播放完毕监听器
        player.setOnCompletionListener(this);
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

            //回调监听器
            publishPlayingStatus();

            //启动播放进度通知
            startPublishProgress();

        } catch (IOException e) {
            e.printStackTrace();
            //TODO 错误了
        }

    }

    /**
     * 发布播放中状态
     */
    private void publishPlayingStatus() {
//        for (MusicPlayerListener listener : listeners) {
//            listener.onPlaying(data);
//        }
        //使用重构后的方法
        ListUtil.eachListener(listeners, new Consumer<MusicPlayerListener>() {
            @Override
            public void accept(MusicPlayerListener musicPlayerListener) {
                musicPlayerListener.onPlaying(data);
            }
        });
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

//            //回调监听器（暂停状态）
//            for (MusicPlayerListener listener : listeners) {
//                listener.onPaused(data);
//            }

            //使用重构后的方法
            ListUtil.eachListener(listeners, musicPlayerListener -> musicPlayerListener.onPaused(data));

            //停止进度通知
            stopPublishProgress();

        }
    }

    /**
     * MusicPlayerManagerImpl 的继续播放方法（注意：这个不是activity中的那个onResume）
     */
    @Override
    public void resume() {
        if (!isPlaying()) {
            //如果没有在播放就播放
            player.start();

            //回调监听器(已经播放了)
            publishPlayingStatus();

            //启动进度通知
            startPublishProgress();
        }
    }

    @Override
    public void addMusicPlayerListener(MusicPlayerListener listener) {
        //listener-->SimplePlayerActivity实现
        if (!listeners.contains(listener)) {//不包含这个监听器的时候才添加
            listeners.add(listener);
        }

        //启动进度通知(当Activity中onResume继续执行的时候，添加监听器后，
        // 重新调用startPublishProgress方法向activity，传递进度)
        //原因：跳转到后台，监听器移除了，定时器没有开始定时（startPublishProgress判断的），
        // 所以Activity中回到前台调用onResume方法，添加监听的时候，重新调用startPublishProgress 重新传递进度
        startPublishProgress();
    }

    @Override
    public void removeMusicPlayerListener(MusicPlayerListener listener) {
        //这里可以不用判断，就算不存在，应该也不会报错
        listeners.remove(listener);
    }

    @Override
    public Song getData() {//这个data，当外界activity中调用play中传过来的
        return data;
    }

    @Override
    public void seekTo(int progress) {
        //注意：这里是调用播放器MediaPlayer 的seekTo方法
        player.seekTo(progress);
    }

    /**
     * 启动播放进度通知
     */
    private void startPublishProgress() {
        if (isEmptyListeners()) {
            //没有进度回调就不启动
            return;
        }

        if (!isPlaying()) {
            //没有播放音乐就不用启动
            return;
        }

        if (timerTask != null) {//说明已经启动了，直接返回（中断方法的运行）
            //已经启动了
            return;
        }

        //创建一个任务
        timerTask = new TimerTask() {
            @Override
            public void run() {
                //如果没有监听器了就停止定时器
                if (isEmptyListeners()) {
                    stopPublishProgress();
                    return;
                }

                LogUtil.d(TAG, "time task progress");
                //这里是子线程
                //不能直接操作UI
                //为了方便外部
                //在内部就切换到主线程
//                HandlerUtil.sendMessage(Constant.MESSAGE_PROGRESS);
                //HandlerUtil.getHandler().obtainMessage(MESSAGE_PROGRESS).sendToTarget();
                //上面的2个写法 发现无法在回调的handleMessage使用listeners监听器
                //所以把handler写在本类中

                handler.obtainMessage(MESSAGE_PROGRESS).sendToTarget();
            }
        };

        //创建一个定时器
        timer = new Timer();

        //启动一个持续的任务
        //
        //        //启动一个持续的任务
        //
        //        //16毫秒
        //        //为什么是16毫秒？
        //        //因为后面我们要实现卡拉OK歌词
        //        //为了画面的连贯性
        //        //应该保持1秒60帧
        //        //所以1/60；就是一帧时间
        //        //如果没有卡拉OK歌词
        //那么每秒钟刷新一次即可
        //参数1：定时任务 2：延迟 3：间隔 16毫秒
        //这里说的是每隔16毫秒，启动播放任务（也就是说每个16毫秒启动TimerTask子类中的run方法）
        timer.schedule(timerTask, 0, DEFAULT_TIME);

    }

    /**
     * 是否没有进度监听器（监听器集合为null）
     */
    private boolean isEmptyListeners() {
        return listeners.size() == 0;
    }

    /**
     * 停止播放进度通知
     * <p>
     * 这里timerTask timer必须要置null的，否则app运行到后台后(这个时候监听器listener已经移除了(没有监听器就不能监听到activity的操作))
     * //所以如果这个时候没有置为null，会导致这个定时器每隔16毫秒运行
     * 总结：移除监听器后，要把timerTask timer值null（还要cancel()）
     */
    private void stopPublishProgress() {
        //停止定时器任务
        if (timerTask != null) {
            timerTask.cancel();
            timerTask = null;
        }

        //停止定时器
        if (timer != null) {
            timer.cancel();
            timer = null;
        }

    }

    @Override
    public void setLooping(boolean looping) {
        //播放器是否单曲循环
        player.setLooping(looping);
    }

    /**
     * 创建Handler
     * 用来将事件转换到主线程
     * <p>
     * 当然目前这样写，可能会有内存泄漏的问题，这里先不说，先这样写吧
     */
    private Handler handler = new Handler(Looper.getMainLooper()) {
        @Override
        public void handleMessage(@NonNull Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case MESSAGE_PROGRESS:
                    //播放进度回调

                    //将进度设置到音乐对象(player.getCurrentPosition():获取当前播放位置（当前进度）)
                    //这个data，是activity那边调用play后传递过来的
                    data.setProgress(player.getCurrentPosition());

                    //回调监听
                    ListUtil.eachListener(listeners, listener -> listener.onProgress(data));
                    break;
                default:
                    break;
            }

        }
    };

    /**
     * 播放完毕了回调
     *
     * onCompletion:这个回调是MediaPlayer里面的
     * @param mp MediaPlayer
     */
    @Override
    public void onCompletion(MediaPlayer mp) {
        //回调监听器
        ListUtil.eachListener(listeners, listener -> listener.onCompletion(mp));
    }
}
