package com.ixuea.courses.mymusicold.util;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * 偏好设置工具类
 * 是否登录了，是否显示引导界面,用户id
 */
public class PreferenceUtil {

    private static final String NAME = "ixuea_my_cloud_music";//持久化数据库名字
    private static final String SHOW_GUIDE = "SHOW_GUIDE";
    private static final String SESSION = "SESSION";//用户登录session key
    private static final String USER_ID = "USER_ID";//用户登录UserId key
    private static PreferenceUtil instance;
    private final Context context;//上下文
    private final SharedPreferences preference;

    /**
     * 注意：这里用的是this.context
     * @param context Context
     */
    public PreferenceUtil(Context context) {//构造方法
//        this.context = context;
        //这样写有内存泄漏
        //因为当前工具类不会马上释放
        //如果当前工具类引用了界面实例
        //当界面关闭后
        //因为界面对应在这里还有引用
        //所以会导致界面对象不会被释放
        //this.context = context;
        //所以用下面的这个
        this.context = context.getApplicationContext();//应用的上下文
        //获取偏好设置
        preference = this.context.getSharedPreferences(NAME, Context.MODE_PRIVATE);

        //
    }

    /**
     * 单例
     * 设置偏好设置单例
     * @param context Context
     * @return PreferenceUtil
     */
    public static PreferenceUtil getInstance(Context context) {
        if (instance == null) {
            instance = new PreferenceUtil(context);
        }
        return instance;
    }

    /**
     * 是否显示引导界面
     * @return true，显示引导界面；false，不显示
     */
    public boolean isShowGuide() {
        return preference.getBoolean(SHOW_GUIDE,true);//第一次默认为true，后面改为false即可
    }

    public void setShowGuide(boolean value) {
        putBoolean(SHOW_GUIDE, value);
    }

    /**
     * 保存登录session
     *
     * @param value session
     */
    public void setSession(String value) {//统一设置为value，方便管理
        putString(SESSION, value);
    }

    /**
     * 获取登录session
     *
     * @param value Value
     */
    public void getSession(String value) {//统一设置为value，方便管理
        preference.getString(SESSION, null);
    }

    /**
     * 设置用户Id
     *
     * @param value 用户id值
     */
    public void setUserId(String value) {
        putString(USER_ID, value);
    }

    /**
     * 获取登录获取id
     *
     * @return id
     */
    public String getUserId() {
        return preference.getString(USER_ID, null);
    }

    //辅助方法

    /**
     * 保存字符串
     *
     * @param key   Key
     * @param value Value
     */
    private void putString(String key, String value) {
        preference.edit().putString(key, value).commit();
    }

    /**
     * 保存boolean
     *
     * @param key
     * @param value
     */
    private void putBoolean(String key, boolean value) {
        preference.edit().putBoolean(key, value).commit();
    }

    //end辅助方法
}
