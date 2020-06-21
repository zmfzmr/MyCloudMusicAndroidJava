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

import com.fcfrt.netbua.FcfrtNetStatusBus;
import com.fcfrt.netbua.annotation.FcfrtNetSubscribe;
import com.fcfrt.netbua.type.Mode;
import com.fcfrt.netbua.type.NetType;
import com.fcfrt.netbua.utils.FcfrtNetworkUtils;
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
import com.ixuea.courses.mymusic.util.ToastUtil;
import com.zhy.view.flowlayout.FlowLayout;
import com.zhy.view.flowlayout.TagAdapter;
import com.zhy.view.flowlayout.TagFlowLayout;

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
public class VideoDetailActivity extends BaseTitleActivity implements MediaPlayer.OnPreparedListener, SeekBar.OnSeekBarChangeListener, MediaPlayer.OnBufferingUpdateListener, MediaPlayer.OnErrorListener, MediaPlayer.OnCompletionListener {

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
    private TagFlowLayout fl;//标签流
    private ImageView iv_avatar;//头像
    private TextView tv_nickname;//昵称
    private int duration;//视频总时长
    private boolean isCompletetion;//是否播放完毕了
    private boolean isResume;//切换WIFI 是否自动播放

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_video_detail);
    }

    /**
     * 界面显示了
     */
    @Override
    protected void onResume() {
        super.onResume();
        //开启屏幕常亮
        //真实项目中
        //可能会做的更完善
        //例如：只有正在播放才常亮 (系统自带的VideoView 会在播放的时候设置 不息屏)
        //而我们设置的是在Activity中，只要这个界面显示，那么就不息屏
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON,
                WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);

        //注册网络状态监听
        FcfrtNetStatusBus.getInstance().register(this);
    }

    /**
     * 界面暂停了
     * <p>
     * 为啥不写在onDestroy中，因为在Activity 跳转到另一个界面的时候，并没有调用onDestroy方法(导致没有销毁，还是常亮)
     * 所以在onPause 中调用
     */
    @Override
    protected void onPause() {
        super.onPause();

        //清除，只需要一个参数就可以了
        getWindow().clearFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);

        //取消网络状态监听
        FcfrtNetStatusBus.getInstance().unregister(this);
    }

    /**
     * 当前移动网络连接可用时
     * <p>
     * 模式mode       Mode.MOBILE_CONNECT  移动网络连接可用  connect:连接
     *
     * 思路： 切换移动网络后，暂停播放； 如要开启播放，获取设置界面的sp偏好设置值
     */
    @FcfrtNetSubscribe(mode = Mode.MOBILE_CONNECT)
    public void onMobileConnected() {
        LogUtil.d(TAG, "net onMobileConnected");

        if (!isPlaying()) {
            //不是播放(也就是已经暂停或者没有播放；所以就没必要调用下面到的暂停方法了，多此一举)
            return;
        }

        if (sp.isMobilePay()) {
            //设置开启了移动网络播放，所以就不暂停播放了（就是开启了移动网络播放，就不走下面的暂停方法）
            return;
        }

        //因为onMobileConnected 方法的调用是在子线程，所以要切换到主线程
        vv.post(new Runnable() {
            @Override
            public void run() {
                //切换WIFI 自动播放(也就是移动网络暂停了，下次切换WIFI后，可以自动播放)
                //取消WIFI 切换到移动后；当我们打开WIFI后，希望它能自动播放，所以把整个变量置为true
                isResume = true;

                pause();
            }
        });
    }

    /**
     * 当前WIFI网络连接 可用时
     */
    @FcfrtNetSubscribe(mode = Mode.WIFI_CONNECT)
    public void onWiFiConnected() {
        LogUtil.d(TAG, "net onWiFiConnected");

        if (isResume && !isPlaying()) {

            //注意： 这里要条件满足才切换到主线程; 防止条件还没有满足就切换主线程

            //因为onWiFiConnected 方法的调用是在子线程，所以要切换到主线程
            vv.post(new Runnable() {
                @Override
                public void run() {
                    //自动播放为true，并且没有在播放; 那么就继续播放
                    resume();

                    //记得置为false(否则一切WIFI 就会调用resume)
                    isResume = false;
                }
            });
        }

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
     * <p>
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

        //设置错误监听器(当然给MediaPlayer设置也是一样的)
        vv.setOnErrorListener(this);

        //设置播放完毕了监听器
        vv.setOnCompletionListener(this);

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

//        //开始播放(记得开始播放)
//        vv.start();

        //这里换成resume()播放
        resume();

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

        //显示视频标签
        //由于服务端没有实现视频标签功能
        //所以这里就写死几个标签
        //目的是讲解如果使用流式标签布局
        ArrayList<String> tags = new ArrayList<>();
        tags.add("爱学啊");
        tags.add("测试");
        tags.add("测试标签1");
        tags.add("标签1");
        tags.add("测试标签1标签1");
        tags.add("标签1");

        //TagAdapter 构造方法里面要传入数据集合tags   类型是String类型
        fl.setAdapter(new TagAdapter<String>(tags) {
            /**
             * @param parent
             * @param position
             * @param data     集合中的单个数据
             * @return
             */
            @Override
            public View getView(FlowLayout parent, int position, String data) {
                //将xml加载为view
//                View view = View.inflate(getMainActivity(), R.layout.item_tag, null);
//                //找标题控件
//                TextView tv_title = view.findViewById(R.id.tv_title);
//                //设置值  集合中的单个数据
//                tv_title.setText(data);

                //方式2
                //因为加载回来的就是一个TextView，所以直接转换就可以了
                TextView view = (TextView) View.inflate(getMainActivity(), R.layout.item_tag, null);
                //设置值
                view.setText(data);

                //返回view
                return view;
            }
        });

        //设置标签
        fl.setOnTagClickListener(new TagFlowLayout.OnTagClickListener() {
            /**
             * tag 点击回调
             *
             * @param view
             * @param position
             * @param parent
             * @return
             */
            @Override
            public boolean onTagClick(View view, int position, FlowLayout parent) {
                //获取点击的标签文本(tags集合数据的 单个对象)
                String tag = tags.get(position);
                LogUtil.d(TAG, "onTagClick: " + tag);
                return true;//true 表示我们处理了这个事件
            }
        });


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

            if (!isCompletetion) {
                //没有播放完毕才显示

                //播放控制容器
                control_container.setVisibility(View.VISIBLE);
            }

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

        //注意：这里获取网络类型是 FcfrtNetworkUtils
        NetType netType = FcfrtNetworkUtils.getNetType();
        if (netType == NetType.MOBILE) {
            if (!sp.isMobilePay()) {
                //移动网络下，如果没有开启移动网络，就弹出提示让用户去设置
                ToastUtil.errorShortToast(R.string.hint_network_play_disable);
                //注意：因为没有设置中开启移动网络开关，所以无法播放(调用return中断下面的vv.start()终止操作)
                return;
            }
        }
        //否则(这个else不用写，那么移动网络下设置中开启WIFI开关和WIFi下,都会调用下面的方法来播放)

        vv.start();

        //显示暂停状态
        showPauseStatus();

        //清除播放完毕了（就是播放完毕了，点击播放按钮，这个isCompletetion开始是为false，只要播放完毕才是true）
        isCompletetion = false;

        //隐藏提示信息
        tv_info.setVisibility(View.GONE);
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

        //也有可能这时长为0，可能
        if (duration > 0) {
            //进度条
            //计算进度百分比
            //因为第二个进度也用max
            //而我们这里第二个进度是百分比
            //max默认为100  （也就是说统一用百分比，前面的那个setMax 注释掉了）
            int percent = progress * 100 / duration;
            sb_progress.setProgress(percent);

            /*
                progress:458
                百分比：1-->progress * 100: 45800-->duration-->38430

                progress:4639
                百分比：12-->progress * 100: 463900-->duration-->38430

                总之记住： 百分比  =  进度 * 100 / 时长   就行了
             */
            LogUtil.d(TAG, "progress:" + progress);
            LogUtil.d(TAG, "百分比：" + percent + "-->progress * 100: " + progress * 100 + "-->duration-->" + duration);
        }
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
     * <p>
     * 动态计算高度：是因为在真实项目中，有些视频宽高可能不一样，而我们现在的视频容器高度固定为210DP，
     * 如果视频不是现在这个比例，那么上下可能会有黑边
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
        duration = mp.getDuration();

        //设置到进度条
        //这里我们注释了，因为我们默认的是Max就是100 ，我们设置0~100的百分比 默认就会把进度和缓存进度给展示出来了
//        sb_progress.setMax(duration);


        //设置到结束文本控件  duration:获取到的是毫秒，我们这里用formatMinuteSecond方法
        tv_end.setText(TimeUtil.ms2ms(duration));

        //显示播放进度
        startShowProgress();

        //设置缓冲进度监听器
        //注意：MediaPlayer  是方法参数里面的mp    buttering 缓冲
        mp.setOnBufferingUpdateListener(this);
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
     *
     * 为什么拖拽到第10秒 其他等时间， 总是差那么几秒，并不是我们拖拽的不精确
     * 是我们视频里面有个关键帧的作用，也就是视频只能拖拽到关键帧的位置，并不是我拖拽到第10秒，绝对就是第10秒
     * 也有可能少2秒
     */
    @Override
    public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
        //fromUser: 表示是否拖拽SeekBar
        if (fromUser) {
            //让视频调转到指定位置播放(也就是跳转到进度条的位置播放)
            /*
              比如进度是 0~100

                getProgress = 30

                因为我们缓冲的时候缓冲了百分比，所以这里需要换算下，
                这个进度30（总的进度是100）主要换算成时长才可以显示出来

                那么 30/100 * duration

                比如duration = 1000
                那么就是： 30/100 * 1000 =  300
                解析： 30占100 的 0.3  0.3*100  就是这个进度总的时长

                但是如果直接先除的话，没有小数了，所以把duration放到前面来
                30 * duration / 100  结果还是一样的

             */
            //progress 方法参数里面的
            progress = seekBar.getProgress() * duration / 100;
            vv.seekTo(progress);
//            vv.seekTo(seekBar.getProgress());
            if (!isPlaying()) {
                //如果暂停了

                //就继续播放
                resume();
//                LogUtil.d(TAG, "onProgressChanged:"" + seekBar.getProgress());
                LogUtil.d(TAG, "onProgressChanged:" + progress);
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
     * <p>
     * 因为前面的 onConfigurationChanged 里面updatePlayContainerLayout设置了竖屏和横屏的布局改变
     * （和在清单文件中设置了属性）， 所以我们这里设置个方向就行了，只要屏幕发生了改变，
     * 就会回调 onConfigurationChanged方法进行布局的更新
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

    /**
     * 缓冲进度更新了
     *
     * @param mp
     * @param percent 缓冲百分比
     */
    @Override
    public void onBufferingUpdate(MediaPlayer mp, int percent) {
        LogUtil.d(TAG, "onBufferingUpdate: " + percent);

        //设置第二个进度
        sb_progress.setSecondaryProgress(percent);
    }

    /**
     * 播放失败了
     *
     * @param mp    MediaPlayer
     * @param what  错误类型
     * @param extra 错误扩展信息
     * @return true:表示处理了错误
     * <p>
     * 怎么测试?
     * 在vv.setVideoURI中打一个断点，进入到这段的时候，把网关了（跳出飞行模式）
     * 然后，放开断点，这时候VideoView加载的uri 网址 没有网会导致连接超时，自然会回调onError方法
     */
    @Override
    public boolean onError(MediaPlayer mp, int what, int extra) {
        LogUtil.d(TAG, "onError:" + what + "," + extra);

        switch (extra) {
            case MediaPlayer.MEDIA_ERROR_TIMED_OUT:
                //超时了
                showMessage(R.string.play_failed_time_out);
                break;
            default:
                //注意：这里是播放失败，上面的是播放超时
                showMessage(R.string.play_failed);
                break;
        }
        //显示播放状态（也就是把播放按钮 变成 播放的图标  （因为出错了，显示播放图标））
        showPlayStatus();

        return true;
    }

    /**
     * 显示提示
     * (错误和播放完毕 都会调用这个方法)
     *
     * @param resourceId 资源id
     */
    private void showMessage(int resourceId) {
        tv_info.setVisibility(View.VISIBLE);
        tv_info.setText(resourceId);//设置错误提示信息
    }

    /**
     * 播放完毕
     */
    @Override
    public void onCompletion(MediaPlayer mp) {
        //设置播放完毕了
        isCompletetion = true;

        //显示提示
        showMessage(R.string.play_completetion);

        //显示播放状态(显示播放图标)
        showPlayStatus();

        //显示播放按钮(1.因为前面的 点击触摸层显示后，会调用startShowProgress 开始倒计时，接收会隐藏这个播放按钮
        //            2. 或者页面已经来就会调用startShowProgress 开始倒计时隐藏播放按钮)
        ib_play.setVisibility(View.VISIBLE);
    }
}
