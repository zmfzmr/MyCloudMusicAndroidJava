package com.ixuea.courses.mymusic.activity;

import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.View;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
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
import com.ixuea.courses.mymusic.util.ScreenUtil;
import com.ixuea.courses.mymusic.util.TimeUtil;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * 视频详情
 */
public class VideoDetailActivity extends BaseTitleActivity implements MediaPlayer.OnPreparedListener {

    private static final String TAG = "VideoDetailActivity";

    /**
     * 视频容器
     */
    @BindView(R.id.video_container)
    RelativeLayout video_container;

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

    private Video data;//当前视频对象
    CountDownTimer countDownTimer;//进度倒计时任务
    private int videoContainerHeight;//视频容器的高度

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
        this.data = data;
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
     * 视频播放容器点击事件
     * video_touch_container: video上的触摸层
     */
    @OnClick(R.id.video_touch_container)
    public void onTouchContainerClick() {
        if (control_container.getVisibility() == View.VISIBLE) {
            //显示了

            //隐藏  翻译：Controller 控制者
            hideController();
        } else {
            //没有显示

            //显示
            //标题
            abl.setVisibility(View.VISIBLE);
            //播放按钮
            ib_play.setVisibility(View.VISIBLE);
            //播放控制容器
            control_container.setVisibility(View.VISIBLE);

            //显示播放进度
            startShowProgress();
        }
    }

    /**
     * 隐藏播放控制器
     */
    private void hideController() {
        //标题
        abl.setVisibility(View.GONE);
        //播放按钮
        ib_play.setVisibility(View.GONE);
        //播放控制容器
        control_container.setVisibility(View.GONE);

        //停止进度显示
        stopShowProgress();
    }

    /**
     * 停止进度显示
     */
    private void stopShowProgress() {
        //调用取消定时器的方法就行了
        cancelTask();
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
     * 开始显示进度
     * <p>
     * 1.倒计时完成 2.点击隐藏容器  2个 都会调用hideController(这个是上面的onTouchContainerClick 执行的)
     */
    private void startShowProgress() {
        //取消原来的定时器
        cancelTask();

        //倒计时的总时间，间隔
        countDownTimer = new CountDownTimer(5000, 100) {
            /**
             * 每次间隔调用(每秒调用一次)
             */
            @Override
            public void onTick(long millisUntilFinished) {
                //显示进度 vv.getCurrentPosition(): 可以获取当前的播放进度
                showProgress(vv.getCurrentPosition());
            }

            /**
             * 倒计时完成了
             */
            @Override
            public void onFinish() {
                //隐藏播放控制器
                hideController();
            }
        };

        //启动倒计时
        countDownTimer.start();

    }

    /**
     * 显示播放进度
     */
    private void showProgress(int progress) {
        //开始位置
        tv_start.setText(TimeUtil.formatMinuteSecond(progress));

        //进度条
        sb.setProgress(progress);
    }

    /**
     * 取消原来的定时器
     */
    private void cancelTask() {
        if (countDownTimer != null) {
            countDownTimer.cancel();
            countDownTimer = null;
        }
    }

    /**
     * 播放准备完毕了
     *
     * 动态计算高度：是因为在真实项目中，有些视频宽高可能不一样，而我们现在的视频容器高度固定为210DP，
     *              如果视频不是现在这个比例，那么上下可能会有黑边
     * 这个动态计算高度解决了 黑边的问题(如果视频本身就有黑边的话，是解决不了了)
     */
    @Override
    public void onPrepared(MediaPlayer mp) {
        //重新计算视频的高
        //这样没有黑边
        //目的是有更好的体验

        //获取视频宽度
        int videoWidth = mp.getVideoWidth();

        //获取视频高度
        int videoHeight = mp.getVideoHeight();

        //获取屏幕的宽度
        int screenWidth = ScreenUtil.getScreenWidth(getMainActivity());

        //注意: 这里除以：可能会有浮点数，所以
        double scale = screenWidth * 1.0 / videoWidth;

        //计算出视频容器的高度
        videoContainerHeight = (int) (videoHeight * scale);

        //更新播放容器高度
        updatePlayContainerLayout();

        //获取视频时长
        int duration = mp.getDuration();

        //设置到进度条
        sb.setMax(duration);
        //设置到结束文本控件  duration:获取到的是毫秒，我们这里用formatMinuteSecond方法
        tv_end.setText(TimeUtil.formatMinuteSecond(duration));

        //显示播放进度
        startShowProgress();
    }

    /**
     * 更新播放容器高度(更新布局)
     */
    private void updatePlayContainerLayout() {
        //获取视频容器布局参数
        //注意： video_container 是RelativeLayout布局， 必须要是它的外层布局才可以的
        LinearLayout.LayoutParams layoutParams = (LinearLayout.LayoutParams) video_container.getLayoutParams();
        //设置视频容器高度
        layoutParams.height = videoContainerHeight;
    }
}
