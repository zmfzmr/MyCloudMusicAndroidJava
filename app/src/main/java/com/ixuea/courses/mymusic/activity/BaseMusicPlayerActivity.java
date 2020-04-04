package com.ixuea.courses.mymusic.activity;

import android.media.MediaPlayer;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.ixuea.courses.mymusic.MusicPlayerActivity;
import com.ixuea.courses.mymusic.R;
import com.ixuea.courses.mymusic.domain.Song;
import com.ixuea.courses.mymusic.domain.event.PlayListChangeEvent;
import com.ixuea.courses.mymusic.domain.lyric.Line;
import com.ixuea.courses.mymusic.domain.lyric.Lyric;
import com.ixuea.courses.mymusic.domain.lyric.LyricUtil;
import com.ixuea.courses.mymusic.fragment.PlayListDialogFragment;
import com.ixuea.courses.mymusic.listener.MusicPlayerListener;
import com.ixuea.courses.mymusic.manager.ListManager;
import com.ixuea.courses.mymusic.manager.MusicPlayerManager;
import com.ixuea.courses.mymusic.service.MusicPlayerService;
import com.ixuea.courses.mymusic.util.ImageUtil;
import com.ixuea.courses.mymusic.util.LogUtil;
import com.ixuea.courses.mymusic.view.LyricLineView;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

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
     * 迷你播放控制器 歌词控件
     */
    @BindView(R.id.llv)
    LyricLineView llv;
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
    private Song data;

    @Override
    protected void initView() {
        super.initView();
        //这一行歌词始终是选中状态
        llv.setLineSelected(true);
    }

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

        //注册播放列表改变了监听
        //之所以在这里注册是因为
        //只是当前显示的界面监听就行了
        //其他界面再次显示的时候会执行onResume方法

        //注册了以后就可以接收EventBus发送的通知了
        EventBus.getDefault().register(this);

    }

    /**
     * 界面隐藏了
     */
    @Override
    protected void onPause() {
        super.onPause();
        //移除播放管理器监听器
        musicPlayerManager.removeMusicPlayerListener(this);

        //解除发布订阅框架注册
        EventBus.getDefault().unregister(this);
    }

    /**
     * 注意：这个发送通知的事件类PlayListChangeEvent要写对
     * 否则方法无法执行
     *
     * @param event
     */
    @Subscribe(threadMode = ThreadMode.MAIN)//这里是线程模式为主线程
    public void onPlayListDialogFragment(PlayListChangeEvent event) {
        //显示迷你播放控制器(里面会判断，如果删除音乐，播放列表没有数据后，就会隐藏迷你播放列表控制器)
        showSmallPlayControlData();
    }


    /**
     * 显示迷你播放控制器数据
     */
    private void showSmallPlayControlData() {

        //这个listManager.getDatum()有值的话，说明播放过音乐，并把Song添加到list集合里面
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

                //显示歌词
                showLyricData();
            }
        } else {
            //隐藏迷你播放控制器
            ll_play_control_small.setVisibility(View.GONE);
        }
    }

    /**
     * 显示歌词
     */
    private void showLyricData() {
        //获取当前播放的音乐
        Song data = listManager.getData();
        Lyric lyric = data.getParsedLyric();//获取当前音乐中解析的歌词
        if (lyric == null) {
            //清空原来的歌词(获取解析后的歌词为null，就在这个控件上 设置数据为null（就是没有歌词了）)
            llv.setData(null);
        } else {
            //因为要别的activity继承 本类，所以当点击音乐进入 MusicPlayerActivity 回到主页面，这时音乐已经播放了
            //不管哪个activity继承本类，必须要播放以后才能显示 迷你控制器（也就是listManager.getDatum().size() > 0）
            //播放音乐的时候，已经把列表List<Song>集合添加到listManager中的 this.datum 并保存到了数据库中
            //播放音乐：listManger.play -- > MusicPlayerManagerImpl.play
            // (把请求的歌词并解析后(这时是否精确 已经设置到Song对象里面的Lyric对象了)，添加到Song对象)
            //因为MusicPlayerManagerImpl中 用定时器 每16毫秒分发进度，所有在listManger onProgress中把回调回来的Song保存到了数据库中了

            //设置歌词模式(设置是否是精确的)
            llv.setAccurate(lyric.isAccurate());

            //获取当前播放进度对应的歌词行设置歌词数据到歌词控件
            Line line = LyricUtil.getLyricLine(lyric, data.getProgress());

            //设置数据
            llv.setData(line);
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

        //获取当前音乐歌词
        Lyric lyric = data.getParsedLyric();//data 当前播放的Song对象

        if (lyric == null) {
            //没有歌词
            return;
        }

        long progress = data.getProgress();
        //获取当前进度对应的歌词行
        Line line = LyricUtil.getLyricLine(lyric, progress);

        //设置数据(之前已经设置数据了，为什么还要再设置呢 因为之前设置的是第一行的歌词，那么过会唱到第二行的歌词，
        // 所以还是需要再设置一次(一直播放就会一直调用这个onProgress方法))

        //setData:里面调用了invalidate 来绘制控件（这个主要是针对LRC歌词，因为下面的llv.onProgress是针对精确到字的KSC歌词绘制的
        // 所以这里还是setData 还是调用invalidate一次 （并且是不等于当前行的时候调用） ）
        if (line != llv.getData()) {//llv.getData():获取的是Line对象
            llv.setData(line);//设置数据
        }
        //如果是精确到字歌曲
        //因为要计算唱到那个字了
        if (lyric.isAccurate()) {
            //获取当前时间是该行第几个字
            int lyricCurrentWordIndex = LyricUtil.getWordIndex(line, progress);
            //当前时间对应的字已经播放的时间
            float wordPlayedTime = LyricUtil.getWordPlayedTime(line, progress);
            //设置当前时间第几个
            llv.setLyricCurrentWordIndex(lyricCurrentWordIndex);
            //设置该字已经播放的时间
            llv.setWordPlayedTime(wordPlayedTime);
            //刷新控件 里面调用invalidate 方法
            llv.onProgress();
        }

    }

    @Override
    public void onLyricChanged(Song data) {
        //歌词改变了以后也要调用这个方法更新显示歌词
        showLyricData();
    }

    //end 监听MusicPlayManager中方法的调用情况

    /**
     * 迷你播放控制器 容器点击
     */
    @OnClick(R.id.ll_play_control_small)
    public void onPlayControlSmallClick() {
        LogUtil.d(TAG, "onPlayControlSmallClick");

        //简单播放器界面
        startMusicPlayerActivity();
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
            //这个直接调用resume没问题 （因为肯定是播放了音乐后，这个迷你控制器次显示出来,
            // 所以肯定是第一次播放音乐了，所以调用这个resume没啥问题）
//            musicPlayerManager.resume();

            //上面说的可能有问题，杀掉程序重新播放后，点击播放就有问题了，（因为要从当前进度开始播放，用上面代码就有问题）
            // 所以换成如下代码
            listManager.resume();
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

    /**
     * 进入音乐播放界面
     */
    public void startMusicPlayerActivity() {
//        //简单播放器界面
//        SimplePlayerActivity.start(getMainActivity());

        //黑胶唱片播放界面
        MusicPlayerActivity.start(getMainActivity());
    }
}
