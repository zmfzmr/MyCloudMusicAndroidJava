package com.ixuea.courses.mymusic;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageView;

import com.ixuea.courses.mymusic.activity.BaseTitleActivity;
import com.ixuea.courses.mymusic.domain.Song;
import com.ixuea.courses.mymusic.manager.ListManager;
import com.ixuea.courses.mymusic.manager.MusicPlayerManager;
import com.ixuea.courses.mymusic.service.MusicPlayerService;
import com.ixuea.courses.mymusic.util.ImageUtil;

import butterknife.BindView;

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

        //显示背景
        ImageUtil.show(getMainActivity(), iv_background, data.getBanner());

    }
    /**
     * 启动方法
     */
    public static void start(Activity activity) {
        Intent intent = new Intent(activity, MusicPlayerActivity.class);
        activity.startActivity(intent);
    }
}
