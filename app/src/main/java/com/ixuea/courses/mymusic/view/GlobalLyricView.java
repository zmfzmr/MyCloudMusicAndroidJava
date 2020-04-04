package com.ixuea.courses.mymusic.view;

import android.content.Context;
import android.os.Build;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.ixuea.courses.mymusic.R;
import com.ixuea.courses.mymusic.util.LogUtil;

import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * 全局(桌面)歌词
 */
public class GlobalLyricView extends LinearLayout {
    private static final String TAG = "GlobalLyricView";
    /**
     * 第一行歌词控件
     */
    @BindView(R.id.llv1)
    LyricLineView llv1;

    /**
     * 第二行歌词控件
     */
    @BindView(R.id.llv2)
    LyricLineView llv2;
    /**
     * logo
     */
    @BindView(R.id.iv_logo)
    ImageView iv_logo;

    /**
     * 关闭按钮
     */
    @BindView(R.id.iv_close)
    ImageView iv_close;
    /**
     * 播放控制容器
     */
    @BindView(R.id.ll_play_container)
    View ll_play_all_container;

    /**
     * 播放按钮
     */
    @BindView(R.id.iv_play)
    ImageView iv_play;

    /**
     * 构造方法
     */
    public GlobalLyricView(Context context) {
        super(context);
        init();
    }

    public GlobalLyricView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public GlobalLyricView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public GlobalLyricView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init();
    }

    /**
     * 初始化View
     */
    private void init() {
        initViews();
    }

    /**
     * 初始化View
     */
    private void initViews() {
        //设置背景
        setBackground();

        //从xml加载view
        //这里用的是getContext()
        //参数2：root（ViewGroup）：传入this,表示是当前对象  参数3；是否附加到当前root（ViewGroup）上
        View view = LayoutInflater.from(getContext()).inflate(R.layout.item_global_lyric, this, false);

        //添加到当前容器
        addView(view);
        //这样的话，外界在使用这个控件(GlobalLyricView)的时候,就可以使用布局item_global_lyric的内容啦

        //初始化View注入框架
        ButterKnife.bind(this);
    }

    /**
     * 设置背景
     */
    private void setBackground() {
        setBackgroundColor(getResources().getColor(R.color.global_lyric_background));
    }

    /**
     * logo点击
     */
    @OnClick(R.id.iv_logo)
    public void onLogoClick() {
        LogUtil.d(TAG, "onLogoClick");
    }

    /**
     * 关闭点击
     */
    @OnClick(R.id.iv_close)
    public void onCloseClick() {
        LogUtil.d(TAG, "onCloseClick");
    }

    /**
     * 锁定点击
     */
    @OnClick(R.id.iv_lock)
    public void onLockClick() {
        LogUtil.d(TAG, "onLockClick");
    }

    /**
     * 上一曲
     */
    @OnClick(R.id.iv_previous)
    public void onPreviousClick() {
        LogUtil.d(TAG, "onPreviousClick");
    }

    /**
     * 播放点击
     */
    @OnClick(R.id.iv_play)
    public void onPlayClick() {
        LogUtil.d(TAG, "onPlayClick:");
    }

    /**
     * 下一曲
     */
    @OnClick(R.id.iv_next)
    public void onNextClick() {
        LogUtil.d(TAG, "onNextClick:");
    }

    /**
     * 设置点击
     */
    @OnClick(R.id.iv_settings)
    public void onSettingsClick() {
        LogUtil.d(TAG, "onSettingsClick:");
    }

}
