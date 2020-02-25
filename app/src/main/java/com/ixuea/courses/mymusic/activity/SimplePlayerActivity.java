package com.ixuea.courses.mymusic.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.SeekBar;
import android.widget.TextView;

import com.ixuea.courses.mymusic.R;
import com.ixuea.courses.mymusic.manager.MusicPlayerManager;
import com.ixuea.courses.mymusic.service.MusicPlayerService;
import com.ixuea.courses.mymusic.util.LogUtil;

import androidx.recyclerview.widget.RecyclerView;
import butterknife.BindView;
import butterknife.OnClick;

/**
 * 简单的播放器实现
 * 只要测试音乐播放相关逻辑
 * 因为黑胶唱片界面的逻辑比较复杂
 * 如果在和播放相关逻辑混在一起，不好实现
 * 所以我们先使用一个简单的播放器
 * 从而把播放器相关逻辑实现完成
 * 然后在对接的黑胶唱片，就相对来说简单一些
 */
public class SimplePlayerActivity extends BaseTitleActivity implements SeekBar.OnSeekBarChangeListener {

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


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_simple_player);
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
        MusicPlayerManager o1 = MusicPlayerService.getMusicPlayerManager(getMainActivity());
        MusicPlayerManager o2 = MusicPlayerService.getMusicPlayerManager(getMainActivity());

        LogUtil.d(TAG, "initDatum test single:" + (o1 == o2));

    }

    @Override
    protected void initListeners() {
        super.initListeners();
        //设置脱宅进度控件监听器
        sb_progress.setOnSeekBarChangeListener(this);

    }

    /**
     * 上一曲
     */
    @OnClick(R.id.bt_previous)
    public void onPreviousClick() {
        LogUtil.d(TAG, "onPreviousClick");
    }

    /**
     * 播放点击
     */
    @OnClick(R.id.bt_play)
    public void onPlayClick() {
        LogUtil.d(TAG, "onPlayClick");
    }

    /**
     * 下一曲
     */
    @OnClick(R.id.bt_next)
    public void onNextClick() {
        LogUtil.d(TAG, "onNextClick");
    }

    /**
     * 循环模式
     */
    @OnClick(R.id.bt_loop_model)
    public void onLoopModelClick() {
        LogUtil.d(TAG, "onLoopModelClick");
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
}
