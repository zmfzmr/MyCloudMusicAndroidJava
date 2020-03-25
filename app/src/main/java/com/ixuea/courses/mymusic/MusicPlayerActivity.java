package com.ixuea.courses.mymusic;

import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.app.Activity;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.RequestBuilder;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.request.target.CustomTarget;
import com.bumptech.glide.request.transition.Transition;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.ixuea.courses.mymusic.activity.BaseTitleActivity;
import com.ixuea.courses.mymusic.adapter.LyricAdapter;
import com.ixuea.courses.mymusic.adapter.MusicPlayerAdapter;
import com.ixuea.courses.mymusic.domain.Song;
import com.ixuea.courses.mymusic.domain.event.OnPlayEvent;
import com.ixuea.courses.mymusic.domain.event.OnRecordClickEvent;
import com.ixuea.courses.mymusic.domain.event.OnStartRecordEvent;
import com.ixuea.courses.mymusic.domain.event.OnStopRecordEvent;
import com.ixuea.courses.mymusic.domain.event.PlayListChangeEvent;
import com.ixuea.courses.mymusic.fragment.PlayListDialogFragment;
import com.ixuea.courses.mymusic.listener.MusicPlayerListener;
import com.ixuea.courses.mymusic.manager.ListManager;
import com.ixuea.courses.mymusic.manager.MusicPlayerManager;
import com.ixuea.courses.mymusic.service.MusicPlayerService;
import com.ixuea.courses.mymusic.util.Constant;
import com.ixuea.courses.mymusic.util.DensityUtil;
import com.ixuea.courses.mymusic.util.LogUtil;
import com.ixuea.courses.mymusic.util.ResourceUtil;
import com.ixuea.courses.mymusic.util.SwitchDrawableUtil;
import com.ixuea.courses.mymusic.util.TimeUtil;
import com.ixuea.courses.mymusic.util.ToastUtil;

import org.apache.commons.lang3.StringUtils;
import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;
import butterknife.BindView;
import butterknife.OnClick;
import jp.wasabeef.glide.transformations.BlurTransformation;

import static androidx.viewpager.widget.ViewPager.SCROLL_STATE_DRAGGING;
import static androidx.viewpager.widget.ViewPager.SCROLL_STATE_IDLE;
import static com.ixuea.courses.mymusic.util.Constant.MODEL_LOOP_LIST;
import static com.ixuea.courses.mymusic.util.Constant.MODEL_LOOP_ONE;
import static com.ixuea.courses.mymusic.util.Constant.MODEL_LOOP_RANDOM;
import static com.ixuea.courses.mymusic.util.Constant.THUMB_ROTATION_PAUSE;


/**
 * 黑胶唱片界面
 */
public class MusicPlayerActivity extends BaseTitleActivity implements MusicPlayerListener, SeekBar.OnSeekBarChangeListener, ViewPager.OnPageChangeListener, ValueAnimator.AnimatorUpdateListener, BaseQuickAdapter.OnItemClickListener {
    private static final String TAG = "MusicPlayerActivity";
    /**
     * 背景
     */
    @BindView(R.id.iv_background)
    ImageView iv_background;

    /**
     * 歌词容器
     */
    @BindView(R.id.rl_lyric)
    View rl_lyric;

    /**
     * 列表控件(歌词列表)
     */
    @BindView(R.id.rv)
    RecyclerView rv;

    /**
     * 黑胶唱片容器
     */
    @BindView(R.id.cl_record)
    View cl_record;


    /**
     * 黑胶唱片列表控件
     */
    @BindView(R.id.vp)
    ViewPager vp;

    /**
     * 黑胶唱片指针
     */
    @BindView(R.id.iv_record_thumb)
    ImageView iv_record_thumb;

    /**
     * 下载按钮
     */
    @BindView(R.id.ib_download)
    ImageButton ib_download;

    /**
     * 开始位置
     */
    @BindView(R.id.tv_start)
    TextView tv_start;


    /**
     * 进度条
     */
    @BindView(R.id.sb_progress)
    SeekBar sb_progress;

