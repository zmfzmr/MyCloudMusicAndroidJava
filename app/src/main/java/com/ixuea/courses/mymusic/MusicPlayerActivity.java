package com.ixuea.courses.mymusic;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

import com.ixuea.courses.mymusic.activity.BaseTitleActivity;

/**
 * 黑胶唱片界面
 */
public class MusicPlayerActivity extends BaseTitleActivity {

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

    /**
     * 启动方法
     */
    public static void start(Activity activity) {
        Intent intent = new Intent(activity, MusicPlayerActivity.class);
        activity.startActivity(intent);
    }
}
