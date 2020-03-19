package com.ixuea.courses.mymusic;

import android.app.Activity;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.RequestBuilder;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.request.target.CustomTarget;
import com.bumptech.glide.request.transition.Transition;
import com.ixuea.courses.mymusic.activity.BaseTitleActivity;
import com.ixuea.courses.mymusic.domain.Song;
import com.ixuea.courses.mymusic.listener.MusicPlayerListener;
import com.ixuea.courses.mymusic.manager.ListManager;
import com.ixuea.courses.mymusic.manager.MusicPlayerManager;
import com.ixuea.courses.mymusic.service.MusicPlayerService;
import com.ixuea.courses.mymusic.util.LogUtil;
import com.ixuea.courses.mymusic.util.ResourceUtil;
import com.ixuea.courses.mymusic.util.SwitchDrawableUtil;
import com.ixuea.courses.mymusic.util.ToastUtil;

import org.apache.commons.lang3.StringUtils;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import butterknife.BindView;
import butterknife.OnClick;
import jp.wasabeef.glide.transformations.BlurTransformation;


/**
 * 黑胶唱片界面
 */
public class MusicPlayerActivity extends BaseTitleActivity implements MusicPlayerListener {
    private static final String TAG = "MusicPlayerActivity";
    /**
     * 背景
     */
    @BindView(R.id.iv_background)
    ImageView iv_background;

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
    }

    @Override
    protected void initDatum() {
        super.initDatum();

        //初始化列表管理器
        listManager = MusicPlayerService.getListManager(getApplicationContext());

        //初始化播放管理器
        musicPlayerManager = MusicPlayerService.getMusicPlayerManager(getApplicationContext());

    }

    /**
     * 界面显示了
     */
    @Override
    protected void onResume() {
        super.onResume();

        //显示初始化数据
        showInitData();

        //添加监听器
        musicPlayerManager.addMusicPlayerListener(this);
    }

    /**
     * 界面隐藏了
     */
    @Override
    protected void onPause() {
        super.onPause();

        //移除播放监听器
        musicPlayerManager.removeMusicPlayerListener(this);
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
    }


    /**
     * 上一曲按钮点击
     */
    @OnClick(R.id.ib_previous)
    public void onPreviousClick() {
        LogUtil.d(TAG, "onPreviousClick:");
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
    }

    /**
     * 播放列表按钮点击
     */
    @OnClick(R.id.ib_list)
    public void onListClick() {
        LogUtil.d(TAG, "onListClick:");
    }

    /**
     * 启动方法
     */
    public static void start(Activity activity) {
        Intent intent = new Intent(activity, MusicPlayerActivity.class);
        activity.startActivity(intent);
    }

    //播放管理器回调
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

    }
    //end播放管理器回调
}
