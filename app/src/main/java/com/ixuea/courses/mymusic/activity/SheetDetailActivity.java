package com.ixuea.courses.mymusic.activity;

import android.os.Bundle;

import com.ixuea.courses.mymusic.R;
import com.ixuea.courses.mymusic.util.Constant;

/**
 * 歌单详情界面
 */
public class SheetDetailActivity extends BaseTitleActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sheet_detail);
    }

    @Override
    protected void initDatum() {
        super.initDatum();

        //获取传递的参数
        getIntent().getStringExtra(Constant.ID);
    }
}
