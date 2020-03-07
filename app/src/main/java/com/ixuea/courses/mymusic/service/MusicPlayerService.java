package com.ixuea.courses.mymusic.service;

import android.app.Notification;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.IBinder;

import com.ixuea.courses.mymusic.manager.ListManager;
import com.ixuea.courses.mymusic.manager.MusicPlayerManager;
import com.ixuea.courses.mymusic.manager.impl.ListManagerImpl;
import com.ixuea.courses.mymusic.manager.impl.MusicNotificationManager;
import com.ixuea.courses.mymusic.manager.impl.MusicPlayerManagerImpl;
import com.ixuea.courses.mymusic.util.LogUtil;
import com.ixuea.courses.mymusic.util.NotificationUtil;
import com.ixuea.courses.mymusic.util.ServiceUtil;

/**
 * 音乐播放service
 */
public class MusicPlayerService extends Service {
    private static final String TAG = "MusicPlayerService";
    private MusicNotificationManager musicNotificationManager;

    /**
     * 构造方法
     */
    public MusicPlayerService() {
    }

    /**
     * 获取音乐播放Manager
     * <p>
     * 为什么不直接将逻辑写到Service呢？
     * 是因为操作service要么通过bindService
     * 那么startService来使用
     * 比较麻烦
     * （这里通过一个方法，来启动service，返回了一个音乐播放管理器）
     * 而不需要bindService
     */
    public static MusicPlayerManager getMusicPlayerManager(Context context) {
        context = context.getApplicationContext();//这里要获取全局的上下文，否则会发生内存泄漏

        //尝试启动service
        ServiceUtil.startService(context, MusicPlayerService.class);

        return MusicPlayerManagerImpl.getInstance(context);
    }

    /**
     * 获取列表管理器
     */
    public static ListManager getListManager(Context context) {
        context = context.getApplicationContext();

        //尝试启动service（不启动service也可以播放，启动service只为应用保活，防止资源不足的时候把我们的应用杀死掉
        // 所以是提高应用的优先级）
        ServiceUtil.startService(context, MusicPlayerService.class);

        return ListManagerImpl.getInstance(context);
    }

    /**
     * Service创建了
     * 类似Activity的onCreate
     */
    @Override
    public void onCreate() {
        super.onCreate();
        LogUtil.d(TAG, "onCreate");

        //初始化音乐通知管理器
        musicNotificationManager = MusicNotificationManager.getInstance(getApplicationContext());
    }

    /**
     * 启动service调用（如果已经启动，再次启动还是会调用）
     * <p>
     * 总结：只要启动都会调用
     * <p>
     * 多次启动也调用该方法
     *
     * @param intent  Intent
     * @param flags   int
     * @param startId int
     */
    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        LogUtil.d(TAG, "onStartCommand");
        //因为这个API是8.0才有的
        //所以要这样判断版本
        //不然低版本会崩溃
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            //设置Service为前台service
            //提高应用的优先级

            //获取通知
            Notification notification = NotificationUtil.getServiceForeground(getApplicationContext());

            //Id写0：这个通知就不会显示
            //对于我们这里来说
            //就需要不显示(我们这里不显示通知，只是应用保活（防止被杀死）)
            startForeground(0, notification);

        }
        return super.onStartCommand(intent, flags, startId);
    }

    /**
     * 销毁时调用
     */
    @Override
    public void onDestroy() {
        LogUtil.d(TAG, "onDestroy");

        //停止前台服务
        //true:移除之前的通知
        stopForeground(true);

        super.onDestroy();
    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        throw new UnsupportedOperationException("Not yet implemented");
    }
}
