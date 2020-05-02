package com.ixuea.courses.mymusic.manager.impl;

import android.content.Context;
import android.media.MediaPlayer;

import com.ixuea.courses.mymusic.domain.Song;
import com.ixuea.courses.mymusic.domain.event.OnPlayEvent;
import com.ixuea.courses.mymusic.listener.MusicPlayerListener;
import com.ixuea.courses.mymusic.manager.ListManager;
import com.ixuea.courses.mymusic.manager.MusicPlayerManager;
import com.ixuea.courses.mymusic.service.MusicPlayerService;
import com.ixuea.courses.mymusic.util.Constant;
import com.ixuea.courses.mymusic.util.DataUtil;
import com.ixuea.courses.mymusic.util.LogUtil;
import com.ixuea.courses.mymusic.util.ORMUtil;
import com.ixuea.courses.mymusic.util.PreferenceUtil;
import com.ixuea.courses.mymusic.util.ResourceUtil;

import org.apache.commons.lang3.StringUtils;
import org.greenrobot.eventbus.EventBus;

import java.util.LinkedList;
import java.util.List;
import java.util.Random;

import static com.ixuea.courses.mymusic.util.Constant.MODEL_LOOP_LIST;
import static com.ixuea.courses.mymusic.util.Constant.MODEL_LOOP_ONE;
import static com.ixuea.courses.mymusic.util.Constant.MODEL_LOOP_RANDOM;

/**
 * 列表管理器默认实现
 */
public class ListManagerImpl implements ListManager, MusicPlayerListener {
    private static final String TAG = "ListManagerImpl";
    private static ListManagerImpl instance;//实例对象
    private final Context context;//上下文
    /**
     * 列表
     * LinkedList:增删高效
     * ArrayList：遍历高效
     */
    private List<Song> datum = new LinkedList<>();
    //其实列表管理器中主要用到了播放管理器（就是让播放管理利器实现）
    private final MusicPlayerManager musicPlayerManager;//音乐播放管理器
    private Song data;//当前音乐对象
    private boolean isPlay;//是否播放了
    /**
     * 循环模式
     * 默认列表循环
     */
    private int model = MODEL_LOOP_LIST;
    private final ORMUtil orm;//数据库工具类对象
    private long lastTime;//最后保存播放进度时间
    private final PreferenceUtil sp;


    /**
     * 构造方法
     *
     * @param context Context
     */
    private ListManagerImpl(Context context) {
        this.context = context.getApplicationContext();
        //初始化音乐播放管理器
        musicPlayerManager = MusicPlayerService.getMusicPlayerManager(context);

        //添加音乐监听器(给musicPlayerManager设置监听器（就是让ListManagerImpl成为musicPlayerManager里面的监听器）)
        //主要是监听当前类的 方法调用（操作）情况
        //（也就是说：本类(this)对musicPlayerManager方法的调用情况监听）
        //外界对我(musicPlayerManager)里面的方法调用，我(musicPlayerManager)能监听器到；然后回调一个参数给外界
        musicPlayerManager.addMusicPlayerListener(this);

        //初始化数据库工具类
        orm = ORMUtil.getInstance(this.context);

        //初始化偏好设置工具类
        sp = PreferenceUtil.getInstance(this.context);

        //初始化播放列表
        initPlayList();

    }

    /**
     * 初始化播放列表（这里是从本地查询出来后，和持久化的最后播放的音乐对比，找到就赋值给this.data）
     */
    private void initPlayList() {
        //查询播放列表
        List<Song> datum = orm.queryPlayList();
        if (datum.size() > 0) {
            //添加到现在的播放列表
            this.datum.clear();//这个可以不用清空（因为刚参加对象，这个播放列表是没有值的）
            this.datum.addAll(datum);//这个一进来的时候，就把查询到的音乐添加到播放列表啦
            //获取最后播放音乐id
            // (还原回来当前播放列表里面的这个Song对象，从而在外界好方便展示)
            String id = sp.getLastPlaySongId();

            //获取出来记得判断
            if (StringUtils.isNotBlank(id)) {//这个id并不是null的
                //有最后音乐的id
                for (Song s : datum) {
                    if (s.getId().equals(id)) {
                        //找到了(查询出来的对象和持久化里面的Song id（最后播放音乐的id）匹配)

                        data = s;
                        break;//找到了记得跳出循环，这样执行会快点
                    }

                }

                if (data == null) {//因为没有找到，也就没有前面的 data = s;，所以data为null
                    //表示没找到
                    //可能各种原因（可能在数据库中把这首音乐给删除了，找不到这首音乐）
                    defaultPlaySong();//默认初始化第一首音乐
                } else {
                    //找到了，可以不用做任何处理（当然这个else可以不用写）
                }
            } else {
                //如果没有最后播放音乐
                //默认第一首
                defaultPlaySong();
            }

        }
    }

