package com.ixuea.courses.mymusic.activity;

import android.os.Bundle;

import com.ixuea.courses.mymusic.R;
import com.ixuea.courses.mymusic.adapter.DownloadAdapter;

import java.util.ArrayList;

import androidx.viewpager.widget.ViewPager;
import butterknife.BindView;

/**
 * 下载管理界面
 */
public class DownloadActivity extends BaseTitleActivity {
    /**
     * ViewPager 左右滚动控件
     */
    @BindView(R.id.vp)
    ViewPager vp;
    private DownloadAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_download);
    }

    @Override
    protected void initDatum() {
        super.initDatum();

        //创建适配器
        adapter = new DownloadAdapter(getMainActivity(), getSupportFragmentManager());

        //设置适配器
        vp.setAdapter(adapter);

        //创建列表
        ArrayList<Integer> datum = new ArrayList<>();
        datum.add(0);
        datum.add(1);
        //设置数据
        adapter.setDatum(datum);
    }
}
