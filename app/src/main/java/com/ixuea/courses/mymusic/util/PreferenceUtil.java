package com.ixuea.courses.mymusic.util;

import android.content.Context;
import android.content.SharedPreferences;
import android.text.TextUtils;

/**
 * 偏好设置工具类
 * 是否登录了，是否显示引导界面,用户id
 */
public class PreferenceUtil {

    private static final String NAME = "ixuea_my_cloud_music";//持久化数据库名字
    private static final String SHOW_GUIDE = "SHOW_GUIDE";
    private static final String SESSION = "SESSION";//用户登录session key
    private static final String USER_ID = "USER_ID";//用户登录UserId key
    private static final String LAST_PLAY_SONG_ID = "LAST_PLAY_SONG_ID";//最后播放音乐id key
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
     */
    public String getSession() {//统一设置为value，方便管理
        return preference.getString(SESSION, null);
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

    /**
     * 退出
     * <p>
     * 退出方法不属于辅助方法，所以放到上面来
     */
    public void logout() {
        delete(USER_ID);
        delete(SESSION);
    }

    /**
     * 获取最后播放的音乐Id
     */
    public String getLastPlaySongId() {
        return preference.getString(LAST_PLAY_SONG_ID, null);
    }

    /**
     * 设置当前播放音乐的id
     * <p>
     * commit：同步的（调用方法后马上就保存）  apply：异步的（在合适的时间保存）
     */

    public void setLastPlaySongId(String data) {
//        putString(LAST_PLAY_SONG_ID,data);
        preference.edit().putString(LAST_PLAY_SONG_ID, data).apply();
    }


    //辅助方法 （就是添加 删除等之类的）

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
     * @param key Key
     * @param value Value
     */
    private void putBoolean(String key, boolean value) {
        preference.edit().putBoolean(key, value).commit();
    }

    /**
     * 是否登录了
     *
     * @return true：表示登录，false：表示没有登录
     */
    public boolean isLogin() {
        //TextUtils.isEmpty(getSession()):不为空就返回false；希望返回true，加个！
        return !TextUtils.isEmpty(getSession());
    }

    /**
     * 删除内容（根据key删除SharedPreferences里面的值）
     *
     * @param key Key
     */
    private void delete(String key) {
        preference.edit().remove(key).commit();
    }


    //end辅助方法
}
