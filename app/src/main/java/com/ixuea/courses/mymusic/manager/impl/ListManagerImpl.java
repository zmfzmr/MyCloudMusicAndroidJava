package com.ixuea.courses.mymusic.manager.impl;

import android.content.Context;

import com.ixuea.courses.mymusic.domain.Song;
import com.ixuea.courses.mymusic.manager.ListManager;
import com.ixuea.courses.mymusic.manager.MusicPlayerManager;
import com.ixuea.courses.mymusic.service.MusicPlayerService;
import com.ixuea.courses.mymusic.util.LogUtil;
import com.ixuea.courses.mymusic.util.ResourceUtil;

import java.util.LinkedList;
import java.util.List;
import java.util.Random;

import static com.ixuea.courses.mymusic.util.Constant.MODEL_LOOP_LIST;
import static com.ixuea.courses.mymusic.util.Constant.MODEL_LOOP_ONE;
import static com.ixuea.courses.mymusic.util.Constant.MODEL_LOOP_RANDOM;

/**
 * 列表管理器默认实现
 */
public class ListManagerImpl implements ListManager {
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


    /**
     * 构造方法
     *
     * @param context Context
     */
    private ListManagerImpl(Context context) {
        this.context = context.getApplicationContext();
        //初始化音乐播放管理器
        musicPlayerManager = MusicPlayerService.getMusicPlayerManager(context);
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

        //清空原来的数据
        this.datum.clear();
        //添加新的数据
        this.datum.addAll(datum);
    }

    @Override
    public List<Song> getDatum() {
        LogUtil.d(TAG, "setDatum");
        return datum;
    }

    @Override
    public void play(Song data) {
        LogUtil.d(TAG, "play");
        //标志已经播放了
        isPlay = true;

        //保存数据
        this.data = data;
        //播放音乐
        //这里是从SheetDetailActivity中，通过play(position)传递过来的Song
        musicPlayerManager.play(ResourceUtil.resourceUri(data.getUri()), data);
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
}