    /**
     * 结束位置
     */
    @BindView(R.id.tv_end)
    TextView tv_end;

    /**
     * 循环模式按钮
     */
    @BindView(R.id.ib_loop_model)
    ImageButton ib_loop_model;

    /**
     * 播放按钮
     */
    @BindView(R.id.ib_play)
    ImageButton ib_play;


    private ListManager listManager;//列表管理器
    private MusicPlayerManager musicPlayerManager;//音乐播放管理器
    private MusicPlayerAdapter recordAdapter;
    private ObjectAnimator playThumbAnimation;
    private ValueAnimator pauseThumbAnimation;
    private LyricAdapter lyricAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_music_player);
    }

    @Override
    protected void initView() {
        super.initView();

        //显示亮色状态栏(父类的这个方法里面，已经把toolbar文字设置为白色了和内容显示到状态栏)
        lightStatusBar();

        //缓存页面数量
        vp.setOffscreenPageLimit(3);

        //黑胶唱片指针旋转点
        //旋转点为15dp
        //而设置需要单位为px
        //所以要先转换
        int rotate = DensityUtil.dip2px(getMainActivity(), 15);
        //图片的锚点 默认是设置在中间的
        //这里设置图片的锚点（这里设置的是在(15dp,15dp)这个锚点上），旋转的话，根据这个锚点来旋转
        iv_record_thumb.setPivotX(rotate);
        iv_record_thumb.setPivotX(rotate);

        //尺寸固定
        rv.setHasFixedSize(true);

        //设置布局管理器
        LinearLayoutManager layoutManager = new LinearLayoutManager(getMainActivity());
        rv.setLayoutManager(layoutManager);

    }

    @Override
    protected void initDatum() {
        super.initDatum();

        //初始化列表管理器
        listManager = MusicPlayerService.getListManager(getApplicationContext());

        //初始化播放管理器
        musicPlayerManager = MusicPlayerService.getMusicPlayerManager(getApplicationContext());

        //创建黑胶唱片列表适配器
        recordAdapter = new MusicPlayerAdapter(getMainActivity(), getSupportFragmentManager());

        //设置到控件
        vp.setAdapter(recordAdapter);

        //设置数据（ViewPager的页数和播放列表的Song数量一样）
        //传递播放列表Song集合数据主要是
        // 向MusicPlayerAdapter->getItem->MusicRecordFragment.newInstance(getData(position));
        // 这里的进入Fragment中获取到当期getData(position)，也就是Song对象
        recordAdapter.setDatum(listManager.getDatum());


        //创建黑胶唱片指针的属性动画

        //属性动画的(对象动画)
        //从暂停到播放状态动画
        //从-25到0 （逆时针就是负数 ）
        //rotation:这个对象的属性是 是父类View中的
        //这里指的是 操作这个对象iv_record_thumb(ImageView中的)) rotation属性
        playThumbAnimation = ObjectAnimator.ofFloat(iv_record_thumb, "rotation", Constant.THUMB_ROTATION_PAUSE,
                Constant.THUMB_ROTATION_PLAY);

        //设置执行时间
        playThumbAnimation.setDuration(Constant.THUMB_DURATION);

        //属性动画的（值动画）
        //从播放到暂停状态动画
        //注意：值动画这里没有用差值器（加速 减速的时候用到），默认是匀速的，所以就不用了
        pauseThumbAnimation = ValueAnimator.ofFloat(Constant.THUMB_ROTATION_PLAY, Constant.THUMB_ROTATION_PAUSE);
        pauseThumbAnimation.setDuration(Constant.THUMB_DURATION);//设置执行时间
        //设置更新监听器
        pauseThumbAnimation.addUpdateListener(this);

        //创建歌词列表适配器
        lyricAdapter = new LyricAdapter(R.layout.item_lyric);

        //设置适配器
        rv.setAdapter(lyricAdapter);

    }

    @Override
    protected void initListeners() {
        super.initListeners();

        //设置拖拽进度控件监听器
        sb_progress.setOnSeekBarChangeListener(this);

        //添加滚动监听事件
        vp.addOnPageChangeListener(this);

        //设置歌词点击事件
        lyricAdapter.setOnItemClickListener(this);
    }

    /**
     * 界面显示了
     */
    @Override
    protected void onResume() {
        super.onResume();

        //显示初始化数据
        showInitData();

        //显示音乐时长
        showDuration();
        //显示播放进度
        showProgress();
        //显示播放状态
        showMusicPlayStatus();

        //显示循环模式
        showLoopModel();

        //添加监听器
        musicPlayerManager.addMusicPlayerListener(this);

        //注册发布订阅框架
        EventBus.getDefault().register(this);

        //滚动当前音乐位置
        scrollPosition();

        //显示歌词数据
        showLyricData();
    }

    /**
     * //显示歌词数据
     */
    private void showLyricData() {
        //获取当前的音乐
        Song data = listManager.getData();
        if (data.getParsedLyric() == null) {
            //Song 里面的解析过的歌词对象Lyric 为null（严格来说是里面的集合list大小为0，说明解析失败了）

            //从 MusicPlayerManagerImpl->data.setParsedLyric(LyricParser.parse(data.getStyle(),data.getLyric()));
            // --> LyricParser -> KSCLyricParser-parse 中知道 这个data.getParsedLyric()（不是null的）
            //所以时候这里的if不会执行 ，所以这里写不写都无所谓
            lyricAdapter.replaceData(new ArrayList<>());//空数组，里面没有数据，相等于替换了，数据没有了
        } else {
            //设置歌词数据 （这个数据把原来的数据替换掉了）
            lyricAdapter.replaceData(data.getParsedLyric().getDatum());
        }
    }

    /**
     * 滚动当前音乐位置
     */
    private void scrollPosition() {
        //选中当前播放的音乐

        //使用post方法是
        //将方法放到了消息循环
        //如果不这样做，在onCreate这样的方法中滚动无效
        //因为这时候列表的数据还没有显示完成
        //具体的这部分我们在《详解View》课程中讲解了
        vp.post(new Runnable() {
            @Override
            public void run() {
                //找到当前播放音乐的索引
                int index = listManager.getDatum().indexOf(listManager.getData());
                //也可以不判断（因为进入到这个界面，是有音乐播放的，可以不判断）
                if (index != -1) {
                    //滚动到改位置 如果用参数2 为true：说明是以动画的方式滚动上去 默认是为true的
                    //setCurrentItem是ViewPager自带的

                    //设置false不用动画，默认好像是从左到右的，设置false不用动画，效果，感觉会好点
                    //有动画，动画可能会好点，因为可能我们会监听滚动完成以后，也就是动画执行完成以后，才能开始继续滚动
                    //那么的话，这个时间的话是不那么好监听的
                    vp.setCurrentItem(index, false);
                }

                //判断是否需要旋转黑胶唱片
                if (musicPlayerManager.isPlaying()) {
                    startRecordRotate();
                } else {
                    stopRecordRotate();
                }

            }
        });
    }

    /**
     * 显示播放状态
     */
    private void showMusicPlayStatus() {
        if (musicPlayerManager.isPlaying()) {
            showPauseStatus();
        } else {
            showPlayStatus();
        }
    }

    /**
     * 界面隐藏了
     */
    @Override
    protected void onPause() {
        super.onPause();

        //移除播放监听器
        musicPlayerManager.removeMusicPlayerListener(this);

        //解除注册
        EventBus.getDefault().unregister(this);
    }

    /**
     * 播放列表改变了事件
     *
     * @param event
     */
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onPlayListChangeEvent(PlayListChangeEvent event) {
        if (listManager.getDatum().size() == 0) {
            //没有音乐（播放列表没有音乐）

            //关闭播放界面
            finish();
        }
    }

    /**
     * 黑胶唱片点击事件
     */
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onRecordClickEvent(OnRecordClickEvent event) {

        if (isLyricEmpty()) {
            //没有歌词
            ToastUtil.errorShortToast(R.string.lyric_empty);
            return;
        }

        //隐藏黑胶唱片
        //cl_record(就是ConstraintLayout:ViewPager和ImageView(指针的) 的父容器)
        cl_record.setVisibility(View.GONE);

        //显示歌词
        rl_lyric.setVisibility(View.VISIBLE);

    }

    /**
     * 是否没有歌词
     */
    private boolean isLyricEmpty() {
        return lyricAdapter.getItemCount() == 0;//等于0 表示没有歌词，返回true
    }

    /**
     * 显示初始化数据
     */
    public void showInitData() {
        //获取当前播放的音乐
        Song data = listManager.getData();

        //进入到这个界面的时候，音乐已经播放了，所以data!=null 的，所以不用判断了

        //设置标题(顶部的toolbar 的标题设置为 播放音乐的标题)
        setTitle(data.getTitle());

        //设置子标题(用toolbar设置)
        toolbar.setSubtitle(data.getSinger().getNickname());

//        //显示背景
//        ImageUtil.show(getMainActivity(), iv_background, data.getBanner());

        //实现背景高斯模糊效果 返回的是RequestBuilder<Drawable>
        RequestBuilder<Drawable> requestBuilder = Glide.with(this).asDrawable();

        if (StringUtils.isBlank(data.getBanner())) {//data:正在播放的音乐
            //没有封面图

            //使用默认封面图
            requestBuilder.load(R.drawable.default_album);
        } else {
            //使用真是图片
            requestBuilder.load(ResourceUtil.resourceUri(data.getBanner()));
        }
        //创建请求选项
        //传入了BlurTransformation
        //用来实现高斯模糊
        //radius:模糊半径；值越大越模糊
        //sampling:采样率；值越大越模糊

        //bitmapTransform方法是Glide中的RequestOptions类中的
        //BlurTransformation:是新添加的图片变换框架里面的 blur：模拟
        RequestOptions options = RequestOptions.bitmapTransform(new BlurTransformation(25, 3));

        //加载图片
        requestBuilder
                .apply(options)
//                .into(iv_background);//后面需要实现一些效果，所以先不用这个
                .into(new CustomTarget<Drawable>() {
                    /**
                     * 资源下载成功
                     */
                    @Override
                    public void onResourceReady(@NonNull Drawable resource, @Nullable Transition<? super Drawable> transition) {
//                        //设置到背景控件上
//                        iv_background.setImageDrawable(resource);

                        //创建切换你动画工具类(内部会根据这2个drawable合并成一个新的drawable)
                        // Switch:切换的意思
                        //为啥要2个drawable （一个在下  一个上面）上面的那个设置个淡出的效果（在原来背景（也就是下面的图片的基础上）实现这种效果）
                        SwitchDrawableUtil switchDrawableUtil = new SwitchDrawableUtil(iv_background.getDrawable(), resource);

                        //设置drawable到ImageView上
                        iv_background.setImageDrawable(switchDrawableUtil.getDrawable());

                        //开始动画
                        switchDrawableUtil.start();
                    }

                    @Override
                    public void onLoadCleared(@Nullable Drawable placeholder) {

                    }
                });

    }

    /**
     * 创建菜单方法
     * <p>
     * 有点类似显示布局要写到onCreate方法一样
     */
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        //加载布局文件
        getMenuInflater().inflate(R.menu.menu_music_player, menu);
        return true;//这个地方要返回true
    }

    /**
     * menu item按钮点击了
     *
     * @return
     */
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();//获取点击的菜单id
        if (R.id.action_share == id) {
            //分享按钮点击了
            ToastUtil.successShortToast("你点击了分享按钮！");
            return true;//注意：这里返回了true
        }
        return super.onOptionsItemSelected(item);
    }

    /**
     * 下载按钮点击
     */
    @OnClick(R.id.ib_download)
    public void onDownLoadClick() {
        LogUtil.d(TAG, "onDownLoadClick:");
    }

    /**
     * 循环模式按钮点击
     */
    @OnClick(R.id.ib_loop_model)
    public void onLoopModelClick() {
        LogUtil.d(TAG, "onLoopModelClick:");

        //更改模式
        listManager.changeLoopModel();

        //显示循环模式
        showLoopModel();
    }

    /**
     * 显示循环模式
     */
    private void showLoopModel() {
        //获取当循环模式
        int model = listManager.getLoopModel();
        switch (model) {
            case MODEL_LOOP_RANDOM:
                ib_loop_model.setImageResource(R.drawable.ic_music_repeat_random);
                break;
            case MODEL_LOOP_LIST:
                ib_loop_model.setImageResource(R.drawable.ic_music_repeat_list);
                break;
            case MODEL_LOOP_ONE:
                ib_loop_model.setImageResource(R.drawable.ic_music_repeat_one);
                break;
            default:
                break;
        }
    }


    /**
     * 上一曲按钮点击
     */
    @OnClick(R.id.ib_previous)
    public void onPreviousClick() {
        LogUtil.d(TAG, "onPreviousClick:");

//        //停止黑胶唱片滚动
//        stopRecordRotate();

        listManager.play(listManager.previous());
    }

    /**
     * 播放按钮
     */
    @OnClick(R.id.ib_play)
    public void onPlayClick() {
        LogUtil.d(TAG, "onPlayClick:");

        playOrPause();
    }

    /**
     * 播放或暂停
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
     * 下一曲按钮点击
     */
    @OnClick(R.id.ib_next)
    public void onNextClick() {
        LogUtil.d(TAG, "onNextClick:");

        //注释删除掉，因为已经在listManager.play里面处理了
//        //停止黑胶唱片滚动
//        stopRecordRotate();

        listManager.play(listManager.next());
    }

    /**
     * 播放列表按钮点击
     */
    @OnClick(R.id.ib_list)
    public void onListClick() {
        LogUtil.d(TAG, "onListClick:");

        //显示播放列表对话框（弹窗）
        PlayListDialogFragment.show(getSupportFragmentManager());

    }

    /**
     * 显示播放进度
     */
    private void showProgress() {
        //获取当前播放进度
        long progress = listManager.getData().getProgress();

        //格式化进度
        tv_start.setText(TimeUtil.formatMinuteSecond((int) progress));

        //设置到进度条
        sb_progress.setProgress((int) progress);
    }

    /**
     * 显示播放状态
     */
    private void showPlayStatus() {
        ib_play.setImageResource(R.drawable.ic_music_play);
    }

    /**
     * 显示暂停状态
     */
    private void showPauseStatus() {
        ib_play.setImageResource(R.drawable.ic_music_pause);
    }

    /**
     * 黑胶唱片停止滚动
     * 黑胶唱片指针回到暂停状态
     */
    private void stopRecordRotate() {
        //停止旋转黑胶唱片指针
        stopRecordThumbRotate();

        //获取当前播放的音乐
        Song data = listManager.getData();

        LogUtil.d(TAG, "stopRecordRotate: " + data.getTitle());

        //发送通知(停止通知)
        EventBus.getDefault().post(new OnStopRecordEvent(data));

    }

    /**
     * 开始黑胶唱片滚动
     * 指针回到播放位置
     */
    private void startRecordRotate() {
        //旋转黑胶唱片指针
        startRecordThumbRotate();

        Song data = listManager.getData();//获取当前播放的音乐

        LogUtil.d(TAG, "startRecordRotate: " + data.getTitle());

        //发送通知
        EventBus.getDefault().post(new OnStartRecordEvent(data));
    }

    /**
     * 黑胶唱片指针默认状态动画（播放状态）
     */
    public void startRecordThumbRotate() {
        playThumbAnimation.start();
    }

    /**
     * 黑胶唱片指针旋转-25度动画（暂停状态）
     */
    public void stopRecordThumbRotate() {
        //获取黑胶唱片指针旋转的角度
        float thumbRotation = iv_record_thumb.getRotation();
        LogUtil.d(TAG, "rotation: " + thumbRotation);

        //如果不判断
        //当前已经停止了
        //如果再次执行停止动画
        //就有跳动问题
        if (THUMB_ROTATION_PAUSE == thumbRotation) {
            //已经是停止状态了

            //就返回
            return;
        }
        pauseThumbAnimation.start();
    }

    /**
     * 播放前回调
     */
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onPlayEvent(OnPlayEvent event) {
        //停止黑胶唱片滚动
        stopRecordRotate();
    }

    //播放管理器回调
    @Override
    public void onPaused(Song data) {
        showPlayStatus();

        //停止黑胶唱片滚动
        stopRecordRotate();
    }

    @Override
    public void onPlaying(Song data) {
        showPauseStatus();

        //开始黑胶唱片滚动
        startRecordRotate();
    }

    @Override
    public void onPrepared(MediaPlayer mp, Song data) {
        //显示初始化数据
        showInitData();

        //显示时长
        showDuration();

        //滚动到当前音乐位置
        scrollPosition();
    }

    private void showDuration() {
        //获取音乐时长
        long duration = listManager.getData().getDuration();
        //格式化
        tv_end.setText(TimeUtil.formatMinuteSecond((int) duration));
        //设置到进度条（设置到进度条最大值）
        sb_progress.setMax((int) duration);
    }

    @Override
    public void onProgress(Song data) {

        showProgress();
    }

    @Override
    public void onLyricChanged(Song data) {
        showLyricData();
    }

    //end播放管理器回调


    /**
     * 进度改变了回调
     *
     * @param seekBar  SeekBar
     * @param progress 当前进度
     * @param fromUser 是否是用户触发的
     */
    @Override
    public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
        if (fromUser) {
            //跳转到该位置播放
            listManager.seekTo(progress);
        }
    }

    @Override
    public void onStartTrackingTouch(SeekBar seekBar) {

    }

    @Override
    public void onStopTrackingTouch(SeekBar seekBar) {

    }

    //ViewPager 页面改变监听(滚动监听器)

    /**
     * 滚动中
     */
    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

    }

    /**
     * 滚动完成了
     */
    @Override
    public void onPageSelected(int position) {

    }

    /**
     * 滚动状态改变了
     *
     * @param state 滚动状态
     * @see：表示更多的信息，参考这个
     * @see ViewPager#SCROLL_STATE_IDLE：空闲  0
     * @see ViewPager#SCROLL_STATE_DRAGGING：正在拖拽 1
     * @see ViewPager#SCROLL_STATE_SETTLING：滚动完成后 2
     */
    @Override
    public void onPageScrollStateChanged(int state) {
        LogUtil.d(TAG, "onPageScrollStateChanged:" + state);
        //ViewPager中的静态变量SCROLL_STATE_IDLE
        if (SCROLL_STATE_DRAGGING == state) {
            //拖拽状态

            //停止当前item滚动
            stopRecordRotate();
        } else if (SCROLL_STATE_IDLE == state) {
            //空闲状态

            //判断黑胶唱片位置对应的音乐是否和现在播放的是一首 vp.getCurrentItem():返回当前item的索引
            Song song = listManager.getDatum().get(vp.getCurrentItem());
            if (listManager.getData().getId().equals(song.getId())) {
                //一样(唱片中的音乐 等于 当前播放音乐 )

                //判断播放状态
                if (musicPlayerManager.isPlaying()) {
                    //开始旋转（当前的唱片中的音乐是在播放，那么就旋转）
                    startRecordRotate();
                }

            } else {
                //不一样

                //播放当前的这首音乐
                listManager.play(song);
            }
        }

    }
    //end ViewPager 页面改变监听(滚动监听器)

    /**
     * 属性动画回调
     * @param animation ValueAnimator
     */
    @Override
    public void onAnimationUpdate(ValueAnimator animation) {
        //对控件设置 旋转（获取到的属性值设置到这个控件）
        iv_record_thumb.setRotation((Float) animation.getAnimatedValue());
    }


    /**
     * 歌词点击事件
     *
     * @param adapter
     * @param view
     * @param position
     */
    @Override
    public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
        //显示黑胶唱片
        cl_record.setVisibility(View.VISIBLE);

        //隐藏歌词
        rl_lyric.setVisibility(View.GONE);
    }

    /**
     * 启动方法
     */
    public static void start(Activity activity) {
        Intent intent = new Intent(activity, MusicPlayerActivity.class);
        activity.startActivity(intent);
    }

}
