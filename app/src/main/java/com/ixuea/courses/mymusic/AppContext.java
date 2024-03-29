package com.ixuea.courses.mymusic;

import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.content.Intent;
import android.media.MediaScannerConnection;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;

import com.facebook.stetho.Stetho;
import com.fcfrt.netbua.FcfrtNetStatusBus;
import com.ixuea.android.downloader.DownloadService;
import com.ixuea.android.downloader.callback.DownloadManager;
import com.ixuea.android.downloader.config.Config;
import com.ixuea.courses.mymusic.activity.LoginOrRegisterActivity;
import com.ixuea.courses.mymusic.domain.Session;
import com.ixuea.courses.mymusic.domain.event.LoginSuccessEvent;
import com.ixuea.courses.mymusic.domain.event.OnNewMessageEvent;
import com.ixuea.courses.mymusic.manager.impl.ActivityManager;
import com.ixuea.courses.mymusic.util.Constant;
import com.ixuea.courses.mymusic.util.LogUtil;
import com.ixuea.courses.mymusic.util.MessageUtil;
import com.ixuea.courses.mymusic.util.ORMUtil;
import com.ixuea.courses.mymusic.util.PreferenceUtil;
import com.ixuea.courses.mymusic.util.PushUtil;
import com.ixuea.courses.mymusic.util.StringUtil;
import com.ixuea.courses.mymusic.util.ToastUtil;
import com.mob.MobSDK;
import com.tencent.bugly.Bugly;

import org.greenrobot.eventbus.EventBus;

import java.io.File;
import java.lang.ref.WeakReference;

import androidx.appcompat.app.AlertDialog;
import androidx.emoji.bundled.BundledEmojiCompatConfig;
import androidx.emoji.text.EmojiCompat;
import androidx.multidex.MultiDex;
import cn.jiguang.analytics.android.api.JAnalyticsInterface;
import cn.jpush.android.api.JPushInterface;
import cn.jpush.im.android.api.JMessageClient;
import cn.jpush.im.android.api.event.MessageEvent;
import cn.jpush.im.android.api.event.NotificationClickEvent;
import cn.jpush.im.android.api.model.Message;
import cn.jpush.im.api.BasicCallback;
import cn.sharesdk.framework.Platform;
import cn.sharesdk.framework.ShareSDK;
import cn.sharesdk.sina.weibo.SinaWeibo;
import cn.sharesdk.tencent.qq.QQ;
import es.dmoral.toasty.Toasty;
import io.realm.Realm;

import static android.os.Build.VERSION.SDK_INT;

/**
 * 全局Application
 */
public class AppContext extends Application implements Application.ActivityLifecycleCallbacks {
    private static final String TAG = "AppContext";
//    public class AppContext extends MultiDexApplication {

