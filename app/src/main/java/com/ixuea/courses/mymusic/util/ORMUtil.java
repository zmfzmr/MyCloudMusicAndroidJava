package com.ixuea.courses.mymusic.util;

import android.content.Context;

import com.ixuea.courses.mymusic.domain.Song;

import io.realm.Realm;
import io.realm.RealmConfiguration;

/**
 * 数据库工具类
 */
public class ORMUtil {

    private static ORMUtil instance;
    private final Context context;
    private final PreferenceUtil sp;

    /**
     * 构造方法
     */
    private ORMUtil(Context context) {
        this.context = context.getApplicationContext();

        //初始化偏好设置
        sp = PreferenceUtil.getInstance(this.context);
    }

    /**
     * 获取数据工具类单例
     */
    public static ORMUtil getInstance(Context context) {
        if (instance == null) {
            instance = new ORMUtil(context);
        }

        return instance;
    }

    public void saveSong(Song data) {
        //获取数据库对象
        Realm realm = getInstance();

        //记得关闭数据库
        realm.close();
    }

    /**
     * 获取数据库对象
     * 不同的用户 调用这个方法，就返回不同的数据库文件（返回不同的数据库对象）
     */
    private Realm getInstance() {
        //数据库配置
        RealmConfiguration configuration = new RealmConfiguration.Builder()
                //数据库名称
                //不同的用户使用不同的数据库文件
                //从而使用多用户
                //但让还可以在数据库中保存用户Id

                //比如用户id是153，数据库文件是153.ream
                .name(String.format("%s.realm", sp.getUserId()))
                .build();

        //返回数据库对象 (传入数据库配置)
        return Realm.getInstance(configuration);
    }

    /**
     * 销毁数据库对象
     */
    public static void destroy() {
        instance = null;
    }
}
