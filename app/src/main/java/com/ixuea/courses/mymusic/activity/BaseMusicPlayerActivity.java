package com.ixuea.courses.mymusic.activity;

import android.media.MediaPlayer;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.ixuea.courses.mymusic.R;
import com.ixuea.courses.mymusic.domain.Song;
import com.ixuea.courses.mymusic.fragment.PlayListDialogFragment;
import com.ixuea.courses.mymusic.listener.MusicPlayerListener;
import com.ixuea.courses.mymusic.manager.ListManager;
import com.ixuea.courses.mymusic.manager.MusicPlayerManager;
import com.ixuea.courses.mymusic.service.MusicPlayerService;
import com.ixuea.courses.mymusic.util.ImageUtil;
import com.ixuea.courses.mymusic.util.LogUtil;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * 迷你播放列表控制器
 */
public class BaseMusicPlayerActivity extends BaseTitleActivity implements MusicPlayerListener {
    private static final String TAG = "BaseMusicPlayerActivity";
    /**
     * 迷你播放控制器
     */
    @BindView(R.id.ll_play_control_small)
    LinearLayout ll_play_control_small;
    /**
     * 迷你播放控制器 封面
     */
    @BindView(R.id.iv_banner_small_control)
    ImageView iv_banner_small_control;
    /**
     * 迷你播放控制器 标题
     */
    @BindView(R.id.tv_title_small_control)
    TextView tv_title_small_control;
    /**
     * 迷你播放控制器 播放暂停按钮
     */
    @BindView(R.id.iv_play_small_control)
    ImageView iv_play_small_control;

    /**
     * 迷你播放控制器 进度条
     */
    @BindView(R.id.pb_progress_small_control)
    ProgressBar pb_progress_small_control;
    //注意：这里的下一曲按钮 和播放列表按钮，只需要监听点击事件，而把不需要更改内容，所以不需要写

    /**
     * 这里用protected 子类就可以访问了
     */
    protected ListManager listManager;//初始化列表管理器
    protected MusicPlayerManager musicPlayerManager;

    @Override
    protected void initDatum() {
        super.initDatum();
        //初始化列表管理器
        listManager = MusicPlayerService.getListManager(getApplicationContext());

        //初始化音乐播放器
        musicPlayerManager = MusicPlayerService.getMusicPlayerManager(getApplicationContext());
    }

    /**
     * 界面显示了回调
     */
    @Override
    protected void onResume() {
        super.onResume();
        //添加播放管理器监听器
        musicPlayerManager.addMusicPlayerListener(this);

        //显示迷你播放控制器数据
        showSmallPlayControlData();
    }

    /**
     * 界面隐藏了
     */
    @Override
    protected void onPause() {
        super.onPause();
        //移除播放管理器监听器
        musicPlayerManager.removeMusicPlayerListener(this);
    }

    /**
     * 显示迷你播放控制器数据
     */
    private void showSmallPlayControlData() {
        if (listManager.getDatum() != null && listManager.getDatum().size() > 0) {
            //有音乐

            //显示迷你控制器
            ll_play_control_small.setVisibility(View.VISIBLE);

            //获取当前播放的音乐
            Song data = listManager.getData();
            if (data != null) {
                //显示初始化数据
                showInitData(data);
                //显示时长
                showDuration(data);
                //显示播放进度
                showProgress(data);
                //显示播放状态
                showMusicPlayStatus();
            }
        } else {
            //隐藏迷你播放控制器
            ll_play_control_small.setVisibility(View.GONE);
        }
    }

    //显示播放状态
    private void showMusicPlayStatus() {
        //注意：这里要判断，有可能播放的时候点击暂停了，所以需要判断播放状态
        if (musicPlayerManager.isPlaying()) {//是已经播放
            showPauseStatus();
        } else {
            showPlayStatus();
        }
    }

    /**
     * 显示播放状态
     */
    private void showPlayStatus() {
        //这种图片切换可以使用Selector来实现
        iv_play_small_control.setSelected(false);
    }

    /**
     * 显示暂停状态
     */
    private void showPauseStatus() {//播放的，显示暂停状态
        iv_play_small_control.setSelected(true);
    }

    /**
     * 显示播放进度
     */
    private void showProgress(Song data) {
        //设置进度条（这个进度，在MusicPlayerManagerImpl中play中定时器任务回到主线程中
        // 用data.setProgress(player.getCurrentPosition());）保存到data里面，所以可以直接获取进度
        pb_progress_small_control.setProgress((int) data.getProgress());
    }

    /**
     * 显示音乐时长
     *
     * @param data
     */
    private void showDuration(Song data) {
        //获取当前音乐时长
        int end = (int) data.getDuration();

        //设置到进度条(最大时长)
        pb_progress_small_control.setMax(end);
    }

    /**
     * 显示初始化数据
     *
     * @param data Song
     */
    private void showInitData(Song data) {
        //显示封面
        ImageUtil.show(getMainActivity(), iv_banner_small_control, data.getBanner());
        //显示标题
        tv_title_small_control.setText(data.getTitle());
    }

    //监听MusicPlayManager中方法的调用情况
    @Override
    public void onPaused(Song data) {
        //监听到暂停，就显示播放状态
        showPlayStatus();
    }

    @Override
    public void onPlaying(Song data) {
        showPauseStatus();//播放中，显示暂停状态（就是图标为暂停）
    }

    @Override
    public void onPrepared(MediaPlayer mp, Song data) {
        showInitData(data);

        //不知道当前界面是否有列表，所以选中逻辑放在子类实现
//        //选中当前播放的音乐
//        scrollPositionAsync();
    }

    @Override
    public void onProgress(Song data) {
        showProgress(data);
    }
    //end 监听MusicPlayManager中方法的调用情况

    /**
     * 迷你播放控制器 容器点击
     */
    @OnClick(R.id.ll_play_control_small)
    public void onPlayControlSmallClick() {
        LogUtil.d(TAG, "onPlayControlSmallClick");

        //简单播放器界面
        SimplePlayerActivity.start(getMainActivity());
    }

    /**
     * 迷你播放控制器 播放（暂停）点击
     */
    @OnClick(R.id.iv_play_small_control)
    public void onPlaySmallClick() {
        LogUtil.d(TAG, "onPlaySmallClick");

        if (musicPlayerManager.isPlaying()) {
            musicPlayerManager.pause();
        } else {
            musicPlayerManager.resume();
        }
    }

    /**
     * 迷你播放控制器 下一曲点击
     */
    @OnClick(R.id.iv_next_small_control)
    public void onNextSmallClick() {
        LogUtil.d(TAG, "onNextSmallClick");
        //获取下一首Song对象(获取下一首音乐)
        Song data = listManager.next();
        //播放
        listManager.play(data);
    }

    /**
     * 迷你播放控制器 播放列表
     */
    @OnClick(R.id.iv_list_small_control)
    public void onListSmallClick() {
        LogUtil.d(TAG, "onListSmallClick");
        showPlayListDialog();
    }

    /**
     * 显示播放列表对话框
     */
    private void showPlayListDialog() {
        //
        PlayListDialogFragment.show(getSupportFragmentManager());
    }
}