    /**
     * 设置默认播放音乐
     */
    private void defaultPlaySong() {
        data = datum.get(0);//获取第一首音乐
    }

    /**
     * 获取单例对象(获取列表管理器)
     *
     * @param context Context
     * @return ListManagerImpl
     */
    public static synchronized ListManagerImpl getInstance(Context context) {
        if (instance == null) {
            //只有没有初始化才创建对象
            instance = new ListManagerImpl(context);
        }
        return instance;
    }

    @Override
    public void setDatum(List<Song> datum) {
        LogUtil.d(TAG, "setDatum");

        //将原来数据playList标志设置为false(这里是 播放列表的每一首音乐Song都设置为了false)
        DataUtil.changePlayListFlag(this.datum, false);
        //保存下原来的数据（保存到数据库）
        saveAll();

        //清空原来的数据
        this.datum.clear();//注意：这里是全局的那个datum
        //添加新的数据
        this.datum.addAll(datum);

        //更改播放列表标志
        DataUtil.changePlayListFlag(this.datum, true);
        //保存到数据库
        saveAll();
    }

    @Override
    public List<Song> getDatum() {
        LogUtil.d(TAG, "setDatum");
        return datum;
    }

    @Override
    public void play(Song data) {
        LogUtil.d(TAG, "play");
        //播放音乐前先发送通知
        //目的是黑胶唱片接收到通知
        //先停止原来的黑胶唱片
        EventBus.getDefault().post(new OnPlayEvent());

        //标志已经播放了
        isPlay = true;

        //保存数据
        this.data = data;

        if (data.isLocal()) {
            //是不会本地的，这对象里面的source会等于SongLocal.LOCAL
            //data.isLocal() return source == SongLocal.SOURCE_LOCAL;

            //这个本地的uri从哪里设置进来的呢?()
            // 路线：ScanLocalMusicActivity-扫描设置uri到SongLocal并保存到本地数据库
            // -->LocalMusicActivity查找数据(转换成List<Song>)到适配器adapter--点击item传入data到ListManagerImpl中
            musicPlayerManager.play(data.getUri(), data);

        } else {
            //不是本地（从其他或者网络中来的额）

            //播放音乐
            //这里是从SheetDetailActivity中，通过play(position)传递过来的Song
            musicPlayerManager.play(ResourceUtil.resourceUri(data.getUri()), data);
        }

        //设置最后播放音乐的Id(保存歌曲的id 到持久化)
        sp.setLastPlaySongId(data.getId());


    }

    @Override
    public void pause() {
        LogUtil.d(TAG, "pause");
        musicPlayerManager.pause();
    }

    @Override
    public void resume() {
        LogUtil.d(TAG, "resume");
        if (isPlay) {
            //原来已经播放过
            //也就说播放器已经初始化了,已经播放过音乐了（播放要有准备等阶段，没有这些阶段，是不能直接调用player.start继续播放的）
            musicPlayerManager.resume();
        } else {
            //到这里，是应用开启后，第一次点继续播放
            //而这时内部其实还没有准备播放，所以应该调用播放
            play(data);

            //这里开始后台杀死进程，会调动else里面的
            if (data.getProgress() > 0) {
                //有播放进度（查询数据库中找到了这首播放的音乐并赋值给data）

                //就从上一次位置开始播放
                musicPlayerManager.seekTo((int) data.getProgress());

            }
        }
    }

    @Override
    public Song previous() {
        //音乐索引
        int index = 0;
        //判断循环模式
        switch (model) {
            case MODEL_LOOP_RANDOM:
                //随机循环

                //在0~datum.size()中
                //不包含datum.size()
                index = new Random().nextInt(datum.size());
                break;
            default:
                //找到当前音乐索引
                index = datum.indexOf(data);

                if (index != -1) {
                    //找到了

                    //如果当前播放是列表第一个
                    if (index == 0) {
                        //第一首音乐

                        //那就从最后开始播放
                        index = datum.size() - 1;
                    } else {
                        index--;
                    }

                } else {
                    //抛出异常
                    //因为正常情况下是能找到的
                    throw new IllegalArgumentException("Can't found current song");
                }
                break;
        }

        return datum.get(index);
    }

    @Override
    public Song next() {
        if (datum.size() == 0) {
            //如果没有音乐了
            //直接返回null
            return null;
        }

        //音乐索引
        int index = 0;//默认是0，0就是默认列表模式

        //判断循环模式
        switch (model) {
            case MODEL_LOOP_RANDOM:
                //随机循环

                //在0~datum.size()中
                //不包含datum.size()
                index = new Random().nextInt(datum.size());
                break;
            default:
                //找到当前音乐索引
                index = datum.indexOf(data);//找data在集合中的索引
                if (index != -1) {
                    //找到了

                    //如果当前播放是列表最后一个,index置为0
                    if (index == datum.size() - 1) {
                        index = 0;
                    } else {
                        index++;
                    }
                } else {
                    //抛出异常
                    //因为正常情况下是能找到的
                    throw new IllegalArgumentException("Can't found current song");
                }
                break;
        }

        return datum.get(index);//获取音乐（根据索引获取Song对象（音乐对象））
    }

