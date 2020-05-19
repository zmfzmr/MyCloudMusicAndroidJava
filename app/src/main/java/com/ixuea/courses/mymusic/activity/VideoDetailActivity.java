package com.ixuea.courses.mymusic.activity;

import android.content.pm.ActivityInfo;
import android.content.res.Configuration;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.VideoView;

import com.github.jdsjlzx.recyclerview.LRecyclerView;
import com.github.jdsjlzx.recyclerview.LRecyclerViewAdapter;
import com.google.android.material.appbar.AppBarLayout;
import com.ixuea.courses.mymusic.R;
import com.ixuea.courses.mymusic.adapter.VideoDetailAdapter;
import com.ixuea.courses.mymusic.api.Api;
import com.ixuea.courses.mymusic.domain.Comment;
import com.ixuea.courses.mymusic.domain.Video;
import com.ixuea.courses.mymusic.domain.response.DetailResponse;
import com.ixuea.courses.mymusic.domain.response.ListResponse;
import com.ixuea.courses.mymusic.listener.HttpObserver;
import com.ixuea.courses.mymusic.util.ImageUtil;
import com.ixuea.courses.mymusic.util.LogUtil;
import com.ixuea.courses.mymusic.util.ResourceUtil;
import com.ixuea.courses.mymusic.util.ScreenUtil;
import com.ixuea.courses.mymusic.util.TimeUtil;
import com.zhy.view.flowlayout.FlowLayout;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import butterknife.BindView;
import butterknife.OnClick;

/**
 * 视频详情
 */
