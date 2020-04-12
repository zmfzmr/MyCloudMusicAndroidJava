package com.ixuea.courses.mymusic.activity;

import android.os.Bundle;

import com.ixuea.courses.mymusic.R;
import com.ixuea.courses.mymusic.domain.Song;

/**
 * 选中歌词界面
 */
public class SelectLyricActivity extends BaseTitleActivity {
    /**
     * 音乐对象
     */
    private Song data;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_lyric);
    }

    @Override
    protected void initDatum() {
        super.initDatum();

        //获取传递的数据
        data = (Song) extraData();
    }
}
