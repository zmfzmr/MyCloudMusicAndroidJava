package com.ixuea.courses.mymusicold;

import android.app.Application;
import android.content.Context;

import com.facebook.stetho.Stetho;
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
    private static Context context;

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
    public static Context getContext() {
        return context;
    }
}