public class VideoDetailActivity extends BaseTitleActivity implements MediaPlayer.OnPreparedListener, SeekBar.OnSeekBarChangeListener {

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
    SeekBar sb_progress;

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
    private VideoDetailAdapter adapter;
    private LRecyclerViewAdapter adapterWrapper;//适配器包裹类
    private TextView tv_title;//标题
    private TextView tv_create_at;
    private TextView tv_count;//播放次数
    private FlowLayout fl;//标签流
    private ImageView iv_avatar;//头像
    private TextView tv_nickname;//昵称

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_video_detail);
    }

    @Override
    protected void initView() {
        super.initView();

        rv.setHasFixedSize(true);

        LinearLayoutManager layoutManager = new LinearLayoutManager(getMainActivity());
        rv.setLayoutManager(layoutManager);

        DividerItemDecoration decoration = new DividerItemDecoration(getMainActivity(), DividerItemDecoration.VERTICAL);
        rv.addItemDecoration(decoration);
    }

    @Override
    protected void initDatum() {
        super.initDatum();
        //获取id
        id = extraId();

        //创建适配器
        adapter = new VideoDetailAdapter(getMainActivity());
        //创建包裹适配器
        //这里实现头部是通过
        //LRecyclerView框架实现的
        //而发现界面的头部是通过BaseRecyclerViewAdapterHelper框架实现的
        //大家在学习的时候一定要搞明白
        adapterWrapper = new LRecyclerViewAdapter(adapter);

        //添加头部布局(注意：这里用的是包裹适配器)
        adapterWrapper.addHeaderView(createHeaderView());

        //设置适配器(设置包裹类的适配器)
        rv.setAdapter(adapterWrapper);

        //禁用下拉刷新
        rv.setPullRefreshEnabled(false);
        //禁用上拉加载更多
        rv.setLoadMoreEnabled(false);


        Api.getInstance()
                .videoDetail(id)
                .subscribe(new HttpObserver<DetailResponse<Video>>() {
                    @Override
                    public void onSucceeded(DetailResponse<Video> data) {
                        next(data.getData());
                    }
                });
    }

    /**
     * 创建头部布局
     *
     * 注意：头布局 查找到的控件，放到成员变量里面来，然后在外面设置数据
     */
    private View createHeaderView() {
        //把xml加载为view
        //注意: 第2个参数的写法 需要转换类型
        View view = getLayoutInflater().inflate(R.layout.header_video_detail, (ViewGroup) rv.getParent(), false);

        //标题
        tv_title = view.findViewById(R.id.tv_title);

        //发布时间
        tv_create_at = view.findViewById(R.id.tv_create_at);
        //播放量（播放次数）
        tv_count = view.findViewById(R.id.tv_count);

        //标签流
        fl = view.findViewById(R.id.fl);

        //头像
        iv_avatar = view.findViewById(R.id.iv_avatar);

        //昵称
        tv_nickname = view.findViewById(R.id.tv_nickname);

        return view;
    }

    @Override
    protected void initListeners() {
        super.initListeners();
        //设置准备播放监听器
        vv.setOnPreparedListener(this);

        //设置进度条监听器
        sb_progress.setOnSeekBarChangeListener(this);
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
        setTitle(data.getTitle());//Toolbar 标题
        tv_title.setText(data.getTitle());//头布局标题

        //发布时间 (转换成年月日 时分 这种格式)
        String createAt = TimeUtil.yyyyMMddHHmm(data.getCreated_at());
        tv_create_at.setText(getResources()
                .getString(R.string.video_create_at, createAt));

        //播放次数
        String clicksCount = getResources()
                .getString(R.string.video_clicks_count, data.getClicks_count());
        tv_count.setText(clicksCount);

        //头像
        ImageUtil.showAvatar(getMainActivity(), iv_avatar, data.getUser().getAvatar());

        //昵称
        tv_nickname.setText(data.getUser().getNickname());


        //请求相关视频数据
        List<Object> datum = new ArrayList<>();

        datum.add("相关推荐");

        //请求相关视频数据
        //因为服务端没有实现相关视频
        //所以就请求视频列表
        Api.getInstance()
                .videos()
                .subscribe(new HttpObserver<ListResponse<Video>>() {
                    @Override
                    public void onSucceeded(ListResponse<Video> data) {
                        //添加数据视频
                        datum.addAll(data.getData());
                        //添加标题
                        datum.add("精彩评论");

                        //请求精彩评论
                        //由于服务端没有实现与视频相关的评论
                        //所以这里直接请求评论列表
                        Api.getInstance()
                                .comments(new HashMap<>())
                                .subscribe(new HttpObserver<ListResponse<Comment>>() {
                                    @Override
                                    public void onSucceeded(ListResponse<Comment> data) {
                                        //添加到列表
                                        datum.addAll(data.getData());
                                        LogUtil.d(TAG, "comment:" + datum.size());

                                        //设置到适配器  注意：这里设置数据是设置到adapter，不是adapterWrapper
                                        adapter.setDatum(datum);
                                    }
                                });
                    }
                });



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
        tv_start.setText(TimeUtil.ms2ms(progress));

        //进度条
        sb_progress.setProgress(progress);
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
        sb_progress.setMax(duration);
        //设置到结束文本控件  duration:获取到的是毫秒，我们这里用formatMinuteSecond方法
        tv_end.setText(TimeUtil.ms2ms(duration));

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
        //获取当前屏幕方向(获取资源，获取配置，然后获取方向)
        int orientation = getResources().getConfiguration().orientation;

        //android.content.res : 包里面的Configuration
        //注意：这类是配置Configuration类 里面的常量ORIENTATION_PORTRAIT(竖屏标志)
        // ActivityInfo.SCREEN_ORIENTATION_PORTRAIT = 1
        // Configuration.ORIENTATION_PORTRAIT=1  所以说这2个写其中的一个都是可以的
        if (orientation == Configuration.ORIENTATION_PORTRAIT) {
            //竖屏

            //取消全屏(需要获取窗口对象，因为全屏是跟窗口相关的)
            getWindow().clearFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);

            //设置视频容器高度
            layoutParams.height = videoContainerHeight;

            //竖屏图标是 全屏图标
            ib_screen.setImageResource(R.drawable.ic_full_screen);
        } else {
            //横屏(全屏)

            //隐藏状态栏
            //注意：这里是2个参数的
            getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                    WindowManager.LayoutParams.FLAG_FULLSCREEN);

            //视频容器高度和父容器一样  ViewGroup.LayoutParams.MATCH_PARENT = -1(就是跟父容器一样)
            //而上面的videoContainerHeight 是一个具体的数值
            //layoutParams对应的布局RelativeLayout，它的父容器是外层的LinearLayout
            layoutParams.height = ViewGroup.LayoutParams.MATCH_PARENT;

            //更改全屏按钮图标
            //全屏 的话 图标 是缩小值竖屏的图标
            ib_screen.setImageResource(R.drawable.ic_normal_screen);
        }
    }

    //进度条相关回调

    /**
     * 进度条改变了
     */
    @Override
    public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
        //fromUser: 表示是否拖拽SeekBar
        if (fromUser) {
            //让视频调转到指定位置播放(也就是跳转到进度条的位置播放)
            vv.seekTo(seekBar.getProgress());
            if (!isPlaying()) {
                //如果暂停了

                //就继续播放
                resume();
                LogUtil.d(TAG, "onProgressChanged:" + seekBar.getProgress());
            }
        }
    }

    /**
     * 开始拖拽
     *
     * @param seekBar
     */
    @Override
    public void onStartTrackingTouch(SeekBar seekBar) {

    }

    /**
     * 结束拖拽
     *
     * @param seekBar
     */
    @Override
    public void onStopTrackingTouch(SeekBar seekBar) {

    }
    //end 进度条相关回调

    /**
     * 虽然说这个方法是，配置改变回调方法，但由于我们配置了屏幕相关属性，所以说屏幕发生了改变，也会回调。
     * <p>
     * 配置发生了改变调用
     * 具体是什么配置
     * 取决于清单文件中
     * configChanges属性配置
     * (例如：android:configChanges="orientation|keyboard|locale|screenSize|layoutDirection")
     *
     * @param newConfig
     */
    @Override
    public void onConfigurationChanged(@NonNull Configuration newConfig) {
        super.onConfigurationChanged(newConfig);

        //更新播放器布局
        updatePlayContainerLayout();
    }

    /**
     * 屏幕切换按钮点击了
     */
    @OnClick(R.id.ib_screen)
    public void onchangeFullScreenClick() {
        //更改屏幕方向
        changeOrientation();
    }

    /**
     * 更改屏幕方向
     * <p>
     * unspecified：不指定方向
     * 就会自动旋转
     * 或者说根据系统来
     *
     * 因为前面的 onConfigurationChanged 里面updatePlayContainerLayout设置了竖屏和横屏的布局改变
     * （和在清单文件中设置了属性）， 所以我们这里设置个方向就行了，只要屏幕发生了改变，
     *   就会回调 onConfigurationChanged方法进行布局的更新
     */
    private void changeOrientation() {
        if (isFullScreen()) {
            //是全屏

            //设置竖屏 （设置竖屏后，相当锁定为竖屏了，所以我们SCREEN_ORIENTATION_UNSPECIFIED，相当于不锁定）
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
            //注释理由跟下面到的一样
//            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_UNSPECIFIED);
        } else {
            //是竖屏

            //设置为全屏(横屏)  注意是 SCREEN_ORIENTATION_LANDSCAPE这个  并没有full
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
            //这里设置全屏后，可以先不要设置解锁屏幕 ，
            //否则onBackPressed->getRequestedOrientation 获取方向的值就会发生改变
            //后面会思考如何解决这个bug
//            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_UNSPECIFIED);
        }
    }

    /**
     * 是否是全屏(第二种方式)
     * <p>
     * 前面的第一种方法判断全屏
     * int orientation = getResources().getConfiguration().orientation;
     * if (orientation == Configuration.ORIENTATION_PORTRAIT) {
     */
    private boolean isFullScreen() {
        return getRequestedOrientation() == ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE;
    }

    /**
     * 点击物理返回键调用
     */
    @Override
    public void onBackPressed() {
        //如果是全屏
        //点击返回的时候
        //只是退出全屏
        if (isFullScreen()) {
            //是全屏

            //直接改变屏幕方向就行，不销毁
            changeOrientation();
        } else {
            //如果不是全屏，点击返回，调用父类方法(也就是直接销毁了Activity)
            super.onBackPressed();
        }
    }

    /**
     * 菜单点击了回调
     */
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                //Toolbar返回按钮点击
                //这个直接调用 物理返回键的方法就行了，原理都在那里

                //直接调用实体键返回按钮方法
                onBackPressed();
                return true;//记得返回true，否则还是会调用这么的父类方法
        }

        return super.onOptionsItemSelected(item);
    }
}
