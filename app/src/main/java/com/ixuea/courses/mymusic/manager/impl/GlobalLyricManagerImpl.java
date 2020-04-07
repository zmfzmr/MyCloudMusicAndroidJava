package com.ixuea.courses.mymusic.manager.impl;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.PixelFormat;
import android.media.MediaPlayer;
import android.os.Build;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.WindowManager;

import com.ixuea.courses.mymusic.MainActivity;
import com.ixuea.courses.mymusic.domain.Song;
import com.ixuea.courses.mymusic.listener.GlobalLyricListener;
import com.ixuea.courses.mymusic.listener.MusicPlayerListener;
import com.ixuea.courses.mymusic.manager.GlobalLyricManager;
import com.ixuea.courses.mymusic.manager.ListManager;
import com.ixuea.courses.mymusic.manager.MusicPlayerManager;
import com.ixuea.courses.mymusic.service.MusicPlayerService;
import com.ixuea.courses.mymusic.util.Constant;
import com.ixuea.courses.mymusic.util.LogUtil;
import com.ixuea.courses.mymusic.util.NotificationUtil;
import com.ixuea.courses.mymusic.util.PreferenceUtil;
import com.ixuea.courses.mymusic.view.GlobalLyricView;

/**
 * 全局(桌面)歌词管理器实现
 */
public class GlobalLyricManagerImpl implements GlobalLyricManager, GlobalLyricListener, MusicPlayerListener {

    private static final String TAG = "GlobalLyricManagerImpl";
    private static GlobalLyricManagerImpl instance;//实例
    private final Context context;//上下文
    private WindowManager windowManager;//窗口管理器
    private WindowManager.LayoutParams layoutParams;//窗口的布局样式
    private GlobalLyricView globalLyricView;//全局歌词View
    private final PreferenceUtil sp;//偏好设置工具类
    private final ListManager listManager;//列表管理器
    private final MusicPlayerManager musicPlayerManager;//音乐播放管理器
    private BroadcastReceiver unLockGlobalLyricBroadcastReceiver;

    /**
     * 构造方法
     *
     * @param context
     */
    public GlobalLyricManagerImpl(Context context) {
        this.context = context.getApplicationContext();//记得写这个，否则会有内存泄漏
        //初始化编号设置工具类
        sp = PreferenceUtil.getInstance(this.context);

        //初始音乐播放管理器
        musicPlayerManager = MusicPlayerService.getMusicPlayerManager(this.context);

        //添加播放监听器
        musicPlayerManager.addMusicPlayerListener(this);

        //初始化列表管理器
        listManager = MusicPlayerService.getListManager(this.context);

        //初始化窗口管理器
        initWindowManager();

        //后台杀掉 重新进入应用的情况

        //从偏好设置中获取是否要显示全局歌词
        if (sp.isShowGlobalLyric()) {
            //创建全局歌词View
            initGlobalLyricView();

            //如果原来锁定了歌词
            if (sp.isGlobalLyricLock()) {
                lock();
            }
        }
    }

    /**
     * 创建全局歌词View
     */
    private void initGlobalLyricView() {
//        //创建一个测试文本(传入Context对象)
//        TextView tv = new TextView(context);
//        tv.setText("这是一个简单的文本");
//        if (tv.getParent() == null) {//tv.getParent():view的父容器（父view）
//            //如果没有添加
//            //就添加
//            windowManager.addView(tv, layoutParams);
//        }

        if (globalLyricView == null) {
            //创建全局歌词控件
            globalLyricView = new GlobalLyricView(context);

            //设置回调
            globalLyricView.setGlobalLyricListener(this);

        }

        if (globalLyricView.getParent() == null) {
            //如果没有添加
            //就添加
            windowManager.addView(globalLyricView, layoutParams);
        }

        //显示初始化数据(这个桌面歌词显示后，就显示初始化数据)
        showLyricData();

        //显示播放状态
        globalLyricView.setPlay(musicPlayerManager.isPlaying());

        //这个是杀死应用后，重新进入应用，显示桌面歌词控件后，显示通知（否则不写的话，通知不会显示的）

        //显示音乐通知 (参数4：因为已经调用了initGlobalLyricView 来时显示控件 所以这里是显示了，传入true)
        NotificationUtil.showMusicNotification(context,
                listManager.getData(),
                musicPlayerManager.isPlaying(),
                true);

    }

    /**
     * 初始化窗口管理器
     * <p>
     * 因为全局View是通过他显示的
     * <p>
     * 可以在构造方法中初始化
     * <p>
     * 也可以延迟到要显示的时候才初始化
     * 这样效果更高
     */
    private void initWindowManager() {
        if (windowManager == null) {
            //获取系统窗口管理器
            windowManager = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);

            //窗体的布局样式（显示一个窗体 告诉系统，我的位置是什么 宽高是什么）
            layoutParams = new WindowManager.LayoutParams();

            //设置窗体显示类型
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                //6.0及以上版本要设置该类型
                layoutParams.type = WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY;
            } else {
                layoutParams.type = WindowManager.LayoutParams.TYPE_SYSTEM_ALERT;
            }

