package com.ixuea.courses.mymusic.activity;

import android.app.Activity;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.SeekBar;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.ixuea.courses.mymusic.R;
import com.ixuea.courses.mymusic.adapter.SimplePlayerAdapter;
import com.ixuea.courses.mymusic.domain.Song;
import com.ixuea.courses.mymusic.listener.MusicPlayerListener;
import com.ixuea.courses.mymusic.manager.ListManager;
import com.ixuea.courses.mymusic.manager.MusicPlayerManager;
import com.ixuea.courses.mymusic.service.MusicPlayerService;
import com.ixuea.courses.mymusic.util.LogUtil;
import com.ixuea.courses.mymusic.util.TimeUtil;
import com.ixuea.courses.mymusic.util.ToastUtil;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import butterknife.BindView;
import butterknife.OnClick;

import static com.ixuea.courses.mymusic.util.Constant.MODEL_LOOP_LIST;
import static com.ixuea.courses.mymusic.util.Constant.MODEL_LOOP_ONE;
import static com.ixuea.courses.mymusic.util.Constant.MODEL_LOOP_RANDOM;

/**
 * 简单的播放器实现
 * 只要测试音乐播放相关逻辑
 * 因为黑胶唱片界面的逻辑比较复杂
 * 如果在和播放相关逻辑混在一起，不好实现
 * 所以我们先使用一个简单的播放器
 * 从而把播放器相关逻辑实现完成
 * 然后在对接的黑胶唱片，就相对来说简单一些
 */
public class SimplePlayerActivity extends BaseTitleActivity implements SeekBar.OnSeekBarChangeListener, MusicPlayerListener {

    private static final String TAG = "SimplePlayerActivity";
    /**
     * 列表
     */
    @BindView(R.id.rv)
    RecyclerView rv;

    /**
     * 标题
     */
    @BindView(R.id.tv_title)
    TextView tv_title;

    /**
     * 开始时间
     */
    @BindView(R.id.tv_start)
    TextView tv_start;

    /**
     * 进度条
     */
    @BindView(R.id.sb_progress)
    SeekBar sb_progress;

    /**
     * 结束时间（总时长）
     */
    @BindView(R.id.tv_end)
    TextView tv_end;

    /**
     * 播放按钮
     */
    @BindView(R.id.bt_play)
    Button bt_play;

