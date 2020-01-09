package com.ixuea.courses.mymusicold;

import android.app.Application;
import android.content.Context;

import com.facebook.stetho.Stetho;
import com.ixuea.courses.mymusicold.domain.Session;
import com.ixuea.courses.mymusicold.util.PreferenceUtil;
import com.ixuea.courses.mymusicold.util.ToastUtil;

import androidx.multidex.MultiDex;
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
    }

    /**
     * 初始化其他需要登录后初始化的内容
     */
    private void onLogin() {

    }
}
