package com.ixuea.courses.mymusic;

import android.app.Application;
import android.content.Context;
import android.content.Intent;

import com.facebook.stetho.Stetho;
import com.ixuea.courses.mymusic.activity.LoginOrRegisterActivity;
import com.ixuea.courses.mymusic.domain.Session;
import com.ixuea.courses.mymusic.domain.event.LoginSuccessEvent;
import com.ixuea.courses.mymusic.util.PreferenceUtil;
import com.ixuea.courses.mymusic.util.ToastUtil;
import com.mob.MobSDK;

import org.greenrobot.eventbus.EventBus;

import androidx.multidex.MultiDex;
import cn.sharesdk.framework.Platform;
import cn.sharesdk.framework.ShareSDK;
import cn.sharesdk.sina.weibo.SinaWeibo;
import cn.sharesdk.tencent.qq.QQ;
import es.dmoral.toasty.Toasty;

/**
 * 全局Application
 */
public class AppContext extends Application {
//    public class AppContext extends MultiDexApplication {

    /**
     * 上下文
     */
    private static AppContext context;

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

        context = this;
        //初始化toast工具类
        Toasty.Config.getInstance().apply();
        //初始化Toast工具类
        ToastUtil.init(getApplicationContext());

        //初始化Stetho抓包
        //使用默认参数初始化
        Stetho.initializeWithDefaults(this);
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
     * @param sp   PreferenceUtil
     * @param data Session
     *             保存到PreferenceUtil中SharedPreferences对象里面
     */
    public void login(PreferenceUtil sp, Session data) {
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

    }

    /**
     * 退出
     * 清除信息后，跳转到 登录注册界面
     */
    public void logout() {
        //清楚登录相关信息
        //这里在PreferenceUtil偏好设置里面又定义了一个方法（在这里面清除）
        PreferenceUtil.getInstance(getApplicationContext()).logout();

        //QQ退出
        otherLogout(QQ.NAME);

        //微博退出
        otherLogout(SinaWeibo.NAME);

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
        //退出了通知了（可能需要做些其他处理）
        onLogout();
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
     */
    private void onLogout() {

    }
}
