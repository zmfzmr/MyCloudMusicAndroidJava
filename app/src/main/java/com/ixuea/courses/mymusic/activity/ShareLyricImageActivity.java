package com.ixuea.courses.mymusic.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

import com.ixuea.courses.mymusic.R;
import com.ixuea.courses.mymusic.domain.Song;
import com.ixuea.courses.mymusic.util.Constant;

/**
 * 分享歌词图片界面
 */
public class ShareLyricImageActivity extends BaseTitleActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_share_lyric_image);
    }

    /**
     * 启动方法
     */
    public static void start(Activity activity, Song data, String lyric) {
        //创建intent
        Intent intent = new Intent(activity, ShareLyricImageActivity.class);
        //传递音乐
        intent.putExtra(Constant.DATA, data);
        //传递歌词
        intent.putExtra(Constant.ID, lyric);
        //启动界面
        activity.startActivity(intent);
    }
}
