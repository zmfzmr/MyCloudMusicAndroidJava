package com.ixuea.courses.mymusic.activity;

import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.ixuea.courses.mymusic.R;
import com.ixuea.courses.mymusic.util.LogUtil;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * 扫描本地音乐界面
 */
public class ScanLocalMusicActivity extends BaseTitleActivity {
    private static final String TAG = "ScanLocalMusicActivity";
    /**
     * 扫描进度
     */
    @BindView(R.id.tv_progress)
    TextView tv_progress;

    /**
     * 扫描线
     */
    @BindView(R.id.iv_scan_line)
    ImageView iv_scan_line;

    /**
     * 扫描放大镜
     */
    @BindView(R.id.iv_scan_zoom)
    ImageView iv_scan_zoom;

    /**
     * 扫描按钮
     */
    @BindView(R.id.bt_scan)
    Button bt_scan;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scan_local_music);
    }

    /**
     * 扫描按钮点击了
     */
    @OnClick(R.id.bt_scan)
    public void onScanClick() {
        LogUtil.d(TAG, "onScanClick: ");
    }
}