    /**
     * 循环模式
     */
    @BindView(R.id.bt_loop_model)
    Button bt_loop_model;
    private MusicPlayerManager musicPlayerManager;//播放管理器
    private ListManager listManager;//列表管理器
    private SimplePlayerAdapter adapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_simple_player);
    }

    @Override
    protected void initView() {
        super.initView();

        //高度固定
        rv.setHasFixedSize(true);
        //布局管理器
        LinearLayoutManager layoutManager = new LinearLayoutManager(getMainActivity());
        rv.setLayoutManager(layoutManager);
    }

    @Override
    protected void initDatum() {
        super.initDatum();
        //测试单例模式
        //可以发现他们两次的内存地址都是一样
        //说明单例模式生效了
//        MusicPlayerManagerImpl o1 = MusicPlayerManagerImpl.getInstance(getMainActivity());
//        MusicPlayerManagerImpl o2 = MusicPlayerManagerImpl.getInstance(getMainActivity());
//
//        LogUtil.d(TAG, "initDatum test single:" + (o1 == o2));

        //获取MusicPlayerService播放管理器
        //(思路：MusicPlayerService-->getMusicPlayerManager获取播放管理器（里面先开启service，然后获取播放管理器）)
//        MusicPlayerManager o1 = MusicPlayerService.getMusicPlayerManager(getMainActivity());
//        MusicPlayerManager o2 = MusicPlayerService.getMusicPlayerManager(getMainActivity());
//
//        LogUtil.d(TAG, "initDatum test single:" + (o1 == o2));

        //初始化列表管理器
        listManager = MusicPlayerService.getListManager(getApplicationContext());

        //获取播放管理器
        musicPlayerManager = MusicPlayerService.getMusicPlayerManager(getApplicationContext());

//        //测试播放音乐
//        //由于现在没有获取数据
//        //所以创建一个测试数据
//        String songUrl = "http://dev-courses-misuc.ixuea.com/assets/s1.mp3";
//
//        Song song = new Song();
//        song.setUri(songUrl);
//
//        //播放音乐
//        musicPlayerManager.play(songUrl, song);

        //创建适配器
        adapter = new SimplePlayerAdapter(android.R.layout.simple_list_item_1);

        //设置到控件
        rv.setAdapter(adapter);

        //设置到适配器
        adapter.replaceData(listManager.getDatum());

    }

    @Override
    protected void initListeners() {
        super.initListeners();
        //设置脱宅进度控件监听器
        sb_progress.setOnSeekBarChangeListener(this);

        //设置item点击事件
        adapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                //获取这一首音乐(根据点击的位置获取这首音乐)
                Song data = listManager.getDatum().get(position);
                //播放音乐
                listManager.play(data);
            }
        });
    }
    /**
     * 进入了前台(界面可见了)
     */
    @Override
    protected void onResume() {
        super.onResume();
        LogUtil.d(TAG, "onResume");

        //设置播放监听器
        musicPlayerManager.addMusicPlayerListener(this);

        //显示初始化数据
        showInitData();

        //显示音乐时长(这个因为是可能点击：别的地方点击下一曲了，播放时长变了，再次回到当前界面的时候，刷新播放总时长)
        showDuration();

        //显示播放进度
        // (因为在后台监听器listener被移除了（listener主要是回调进度的（传进度到activity的）），)
        //如果没有监听器器，那么在MusicPlayerManagerImpl中startPublishProgress run方法中中断方法（并停止了定时器）
        //所以listener无法传递数据到activity这边，
        //所以这里需要重新刷新显示

        //不过有一点需要注意：MusicPlayerManagerImpl 持有 App应用的Context，虽然切换到后台，可是音乐还是会播放的
        //所以再次回到界面的时候需要更新下播放进度
        showProgress();

        //显示音乐播放状态(这个主要是：在悬浮通知那里点击了播放，然后回到Activity中，然后刷新播放状态)
        showMusicPlayStatus();
    }

    /**
     * 代理(监听器)可以在进入界面后就设置，但最好在界面可见前设置，在界面不可见时移除代理，
     * 好处是，如果界面后台后，就不需要显示相关的状态的，从而节省资源。
     */
    @Override
    protected void onPause() {
        super.onPause();
        LogUtil.d(TAG, "onPaused");

        //取消播放监听器
        musicPlayerManager.removeMusicPlayerListener(this);
    }


    /**
     * 播放点击
     */
    @OnClick(R.id.bt_play)
    public void onPlayClick() {
        LogUtil.d(TAG, "onPlayClick");

//        //测试通知渠道
//        //该通知没有任何实际意义
//
//        //获取通知
//        Notification notification = NotificationUtil.getServiceForeground(getApplicationContext());
//
//        //显示通知
//        //Id没什么实际意义
//        //只是相同Id的通知会被替换
//        NotificationUtil.showNotification(100, notification);

        playOrPause();
    }

    /**
     * 播放或暂停
     * 这里用listManager（实际里面用musicPlayerManager播放的）
     */
    private void playOrPause() {
        if (musicPlayerManager.isPlaying()) {
//            musicPlayerManager.pause();
            listManager.pause();
        } else {
//            musicPlayerManager.resume();
            listManager.resume();
        }
    }

    /**
     * 上一曲
     */
    @OnClick(R.id.bt_previous)
    public void onPreviousClick() {
        LogUtil.d(TAG, "onPreviousClick");
        //listManager.previous()：获取的是上一曲的Song对象
        listManager.play(listManager.previous());
    }

    /**
     * 下一曲
     */
    @OnClick(R.id.bt_next)
    public void onNextClick() {
        LogUtil.d(TAG, "onNextClick");

        //获取下一首音乐
        Song data = listManager.next();
        if (data != null) {
            listManager.play(data);
        } else {
            //正常情况下不能能走到这里
            //因为播放界面只有播放列表有数据时才能进入
            ToastUtil.errorShortToast(R.string.not_play_music);
        }
    }

    /**
     * 循环模式
     */
    @OnClick(R.id.bt_loop_model)
    public void onLoopModelClick() {
        LogUtil.d(TAG, "onLoopModelClick");
        //这里只需要改变循环模式就行，改变循环模式后，用户点击上一曲，或者下一曲，
        // 就会在ListManagerImpl根据模式选择不同的播放
        listManager.changeLoopModel();

        //显示循环模式
        showLoopModel();
    }

    private void showLoopModel() {
        //获取当前循环模式
        int model = listManager.getLoopModel();
        switch (model) {
            case MODEL_LOOP_LIST:
                bt_loop_model.setText("列表模式");
                break;
            case MODEL_LOOP_RANDOM:
                bt_loop_model.setText("随机模式");
                break;
            case MODEL_LOOP_ONE:
                bt_loop_model.setText("单曲循环");
                break;
            default:
                break;
        }
    }


    /**
     * 启动界面方法
     *
     * @param activity 宿主activity
     */
    public static void start(Activity activity) {
        //创建意图
        //意图：就是要干什么
        Intent intent = new Intent(activity, SimplePlayerActivity.class);
        //启动界面
        activity.startActivity(intent);
    }

    /**
     * 进度条改变了
     *
     * @param seekBar  SeekBar
     * @param progress 当前改变后的进度
     * @param fromUser 是否是用户触发的 true：表示用户触发的
     */
    @Override
    public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
        LogUtil.d(TAG, "onProgressChanged: " + progress + ", " + fromUser);
        if (fromUser) {//注意：这里需要判断是否拖拽了
            //跳转到该位置播放
            musicPlayerManager.seekTo(progress);
        }
    }

    /**
     * 开始拖拽进度条
     *
     * @param seekBar SeekBar
     */
    @Override
    public void onStartTrackingTouch(SeekBar seekBar) {
        LogUtil.d(TAG, "onStartTrackingTouch");
    }

    /**
     * 停止拖拽进度条
     *
     * @param seekBar SeekBar
     */
    @Override
    public void onStopTrackingTouch(SeekBar seekBar) {
        LogUtil.d(TAG, "onStopTrackingTouch");
    }

    /**
     * 显示播放状态
     */
    private void showPlayStatus() {
        bt_play.setText("播放");
    }

    /**
     * 显示暂停状态
     */
    private void showPauseStatus() {
        bt_play.setText("暂停");
    }

    /**
     * 显示音乐播放状态
     */

    private void showMusicPlayStatus() {
        if (musicPlayerManager.isPlaying()) {//是播放的话，按钮设置为暂停
            showPauseStatus();
        } else {
            showPlayStatus();//设置为播放
        }
    }

    /**
     * 显示时长
     */
    private void showDuration() {
        //获取正在播放音乐的时长
        long end = musicPlayerManager.getData().getDuration();

        //将格式化为分钟:秒
        //这里转换成了分钟秒
        tv_end.setText(TimeUtil.formatMinuteSecond((int) end));
        //设置到进度条
        sb_progress.setMax((int) end);
    }

    /**
     * 显示播放进度
     */
    private void showProgress() {
        //获取播放进度
        long progress = musicPlayerManager.getData().getProgress();
        //格式化进度
        tv_start.setText(TimeUtil.formatMinuteSecond((int) progress));
        //设置到进度条
        sb_progress.setProgress((int) progress);
    }

    /**
     * 显示初始化数据
     */
    private void showInitData() {
        //获取当前播放的音乐
        Song data = listManager.getData();

        //显示标题
        tv_title.setText(data.getTitle());
    }

    //播放管理器监听器
    ////其实这个方法，已经在MusicPlayerManagerImpl中play pause resume 使用了（如：listener.onPaused(data)）
    @Override
    public void onPaused(Song data) {
        LogUtil.d(TAG, "onPaused");
        //播放管理器监听器
        showPlayStatus();
    }

    @Override
    public void onPlaying(Song data) {
        LogUtil.d(TAG, "onPlaying");

        showPauseStatus();
    }

    @Override
    public void onPrepared(MediaPlayer mp, Song data) {
        LogUtil.d(TAG, "onPrepared: " + data.getProgress() + "," + data.getDuration());
        //显示初始化数据
        showInitData();

        //显示时长
        showDuration();
    }

    @Override
    public void onProgress(Song data) {
        LogUtil.d(TAG, "onProgress:" + data.getProgress() + "," + data.getDuration());
        showProgress();
    }

//    @Override
//    public void onCompletion(MediaPlayer mp) {
//        LogUtil.d(TAG, "onCompletion");
//    }

    //end播放管理器监听器
}
