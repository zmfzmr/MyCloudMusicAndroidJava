package com.ixuea.courses.mymusic;

import android.app.Activity;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.RequestBuilder;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.request.target.CustomTarget;
import com.bumptech.glide.request.transition.Transition;
import com.ixuea.courses.mymusic.activity.BaseTitleActivity;
import com.ixuea.courses.mymusic.domain.Song;
import com.ixuea.courses.mymusic.manager.ListManager;
import com.ixuea.courses.mymusic.manager.MusicPlayerManager;
import com.ixuea.courses.mymusic.service.MusicPlayerService;
import com.ixuea.courses.mymusic.util.ResourceUtil;
import com.ixuea.courses.mymusic.util.SwitchDrawableUtil;

import org.apache.commons.lang3.StringUtils;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import butterknife.BindView;
import jp.wasabeef.glide.transformations.BlurTransformation;


/**
 * 黑胶唱片界面
 */
public class MusicPlayerActivity extends BaseTitleActivity {
    /**
     * 背景
     */
    @BindView(R.id.iv_background)
    ImageView iv_background;
    private ListManager listManager;
    private MusicPlayerManager musicPlayerManager;

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
     * 启动方法
     */
    public static void start(Activity activity) {
        Intent intent = new Intent(activity, MusicPlayerActivity.class);
        activity.startActivity(intent);
    }
}