            //设置显示的模式
            layoutParams.format = PixelFormat.RGBA_8888;

            //设置对齐的方法
            //CENTER_HORIZONTAL:这里是水平居中（水平居中其实可以不用设置，因为宽高（宽高 下面代码会设置）都是和父窗体一样）
            //Gravity.TOP:这是是位于顶部
            layoutParams.gravity = Gravity.CENTER_HORIZONTAL | Gravity.TOP;

            //设置窗体宽度和高度
            DisplayMetrics dm = new DisplayMetrics();
            windowManager.getDefaultDisplay().getMetrics(dm);//这里传入dm，会这个值 保存到dm(DisplayMetrics对象)里面

            //和屏幕一样宽
            layoutParams.width = dm.widthPixels;
            //高度是包裹内容(WindowManager:大写)
            layoutParams.height = WindowManager.LayoutParams.WRAP_CONTENT;

            //从偏好设置中获取坐标
            //如果在真实项目中
            //可能还会实现不同的用户有不同的偏好设置

            //目前是写死的
            layoutParams.y = 100;

            //设置全局歌词控件状态
            setGlobalLyricStatus();

        }


    }

    /**
     * 获取全局歌词管理器
     * <p>
     * 这种单例 在移动端是适用的，但是想java web 那种高并发就是另一种写法
     * 因为移动端 用不到这种高并发，写的代码越大 就越慢
     */

    public static GlobalLyricManagerImpl getInstance(Context context) {
        if (instance == null) {
            instance = new GlobalLyricManagerImpl(context);
        }
        return instance;
    }

    @Override
    public void show() {
        LogUtil.d(TAG, "show");

        //初始化全局全局歌词控件
        initGlobalLyricView();

        //设置了显示了全局歌词(显示桌面歌词 这个控件  就把设置的结果保存到持久化数据文件中)
        sp.setShowGlobalLyric(true);
    }

    @Override
    public void hide() {
        //移除全局歌词控件
        windowManager.removeView(globalLyricView);
        globalLyricView = null;//记得显示为null，下次显示的时候 就会重新创建这个globalLyricView对象了
        //设置没有显示全局歌词
        sp.setShowGlobalLyric(false);

        //重新显示音乐通知
        //参数4：因为隐藏了，所以这里是false

        //这里再写个通知，显示了2个通知（后面一个覆盖了前面的）
        // （第一个通知在MusicNotificationManager的showOrHideGlobalLyric (只不过这个通知方法 第4个参数不一样，其实结果一样的)）
        NotificationUtil.showMusicNotification(context,
                listManager.getData(),
                musicPlayerManager.isPlaying(),
                false);
    }

    @Override
    public boolean isShowing() {
        //这个控件globalLyricView不为null 说明显示了（说明 窗口管理器已经添加view了：windowManager.addView(globalLyricView, layoutParams);）
        return globalLyricView != null;
    }

    @Override
    public void tryHide() {

    }

    @Override
    public void tryShow() {

    }

    /**
     * 显示歌词数据
     */
    public void showLyricData() {
        if (globalLyricView == null) {
            return;
        }

        //获取当前播放的音乐
        if (listManager.getData() == null || listManager.getData().getParsedLyric() == null) {
            //清空原来的歌词
            globalLyricView.clearLyric();
        } else {
            //显示第二个歌词控件
            globalLyricView.setSecondLyricView();

            //设置歌词模式
            globalLyricView.setAccurate(listManager.getData().getParsedLyric().isAccurate());

            //如果显示了歌词
            //执行一次进度方法
            //相当于初始化数据

            //这里是直接调用进度回调方法，然后传入数据(就是第一次分发进度)
            //（相当于第一次手动调用进度回调，之后如果一直播放，则一直调用onProgress）
            //这里的作用是：点击（词）隐藏后，再次点击，能再次看到当前绘制的歌词
            onProgress(listManager.getData());
        }

    }

    //全局歌词控件监听器
    @Override
    public void onLogoClick() {
        LogUtil.d(TAG, "onLogoClick");
        //参数1：是从外面传入进来的context
        //这里传入MainActivity（希望用户点击播放界面返回后还是主界面）
        //因为这里传入的action是Constant.ACTION_MUSIC_PLAY_CLICK，
        // 所以进入MainActivity后判断跳转到MusicPlayerActivity了
        Intent intent = new Intent(context, MainActivity.class);
        intent.setAction(Constant.ACTION_MUSIC_PLAY_CLICK);
        //在Activity以外启动界面
        //都要写这个标识
        //具体的还比较复杂
        //基础课程中讲解
        //这里学会这样用就行了
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(intent);
    }

    @Override
    public void onCloseClick() {
        LogUtil.d(TAG, "onCloseClick");

        hide();//直接调用管理器的方法就行
    }

    @Override
    public void onLockClick() {
        LogUtil.d(TAG, "onLockClick");

        lock();
    }

    /**
     * 锁定全局歌词锁定状态
     */
    private void lock() {
        //保存歌词锁定状态
        sp.setGlobalLyricLock(true);

        //设置全局歌词控件状态
        setGlobalLyricStatus();

        //显示简单模式
        globalLyricView.simpleStyle();

        //更新布局
        //这个必须要更新布局（因为禁用参数的 flag是设置到layoutParams对象中，再点击lock按钮之前，
        // 这个layoutParams对象已经使用了，所以不更新布局的话，不会生效的）
        updateView();

        //显示解锁全局歌词通知
        NotificationUtil.showUnLockGlobalLyricNotification(context);

        //注册接收解锁全局歌词广播接收器
        //虽然在点击锁定的时候注册了 广播，但是还没有发送广播，这里面的回调方法onReceive不会执行
        registerUnLockGlobalLyricReceiver();
    }

    /**
     * 注解接收解锁全局歌词广播接收器
     */
    private void registerUnLockGlobalLyricReceiver() {
        //创建广播接收者
        unLockGlobalLyricBroadcastReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                if (Constant.ACTION_UNLOCK_LYRIC == intent.getAction()) {
                    //歌词解锁事件
                    unlock();
                }
            }
        };

        IntentFilter intentFilter = new IntentFilter();

        //只监听歌词解析 (ACTION_UNLOCK_LYRIC:点击通知发送的那个广播值)
        intentFilter.addAction(Constant.ACTION_UNLOCK_LYRIC);
        context.registerReceiver(unLockGlobalLyricBroadcastReceiver, intentFilter);
    }

    /**
     * 解锁歌词
     */
    private void unlock() {
        //设置没有锁定歌词
        sp.setGlobalLyricLock(false);
        //设置歌词状态
        setGlobalLyricStatus();

        //结果后显示标准样式
        globalLyricView.normalStyle();

        //更新view
        updateView();

        //清除歌词解锁通知
        //不用清除（系统默认解锁后就会清除）之前那个音乐通知没有消息 是设置了setAutoCancel(false):点击不消失
//        NotificationUtil.clearUnlockGlobalLyricNotification(context);

        //解除接收全局歌词时间广播接接收者
        unRegisterUnlockGlobalLyricReceiver();

    }

    /**
     * 解除接收全局歌词时间广播接接收者
     */
    private void unRegisterUnlockGlobalLyricReceiver() {
        if (unLockGlobalLyricBroadcastReceiver != null) {
            context.unregisterReceiver(unLockGlobalLyricBroadcastReceiver);
            unLockGlobalLyricBroadcastReceiver = null;
        }
    }

    /**
     * 更新布局
     */
    private void updateView() {
        windowManager.updateViewLayout(globalLyricView, layoutParams);
    }

    private void setGlobalLyricStatus() {
        if (sp.isGlobalLyricLock()) {
            //已经锁定了

            //窗口不接受任何事件
            //FLAG_NOT_TOUCHABLE:表示不接受事件（禁止触摸控件）
            //FLAG_NOT_FOCUSABLE:这个记得加上，否则返回按钮不能使用
            layoutParams.flags = WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE |
                    WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE;

        } else {
            //没有锁定

            //窗口可以接受触摸事件
            //FLAG_NOT_TOUCH_MODAL和FLAG_NOT_FOCUSABLE 表示可以：接受触摸事件
            //注意FLAG_NOT_TOUCH_MODAL：和上面的FLAG_NOT_TOUCHABLE区别
            layoutParams.flags = WindowManager.LayoutParams.FLAG_NOT_TOUCH_MODAL |
                    WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE;
        }

    }

    @Override
    public void onPreviousClick() {
        LogUtil.d(TAG, "onPreviousClick");

        listManager.play(listManager.previous());
    }

    @Override
    public void onPlayClick() {
        LogUtil.d(TAG, "onPlayClick");
        if (musicPlayerManager.isPlaying()) {
            listManager.pause();
        } else {
            listManager.resume();
        }
    }

    @Override
    public void onNextClick() {
        LogUtil.d(TAG, "onNextClick");
        listManager.play(listManager.next());
    }
    //end 全局歌词控件监听器

    //播放管理器监听器
    @Override
    public void onPaused(Song data) {
        if (globalLyricView != null) {
            globalLyricView.setPlay(false);//设置播放状态为false
        }
    }

    @Override
    public void onPlaying(Song data) {
        if (globalLyricView != null) {
            globalLyricView.setPlay(true);//设置播放状态为true
        }
    }

    @Override
    public void onPrepared(MediaPlayer mp, Song data) {

    }

    @Override
    public void onProgress(Song data) {
        if (globalLyricView != null) {
            //可能让这个控件自己处理不是那么好，（控件不应该处理逻辑相关的）
            globalLyricView.onProgress(data);
        }
    }

    @Override
    public void onLyricChanged(Song data) {
        showLyricData();
    }

    //end 播放管理器监听器
}
