package com.ixuea.courses.mymusic.activity;

import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.VideoView;

import com.github.jdsjlzx.recyclerview.LRecyclerView;
import com.google.android.material.appbar.AppBarLayout;
import com.ixuea.courses.mymusic.R;
import com.ixuea.courses.mymusic.api.Api;
import com.ixuea.courses.mymusic.domain.Video;
import com.ixuea.courses.mymusic.domain.response.DetailResponse;
import com.ixuea.courses.mymusic.listener.HttpObserver;
import com.ixuea.courses.mymusic.util.LogUtil;
import com.ixuea.courses.mymusic.util.ResourceUtil;
import com.ixuea.courses.mymusic.util.TimeUtil;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * 视频详情
 */
public class VideoDetailActivity extends BaseTitleActivity implements MediaPlayer.OnPreparedListener {

    private static final String TAG = "VideoDetailActivity";
    /**
     * 播放器
     */
    @BindView(R.id.vv)
    VideoView vv;

    /**
     * 标题容器
     */
    @BindView(R.id.abl)
    AppBarLayout abl;

    /**
     * 播放控制容器
     */
    @BindView(R.id.control_container)
    View control_container;

    /**
     * 开始时间
     */
    @BindView(R.id.tv_start)
    TextView tv_start;

    /**
     * 进度条
     */
    @BindView(R.id.sb)
    SeekBar sb;

    /**
     * 结束时间
     */
    @BindView(R.id.tv_end)
    TextView tv_end;

    /**
     * 全屏切换按钮
     */
    @BindView(R.id.ib_screen)
    ImageButton ib_screen;

    /**
     * 播放按钮
     */
    @BindView(R.id.ib_play)
    ImageButton ib_play;

    /**
     * 播放信息
     */
    @BindView(R.id.tv_info)
    TextView tv_info;

    /**
     * 列表控件
     */
    @BindView(R.id.rv)
    LRecyclerView rv;

    private String id;//视频id

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_video_detail);
    }

    @Override
    protected void initDatum() {
        super.initDatum();
        //获取id
        id = extraId();

        Api.getInstance()
                .videoDetail(id)
                .subscribe(new HttpObserver<DetailResponse<Video>>() {
                    @Override
                    public void onSucceeded(DetailResponse<Video> data) {
                        next(data.getData());
                    }
                });
    }

    @Override
    protected void initListeners() {
        super.initListeners();
        //设置准备播放监听器
        vv.setOnPreparedListener(this);
    }

    /**
     * 显示数据
     */
    private void next(Video data) {
        //直接写data，其实就是调用toString方法
        LogUtil.d(TAG, "next:" + data);

        //设置播放路径
        //uri=assets/yi_huang_jiu_lao_le.mp4  换成绝对路径  并用Uri.parse 解析
        //这里的ResourceUtil.resourceUri(data.getUri())：http://dev-courses-misuc.ixuea.com/assets/yi_huang_jiu_lao_le.mp4
        //但是这里需要一个uri对象，所以需要解析成uri对象才可以的
        vv.setVideoURI(Uri.parse(ResourceUtil.resourceUri(data.getUri())));

        //开始播放(记得开始播放)
        vv.start();

        //标题(setTitle: 是Activity里面的方法，设置标题栏标题)
        setTitle(data.getTitle());
    }

    /**
     * 播放按钮点击
     */
    @OnClick(R.id.ib_play)
    public void onPlayClick() {
        if (isPlaying()) {
            //在播放

            //暂停
            pause();
            LogUtil.d(TAG, "onPlayClick pause");
        } else {
            //没有播放

            //播放
            resume();
        }
    }

    /**
     * 继续播放(继续播放是 是一个暂停的图标)
     */
    private void resume() {
        vv.start();

        //显示暂停状态
        showPauseStatus();
    }

    /**
     * 暂停播放(是一个播放的图标)
     */
    private void pause() {
        vv.pause();

        //显示播放状态
        showPlayStatus();
    }

    /**
     * 是否是播放
     */
    private boolean isPlaying() {
        //返回true，表示在播放
        return vv.isPlaying();
    }

    /**
     * 显示播放状态(这个是和Ui相关的)  播放图标
     */
    private void showPlayStatus() {
        ib_play.setImageResource(R.drawable.ic_video_play);
    }

    /**
     * 显示暂停状态(暂停图标)
     */
    private void showPauseStatus() {
        ib_play.setImageResource(R.drawable.ic_video_pause);
    }

    @Override
    protected void onDestroy() {
        //释放播放器内部资源
        //不然可能会有内存泄漏
        vv.stopPlayback();

        super.onDestroy();
    }

    /**
     * 播放准备完毕了
     */
    @Override
    public void onPrepared(MediaPlayer mp) {
        //获取视频时长
        int duration = mp.getDuration();

        //设置到进度条
        sb.setMax(duration);
        //设置到结束文本控件  duration:获取到的是毫秒，我们这里用formatMinuteSecond方法
        tv_end.setText(TimeUtil.formatMinuteSecond(duration));
    }
}
