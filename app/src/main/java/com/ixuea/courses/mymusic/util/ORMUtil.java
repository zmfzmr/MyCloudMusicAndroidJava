package com.ixuea.courses.mymusic.util;

import android.content.Context;

import com.ixuea.courses.mymusic.domain.Song;
import com.ixuea.courses.mymusic.domain.SongLocal;

import java.util.ArrayList;
import java.util.List;

import io.realm.Realm;
import io.realm.RealmConfiguration;
import io.realm.RealmResults;

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

            //新增或者更新(根据主键来新增或者更新) 注意：这里传入的是Song转换过的SongLocal对象
            realm.copyToRealmOrUpdate(songLocal);
        }

        //提交事务
        realm.commitTransaction();

        //关闭数据库
        realm.close();
    }

    /**
     * 从数据库中查询播放列表
     */
    public List<Song> queryPlayList() {
        //获取数据库对象
        Realm realm = getInstance();

        //查询播放列表(这里查询的是多个单个SongLocal集合（因为这里调用了findAll()）)
        RealmResults<SongLocal> songLocals = queryPlayListSongLocals(realm);//查询所有的，返回一个集合

//        //关闭数据库
//        realm.close();//查询完后这里不应该关闭

        //返回数据
        return toSongs(songLocals, realm);//在这里方法里面转换成List，需要在toSongs，遍历完成后关闭
    }

    /**
     * 查询本地播放列表音乐
     *
     * @param realm Realm
     */
    private RealmResults<SongLocal> queryPlayListSongLocals(Realm realm) {
        return realm.where(SongLocal.class)
                //查询的是SongLocal对象里面的成员变量playList，等于true，说明就是在播放列表中
                //其实就是查询成员变量playList为true的SongLocal对象
                .equalTo("playList", true)
                .findAll();
    }

    /**
     * 将本地对象转换成音乐对象
     */
    private List<Song> toSongs(RealmResults<SongLocal> songLocals, Realm realm) {
        //创建播放列表
        List<Song> songs = new ArrayList<>();

        //遍历每一个对象
        for (SongLocal songLocal : songLocals) {
            //转为song对象（集合里面添加（本地对象转换过的音乐对象））
            songs.add(songLocal.toSong());
        }

        //关闭数据库
        realm.close();//查询完后这里不应该关闭

        return songs;
    }

    /**
     * 删除音乐
     *
     * @param data 当前点中要删除的音乐（或从播放列表删除（实际数据库中还在））
     *             这里思路：找播放列表存到数据库中的音乐，标志位true和选中当前音乐，找到置为false
     *             这样下次进来的时候，就不会显示这这首音乐了
     */
    public void deleteSong(Song data) {
        //获取数据库对象
        Realm realm = getInstance();

        //没找到该框架通过Id删除数据
        //所有要先查询到要删除的数据
        //然后在删除
        SongLocal songLocal = realm.where(SongLocal.class)
                .equalTo("playList", true)
                .and()//和（并且）
                .equalTo("id", data.getId())
                .findFirst();//查找第一条（因为我们这类设置了主键id，所以不可能有重复，所以这里不写也没关系）
        //在事务删除(里面已经包含开始事务和提交事务)
//        realm.executeTransaction(new Realm.Transaction() {
//            @Override
//            public void execute(Realm realm) {
//                 //删除查询到的数据
//                songLocal.deleteFromRealm();
//            }
//        });

        //开启事务
        realm.beginTransaction();

        //设置播放列表标准

        //因为ListManagerImpl构造方法中查询播放列表是根据标志位true的来查询的
        //所以我们这里设置为false，这样查询的时候这首音乐查询不到，列表自然不显示
        songLocal.setPlayList(false);
        //提交事务
        realm.commitTransaction();
        //关闭数据库
        realm.close();
    }

    /**
     * 删除所有播放列表
     */
    public void deleteAll() {
        //获取数据库对象
        Realm realm = getInstance();
        //查询所有播放列表数据
        RealmResults<SongLocal> songLocals = queryPlayListSongLocals(realm);
        //开启事务
        realm.beginTransaction();
        //遍历音乐
        for (SongLocal songLocal : songLocals) {
            //更改播放列表标志
            //Realm查询回来更改值自动保存
            songLocal.setPlayList(false);
        }
        //提交事务
        realm.commitTransaction();
        //关闭数据库
        realm.close();
    }

    /**
     * 查询本地音乐(查询结果要转成List<Song>（就是每个对象SongLocal对象转换成Song对象返回）)
     * @param sortIndex
     */
    public List<Song> queryLocalMusic(int sortIndex) {
        //获取数据库实例(这个getInstance 方法里面已经实现了多用户了)
        Realm realm = getInstance();

        //查询所有本地音乐(注意：本地音乐对象是SongLocal对象)
        RealmResults<SongLocal> songLocals = realm.where(SongLocal.class)
                .equalTo("source", SongLocal.SOURCE_LOCAL)
                .findAll()
                //因为排序只有本地音乐对象才有，所以排序的key组放到SongLocal中
                //这里第二个参数是排序规则对象，这里不传默认是正序排序
                .sort(SongLocal.SORT_KEYS[sortIndex]);

        return toSongs(songLocals, realm);
    }

    /**
     * 保存本地音乐
     * 前面的那个saveAll 是保存网络请求的音乐(把请求回来的音乐转换成本地对象SongLocal，然后保存得到数据库到的)
     * <p>
     * 这里的，因为是本地对象了(SongLocal)，所以可以直接保存就行了
     *
     * @param data
     */
    public void saveSongLocal(SongLocal data) {
        //获取数据库框架实例
        Realm realm = getInstance();

        try {
            //开始事务
            //有些框架不用到这个事务的时候回帮我默认处理，但是对于我们这个realm框架，就必须要处理它它这个事务(也就是开始事务和提交事务)
            realm.beginTransaction();

            //会根据SongLocal对象里面的id来判断，如果有数据就更新；
            realm.copyToRealmOrUpdate(data);
            //提交事务
            realm.commitTransaction();

            LogUtil.d(TAG, "SongLocal:" + data.getTitle());

        } catch (Exception e) {
            //错误
            e.printStackTrace();

            //回滚事务(也即是取消事务 （前面的那个是提交事务）)
            realm.cancelTransaction();
        } finally {
            //释放realm
            realm.close();
        }
    }

    /**
     * 根据id删除本地音乐
     *
     * @param id
     */
    public void deleteSongLocalById(String id) {
        Realm realm = getInstance();
        //没找到该框架通过Id删除数据
        //所有要先查询到要删除的数据
        //然后在删除
        SongLocal songLocal = realm.where(SongLocal.class)
                .equalTo("id", id)
                //注意这里是findFirst，而不是findAll(),因为要找的是单个数据
                .findFirst();

        //在事务中删除(这里没有之前的开始事务和提交事务，一直一步搞定)
        realm.executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                //删除查询的数据
                songLocal.deleteFromRealm();
            }
        });
    }

    /**
     * 根据id查询音乐对象
     */
    public Song querySongById(String id) {
        //获取数据库实例
        Realm realm = getInstance();

        //查询
        SongLocal songLocal = realm.where(SongLocal.class)
                .equalTo("id", id)
                .findFirst();//根据id查找，肯定是只有一个
        //转换成Song对象
        return songLocal.toSong();
    }
}
