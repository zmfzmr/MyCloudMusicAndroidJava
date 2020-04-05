package com.ixuea.courses.mymusic.view;

import android.content.Context;
import android.os.Build;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.ixuea.courses.mymusic.R;
import com.ixuea.courses.mymusic.domain.Song;
import com.ixuea.courses.mymusic.domain.lyric.Line;
import com.ixuea.courses.mymusic.domain.lyric.Lyric;
import com.ixuea.courses.mymusic.domain.lyric.LyricUtil;
import com.ixuea.courses.mymusic.listener.GlobalLyricListener;
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
    View ll_play_container;

    /**
     * 播放按钮
     */
    @BindView(R.id.iv_play)
    ImageView iv_play;

    private GlobalLyricListener globalLyricListener;//全局歌词控件监听器

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

        initListener();
    }

    /**
     * 初始化监听器
     */
    private void initListener() {
        //歌词view点击
        OnClickListener thisClickListener = new OnClickListener() {

            @Override
            public void onClick(View v) {
                if (iv_logo.getVisibility() == View.VISIBLE) {
                    //标准模式
                    LogUtil.d(TAG, "normalStyle");
                    //显示简单模式（现在是标准模式 切换到标准模式）
                    simpleStyle();
                } else {
                    //简单模式
                    LogUtil.d(TAG, "simpleStyle");
                    //切换标准模式
                    normalStyle();
                }
            }
        };
        //this：桌面歌词控件
        this.setOnClickListener(thisClickListener);
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

        //设置第一行歌词始终是选中状态(就是选中：行控件那边根据选中来绘制文本等)
        llv1.setLineSelected(true);
    }

    /**
     * 设置背景
     */
    private void setBackground() {
        setBackgroundColor(getResources().getColor(R.color.global_lyric_background));
    }

    /**
     * 设置全局歌词控件监听器
     */
    public void setGlobalLyricListener(GlobalLyricListener globalLyricListener) {
        this.globalLyricListener = globalLyricListener;
    }

    /**
     * 简单样式
     * 只有歌词
     */
    public void simpleStyle() {
        //背景设置为透明
        setBackgroundColor(getResources().getColor(R.color.transparent));

        //隐藏logo
        iv_logo.setVisibility(GONE);
        //隐藏关闭按钮
        iv_close.setVisibility(GONE);
        //隐藏播放控制容器
        ll_play_container.setVisibility(GONE);
    }

    /**
     * 标准样式
     * 都显示
     */
    public void normalStyle() {
        //设置背景为半透明
        setBackground();

        //显示 logo 关闭按钮 和 播放控制容器
        iv_logo.setVisibility(VISIBLE);
        iv_close.setVisibility(VISIBLE);
        ll_play_container.setVisibility(VISIBLE);
    }

    /**
     * logo点击
     */
    @OnClick(R.id.iv_logo)
    public void onLogoClick() {
        LogUtil.d(TAG, "onLogoClick");

        globalLyricListener.onLogoClick();
    }

    /**
     * 关闭点击
     */
    @OnClick(R.id.iv_close)
    public void onCloseClick() {
        LogUtil.d(TAG, "onCloseClick");
        globalLyricListener.onCloseClick();
    }

    /**
     * 锁定点击
     */
    @OnClick(R.id.iv_lock)
    public void onLockClick() {
        LogUtil.d(TAG, "onLockClick");
        globalLyricListener.onLockClick();
    }

    /**
     * 上一曲
     */
    @OnClick(R.id.iv_previous)
    public void onPreviousClick() {
        LogUtil.d(TAG, "onPreviousClick");
        globalLyricListener.onPreviousClick();
    }

    /**
     * 播放点击
     */
    @OnClick(R.id.iv_play)
    public void onPlayClick() {
        LogUtil.d(TAG, "onPlayClick:");
        globalLyricListener.onPlayClick();
    }

    /**
     * 下一曲
     */
    @OnClick(R.id.iv_next)
    public void onNextClick() {
        LogUtil.d(TAG, "onNextClick:");
        globalLyricListener.onNextClick();
    }

    /**
     * 设置点击
     */
    @OnClick(R.id.iv_settings)
    public void onSettingsClick() {
        LogUtil.d(TAG, "onSettingsClick:");
    }

    /**
     * 清除歌词
     */
    public void clearLyric() {
        llv1.setData(null);
        llv2.setData(null);
        //没有歌词显示一行就够了，把另外一个llv2隐藏掉
//        llv2.setVisibility(GONE);
        llv2.setVisibility(INVISIBLE);
    }

    /**
     * 设置歌词是否是精确模式
     */
    public void setAccurate(boolean accurate) {
        llv1.setAccurate(accurate);
    }

    /**
     * 设置播放状态
     *
     * @param playing
     */
    public void setPlay(boolean playing) {
        iv_play.setImageResource(playing ? R.drawable.ic_global_pause : R.drawable.ic_global_play);
    }

    /**
     * 音乐进度回调（里面代码跟BaseMusicPlayerActivity的onProgress写的差不多）
     */
    public void onProgress(Song data) {
        //获取当前音乐歌词
        Lyric lyric = data.getParsedLyric();

        if (lyric == null) {
            //没有歌词
            return;
        }

        long progress = data.getProgress();
        //获取当前进度对应的歌词行
        Line line = LyricUtil.getLyricLine(lyric, progress);
        llv1.setData(line);
        //如果是精确到字歌词
        //就需要计算已经播放到那个字相关信息
        if (lyric.isAccurate()) {
            //获取当前时间是该行那个字
            int lyricCurrentWordIndex = LyricUtil.getWordIndex(line, progress);
            //当前时间该字已经播放的时间
            float wordPlayedTime = LyricUtil.getWordPlayedTime(line, progress);
            //设置行 当前字索引和当前字播放时间 到行控件
            llv1.setLyricCurrentWordIndex(lyricCurrentWordIndex);
            llv1.setWordPlayedTime(wordPlayedTime);
            //刷新控件
            llv1.onProgress();
        }


        //第二行歌词控件

        //获取当前时间对应的行数索引
        int lineNumber = LyricUtil.getLineNumber(lyric, progress);

        if (lineNumber < lyric.getDatum().size() - 1) {//lyric:解析的歌词对象
            //还有下一行歌词

            //获取下一行歌词
            Line nextLine = lyric.getDatum().get(lineNumber + 1);
            llv2.setData(nextLine);
        }


    }

    /**
     * 显示第二个歌词控件
     */
    public void setSecondLyricView() {
        llv2.setVisibility(VISIBLE);
    }
}