    /**
     * 上下文
     */
    private static AppContext context;
    private DownloadManager downloadManager;//下载管理器实例
    private ActivityManager activityManager;//界面管理器
    private PreferenceUtil sp;//偏好工具类
    /**
     * 当前Activity引用
     * 通过弱引用保存
     * 不影响gc销毁界面
     */
    private WeakReference<Activity> currentActivity;

    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
        //初始化MultiDex
        MultiDex.install(this);
    }

    /**
     * 创建了 （第一次创建应用或者杀掉应用后就会执行一次这个方法）
     */
    @Override
    public void onCreate() {
        super.onCreate();

        sp = PreferenceUtil.getInstance(getApplicationContext());

        //尽可能早的进行这一步操作(这里放在最前面)
        //建议在 Application 中完成初始化操作
        FcfrtNetStatusBus.getInstance().init(this);

        context = this;
        //初始化toast工具类
        Toasty.Config.getInstance().apply();
        //初始化Toast工具类
        ToastUtil.init(getApplicationContext());

        //初始化Stetho抓包
        //使用默认参数初始化
        Stetho.initializeWithDefaults(this);

        //初始化Realm数据库
        //还有更多的初始化配置
        //官网有介绍

        //context:就是的当前的AppContext对象，全局的
        Realm.init(context);

        //初始化emoji(表情符号)
        BundledEmojiCompatConfig config = new BundledEmojiCompatConfig(this);
        EmojiCompat.init(config);

        //这里我们是在调试状态下(发送广播，更新内容到媒体库)
        if (LogUtil.isDebug) {
            //这个表示扫描文件下的所有内容到媒体库
            updateMedia(context, "/sdcard/Download/");
//            updateMedia(context,"/sdcard/Download/冷漠-笑着说分手.mp3");
//            updateMedia(context,"/sdcard/Download/刀郎-家人.mp3");
//            updateMedia(context,"/sdcard/Download/刘若英-后来.mp3");
//            updateMedia(context,"/sdcard/Download/庞龙-人生第一次.mp3");
        }

        //初始化Activity管理器
        activityManager = ActivityManager.getInstance();

        //注册界面声明周期监听
        //什么是ActivityLifecycle
        //简单来说，他是Android提供的一套API，可以用来监听当前应用，所有界面的生命周期。
        registerActivityLifecycleCallbacks(this);

        //初始化腾讯Bugly服务
        initBugly();

        //初始化极光
        initJiGuang();

        //判断是否登录了(杀死再次进入应用,再次登录极光聊天)
        if (sp.isLogin()) {
            //初始化其他需要登录后才初始化的内容
            onLogin();
        }
    }

    /**
     * 初始化极光
     */
    private void initJiGuang() {
        //初始化极光统计
        JAnalyticsInterface.init(getApplicationContext());
        //设计极光统计调试模式
        JAnalyticsInterface.setDebugMode(LogUtil.isDebug);

        //初始化极光IM
        JMessageClient.init(getApplicationContext());

        //注册极光消息回调 (这个this 也就是应用上下文，跟getApplicationContext都是一样的)
        //回调： 别人给我发的消息
        JMessageClient.registerEventReceiver(this);
    }

    /**
     * 初始化腾讯Bugly服务
     */
    private void initBugly() {
        //更多配置参数
        //https://bugly.qq.com/docs/user-guide/instruction-manual-android/

        //crash ： 崩溃  report： 报告
        //参数1： 上下文 这里传入应用的上下文
        // 2: Bugly官网 应用的App id
        // 3： studio工具左边Build Variants栏 为debug状态，
        //     那么这个BuildConfig.DEBUG为true，否则为false
        //     老师说： 打包也是关闭的(false的) ，这个还没试过，不知道情况
//        CrashReport.initCrashReport(getApplicationContext(),
//                Constant.BUGLY_APP_ID, LogUtil.isDebug);

        //初始化Bugly所有服务
        //包括异常上报
        //更新
        Bugly.init(getApplicationContext(),
                Constant.BUGLY_APP_ID, LogUtil.isDebug);
    }

    /**
     * 这个主要是根据路径更新媒体库的路径(也就是扫描这个路径的文件到我们的媒体库)
     *
     * @param context
     * @param path
     */
    public static void updateMedia(final Context context, String path) {

        if (SDK_INT >= Build.VERSION_CODES.KITKAT) {//当大于等于Android 4.4时
            MediaScannerConnection.scanFile(context, new String[]{path}, null, new MediaScannerConnection.OnScanCompletedListener() {
                @Override
                public void onScanCompleted(String path, Uri uri) {
                    Intent mediaScanIntent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
                    mediaScanIntent.setData(uri);
                    context.sendBroadcast(mediaScanIntent);
                }
            });

        } else {//Andrtoid4.4以下版本
            context.sendBroadcast(new Intent(Intent.ACTION_MEDIA_MOUNTED, Uri.fromFile((new File(path).getParentFile()))));
        }
    }

    /**
     * 获取当前上下文
     *
     * @return Context
     */
    public static AppContext getInstance() {
        return context;
    }

    /**
     * 当用户登录了
     *
     * @param data Session
     *             保存到PreferenceUtil中SharedPreferences对象里面
     */
    public void login(Session data) {
        //保存登录后的session
        sp.setSession(data.getSession());

        //保存用户Id
        sp.setUserId(data.getUser());

        //初始化其他需要登录后初始化的内容
        onLogin();

        //发送登录成功通知
        //目的是一些界面需要接收该事件
        EventBus.getDefault().post(new LoginSuccessEvent());

        //初始化shareSDK
        MobSDK.init(this);

    }

    /**
     * 初始化其他需要登录后初始化的内容
     */
    private void onLogin() {
        //登录聊天服务器
        String id = StringUtil.wrapperUserId(sp.getUserId());
        JMessageClient.login(id, id, new BasicCallback() {
            /**
             * @param responseCode    - 0 表示正常。大于 0 表示异常
             *                        responseMessage 会有进一步的异常信息。
             * @param responseMessage - 一般异常时会有进一步的信息提示。
             */
            @Override
            public void gotResult(int responseCode, String responseMessage) {
                if (responseCode != 0) {
                    LogUtil.d(TAG, "message login failed:" + responseMessage);

                    ToastUtil.errorShortToast(R.string.error_message_login);
                } else {
                    LogUtil.d(TAG, "message login success");
                }
            }
        });

        //设置推送别名
        //这里将别名设置为了用户Id(好处是： 以后可以通过别名的方式用UserId去推送)
        //官网文档：https://docs.jiguang.cn/jpush/client/Android/android_api/
        //也可以到官网看看这个别名到底是干什么的
        JPushInterface.setAlias(getApplicationContext(), 0, sp.getUserId());
        //调试模式
        JPushInterface.setDebugMode(true);

        //初始化极光推送
        JPushInterface.init(getApplicationContext());

        //打印推送注册Id(类似于设备id，它有一个唯一值)
        //只是为了调试
//        String pushId = JPushInterface.getRegistrationID(getApplicationContext());
        String pushId = PushUtil.getPushId(getApplicationContext());
        LogUtil.d(TAG, "onLogin pushId:" + pushId);
    }

    /**
     * 退出
     * 清除信息后，跳转到 登录注册界面
     */
    public void logout() {

        logoutSilence();

        //清除所有界面
        //因为我们应用是只有登录了
        //才能进入
        //所以用户退出了
        //所有界面都应该关闭
        activityManager.clear();

        //退出后跳转到登录注册界面
        //因为我们的应用实现的是必须登录才能进入首页
        Intent intent = new Intent(getApplicationContext(), LoginOrRegisterActivity.class);

        //在Activity以外启动界面
        //都要写这个标识
        //具体的还比较复杂
        //基础课程中讲解
        //这里学会这样用就行了
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        //启动界面
        startActivity(intent);
    }

    /**
     * 第三方平台退出
     *
     * @param name
     */
    private void otherLogout(String name) {
        //清除第三方平台登录信息
        Platform platform = ShareSDK.getPlatform(name);
        if (platform.isAuthValid()) {//有这个认证或者有效期的（就是第三方登录后，这个应该是为true的）
            platform.removeAccount(true);//移除第三方账号
        }
    }

    /**
     * 退出后，可能需要做一些处理
     *
     * 主要用来做一些清理工作
     * 例如：关闭推送，断开聊天服务器，销毁数据库对象
     */
    private void onLogout() {
        //销毁数据库管理器
        ORMUtil.destroy();
        //销毁下载管理器
        if (downloadManager != null) {
            //销毁下载框架实例
            //因为不同的用户使用不同的实例
            //(用户退出后，这个管理器就销毁了，下次进来的时候重新创建)
            downloadManager.destroy();
            downloadManager = null;//注意:销毁后还要置为null
        }

        //退出极光聊天
        JMessageClient.logout();
    }

    /**
     * 获取下载管理器
     *
     * @return DownloadManager对象
     * 注意：DownloadManager 和 Config 都是aixuea 包里面的
     *
     * 因为这里是在AppContext中(Appcontext对象应用结束后销毁)
     * 所以这里的 DownloadManager对象(保存到成员里面，可以不用静态修饰)
     */
    public DownloadManager getDownloadManager() {
        if (downloadManager == null) {
            //获取偏好设置工具栏
            PreferenceUtil sp = PreferenceUtil.getInstance(context);
            //创建下载框架配置
            //aixuea 里面的包类
            Config config = new Config();
            //数据库名称添加了用户id
            //所以不同的用户数据是隔离的
            //注意：这个一开始并没有实现多用户(因为在SplashActivity中就间接调用这个方法，可以调试看看)
            config.setDatabaseName(String.format("%s_download_info.db", sp.getUserId()));
            //获取下载管理器
            downloadManager = DownloadService.getDownloadManager(context, config);
        }
        //返回下载管理器实例
        return downloadManager;
    }
    //Activity声明周期回调

    /**
     * 界面创建了 对应ActivityonCreate方法 其他方法依次类推...
     *
     * @param activity
     * @param savedInstanceState
     */
    @Override
    public void onActivityCreated(Activity activity, Bundle savedInstanceState) {
        LogUtil.d(TAG, "onActivityCreated:" + activity);
        activityManager.add(activity);
    }

    /**
     * 界面准备显示了
     *
     * @param activity
     */
    @Override
    public void onActivityStarted(Activity activity) {
        LogUtil.d(TAG, "onActivityStarted:" + activity);
    }

    /**
     * 界面显示了
     *
     * @param activity
     */
    @Override
    public void onActivityResumed(Activity activity) {
        LogUtil.d(TAG, "onActivityResumed:" + activity);

        //保存界面
        //目的是显示全局对话框的时候用到
        //注意：这里用到了弱引用，防止内存泄漏
        // (因为当前AppContext对象不销毁，这个当前Activity是不能释放的，所以使用弱引用就没有这个问题了)
        currentActivity = new WeakReference<Activity>(activity);
    }

    /**
     * 界面暂停了
     *
     * @param activity
     */
    @Override
    public void onActivityPaused(Activity activity) {
        LogUtil.d(TAG, "onActivityPaused:" + activity);
    }

    /**
     * 界面停止了
     *
     * @param activity
     */
    @Override
    public void onActivityStopped(Activity activity) {
        LogUtil.d(TAG, "onActivityStopped:" + activity);
    }

    /**
     * 界面保存数据恢复了
     *
     * @param activity
     * @param outState
     */
    @Override
    public void onActivitySaveInstanceState(Activity activity, Bundle outState) {
        LogUtil.d(TAG, "onActivitySaveInstanceState:" + activity);
    }

    /**
     * 界面销毁了
     *
     * @param activity
     */
    @Override
    public void onActivityDestroyed(Activity activity) {
        LogUtil.d(TAG, "onActivityDestroyed:" + activity);
        //移除
        activityManager.remove(activity);

    }
    //end Activity声明周期回调

    /**
     * 接收在线消息
     * 还有接收离线消息的事件(离线： 我不在线，别人给我发的消息)
     * 如果有需要请查看文档
     * https://docs.jiguang.cn//jmessage/client/android_sdk/message/#_30
     *
     * @param event 注意： 这个方法名字不要写错了
     *              onEventMainThread： 别人给我发送事件，我这边接收事件是在主线程中运行
     */
    public void onEventMainThread(MessageEvent event) {
        //获取消息
        Message data = event.getMessage();

        //data.getContentType(): 消息类型： 比如图片消息 视频消息 文本消息等
        LogUtil.d(TAG, "onEventMainThread:" + data.getContentType() + "," + MessageUtil.getContent(data.getContent()));

        //发布消息事件
        EventBus.getDefault().post(new OnNewMessageEvent(data));
    }

    /**
     * 聊天消息通知栏点击
     *
     * @param event NotificationClickEvent 这个事件是极光里面的
     */
    public void onEventMainThread(NotificationClickEvent event) {
        //获取消息发送人id (getUserName: 因为我们传递的是id)
        //注意：这个id是String类型的
        String id = event.getMessage().getFromUser().getUserName();
        //创建意图
        Intent intent = new Intent(this, MainActivity.class);
        //添加用户id
        intent.putExtra(Constant.ID, id);
        //要跳转到聊天界面
        //先启动主界面的
        //好处是
        //用户在聊天界面
        //返回正好看到的主界面
        //这样才符合应用逻辑
        intent.setAction(Constant.ACTION_MESSAGE);
        //在Activity以外启动界面
        //都要写这个标识
        //具体的还比较复杂
        //基础课程中讲解
        //这里学会这样用就行了
//        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);好像用set也行
        //这里用addFlags
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        //启动界面
        startActivity(intent);
    }

    /**
     * 远程推送退出事件
     */
    public void onRemoteLogout() {
        LogUtil.d(TAG, "onRemoteLogout");
        //为什么是尝试? 因为这个界面可能被销毁(如果是销毁，那么我们直接销毁数据就行了，不需要显示对话框)
        tryShowLogoutDialog();

    }

    /**
     * 尝试显示被退出的对话框
     */
    private void tryShowLogoutDialog() {
        Activity activity = currentActivity.get();
        if (activity == null) {
            //没有界面了

            //静默退出(也就是不弹出对话框，直接清除登录数据。退出后不会跳转到登录注册界面)
            logoutSilence();
        }
        //显示强制退出对话框
        showLogoutDialog(activity);
    }

    /**
     * 显示强制退出对话框
     * 类似QQ和微信效果
     *
     * @param activity 注意： 可以理解为 这个弹出下线对话框接受到下线消息会随着activity的显示而显示(activity显示，弹出框就显示)
     *                 虽说这个方法会执行，但是activity没有显示出来，弹出框也没有显示出来
     */
    private void showLogoutDialog(Activity activity) {
        //当该界面后台后
        //该方法依然会执行
        //但弹窗不会显示
        //用户在切换到前台后
        //会继续显示
        //这符合我们需求
        //所以不用更改
        AlertDialog.Builder builder = new AlertDialog.Builder(activity);

        //点击外部不能关闭
        //只能点击确定关闭
        //因为确定里面有退出逻辑
        builder.setCancelable(false);
        //设置确定按钮
        builder.setPositiveButton(R.string.confirm, (dialog, which) -> {
            //TODO 执行退出并跳转到登录界面逻辑
            LogUtil.d(TAG, "showLogoutDialog confirm");
            logout();
        });

        //设置标题
        builder.setTitle(R.string.hint_logout);
        //显示
        builder.show();
    }


    /**
     * 静默退出
     */
    private void logoutSilence() {
        //清楚登录相关信息
        //这里在PreferenceUtil偏好设置里面又定义了一个方法（在这里面清除）
        PreferenceUtil.getInstance(getApplicationContext()).logout();

        //QQ退出
        otherLogout(QQ.NAME);

        //微博退出
        otherLogout(SinaWeibo.NAME);

        //退出了通知了（可能需要做些其他处理）
        onLogout();
    }

}