    @Override
    public int changeLoopModel() {
        //循环模式+1
        model++;
        //判断循环模式边界
        if (model > MODEL_LOOP_RANDOM) {//MODEL_LOOP_RANDOM = 2
            //如果当前循环模式
            //大于最后一个循环模式
            //就设置为第0个循环模式
            model = MODEL_LOOP_LIST;//为0，列表模式
        }

        //判断是否是单曲循环
        if (model == MODEL_LOOP_ONE) {
            //单曲循环模式
            //设置到mediaPlay
            musicPlayerManager.setLooping(true);
        } else {
            //其他模式关闭循环
            musicPlayerManager.setLooping(false);
        }
        //返回最终的循环模式
        return model;
    }

    @Override
    public int getLoopModel() {
        return model;
    }

    @Override
    public Song getData() {
        return data;
    }

    /**
     * datum 播放列表中的集合list
     *
     * @param index 索引
     */
    @Override
    public void delete(int index) {
        //获取要删除的音乐
        Song song = datum.get(index);

        if (song.getId().equals(data.getId())) {
            //删除的音乐就是当前播放的音乐

            //应该停止当前播放
            pause();
            //获取下一首音乐
            Song next = next();
            //还剩一首音乐或者随机模式下，下一首等于播放的音乐
            //这里不做操作，直接data置为null
            if (next.getId().equals(data.getId())) {
                //找到了自己
                LogUtil.d(TAG, "delete before");

                //没有歌曲可以播放了
                data = null;

                LogUtil.d(TAG, "delete after");

                //TODO Bug 随机循环的情况下有可能获取到自己
            } else {
                play(next);
            }
        }

        //直接删除(不管怎么样,最终还是要删除的)
        //如果全部都删除之后，最终在简单播放界面onItemSwiped中判断，然后关闭界面
        datum.remove(song);

        //从数据库中删除（这里是从播放列表中删除）
        orm.deleteSong(song);//song：当前要删除的这首音乐
    }

    @Override
    public void deleteAll() {
        //如果在播放音乐就暂停
        if (musicPlayerManager.isPlaying()) {
            pause();
        }
        //清空列表
        datum.clear();

        //从数据库中删除
        orm.deleteAll();
    }

    @Override
    public void seekTo(int progress) {

        if (!musicPlayerManager.isPlaying()) {
            resume();//如果没有播放，就调用本类对象的resume（走else里面方法） 跳转到指定位置播放
        }

        //否则，滑动进度条的时候是在播放的，那就跳转到指定位置播放
        //从指定位置播放
        musicPlayerManager.seekTo(progress);

    }

    /**
     * 保存播放列表
     */
    private void saveAll() {
        orm.saveAll(datum);
    }

    //音乐播放管理器
    @Override
    public void onPaused(Song data) {

    }

    @Override
    public void onPlaying(Song data) {

    }

    @Override
    public void onPrepared(MediaPlayer mp, Song data) {

    }

    @Override
    public void onProgress(Song data) {
        //保存当前音乐播放进度
        //因为Android应用不太好监听应用被杀掉
        //所以这里就在这里保存

        //真实项目肯定需要对不同版本
        //不同手机进行适配
        //而不是采用下面的方法保存
        long currentTimeMillis = System.currentTimeMillis();
        if (currentTimeMillis - lastTime > Constant.SAVE_PROGRESS_TIME) {
            //保持数据（回调到这个onProgress方法的时候，其实Song里面已经保存了progress进度了）
            //那个duration总时长：在设置(音乐播放器监听器)中 onPrepared中就已经设置到Song对象里面
            orm.saveSong(data);
            lastTime = currentTimeMillis;
        }
    }

    /**
     * 播放完毕了回调
     *
     * @param mp
     */
    @Override
    public void onCompletion(MediaPlayer mp) {
        if (MODEL_LOOP_ONE == getLoopModel()) {
            //如果是单曲循环
            //就不会处理了
            //因为我们使用了MediaPlayer的循环模式

            //如果使用的第三方框架
            //如果没有循环模式
            //那就要在这里继续播放当前音乐
        } else {
            //其他模式

            //播放下一首音乐
            Song data = next();
            //可能当前这首音乐正在播放,但是我们把原来的音乐刚删除，这个回调还没有执行完成
            //不过这个只是小几率
            //(也就是说:正在播放的音乐，播放完成后，next：点击下一曲，但是这个音乐被删除了，获取的Song为null)
            //不过这个判不判断都无所谓，小概率事件，最好写上吧
            if (data != null) {
                play(data);//调用本类的播放方法
            }
        }
    }
    //end音乐播放管理器
}
