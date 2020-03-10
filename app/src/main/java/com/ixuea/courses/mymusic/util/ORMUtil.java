package com.ixuea.courses.mymusic.util;

import android.content.Context;

import com.ixuea.courses.mymusic.domain.Song;
import com.ixuea.courses.mymusic.domain.SongLocal;

import java.util.List;

import io.realm.Realm;
import io.realm.RealmConfiguration;

/**
 * 数据库工具类
 */
public class ORMUtil {

    private static final String TAG = "ORMUtil";
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

        //将Song转为SongLocal对象
        SongLocal songLocal = data.toSongLocal();

        //获取数据库对象
        Realm realm = getInstance();

        realm.beginTransaction();//开启事务

        //新增或更新(没有数据库或表就新增，有就更新)，传入保存的对象
        realm.copyToRealmOrUpdate(songLocal);

        realm.commitTransaction();//提交事务

        //记得关闭数据库
        realm.close();

        LogUtil.d(TAG, "saveSong:" + songLocal.getTitle());
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

    /**
     * 保存所有音乐
     */
    public void saveAll(List<Song> datum) {
        //获取数据库对象 getInstance():这个是本类对象的（获取多用户的那个方法）
        Realm realm = getInstance();

        //开启事务
        realm.beginTransaction();

        SongLocal songLocal = null;//这个放在外面，效率高点，不用每次遍历都创建一个对象啦
        for (Song data : datum) {
            //将Song转为SongLocal对象
            songLocal = data.toSongLocal();

            //新增或者更新(根据主键来新增或者更新)
            realm.copyToRealmOrUpdate(songLocal);
        }

        //提交事务
        realm.commitTransaction();

        //关闭数据库
        realm.close();
    }
}
