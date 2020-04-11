package com.ixuea.courses.mymusic.view;

import android.content.Context;
import android.os.Build;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewConfiguration;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.ixuea.courses.mymusic.R;
import com.ixuea.courses.mymusic.domain.Song;
import com.ixuea.courses.mymusic.domain.lyric.Line;
import com.ixuea.courses.mymusic.domain.lyric.Lyric;
import com.ixuea.courses.mymusic.domain.lyric.LyricUtil;
import com.ixuea.courses.mymusic.listener.GlobalLyricListener;
import com.ixuea.courses.mymusic.listener.OnGlobalLyricDragListener;
import com.ixuea.courses.mymusic.util.LogUtil;
import com.ixuea.courses.mymusic.util.PreferenceUtil;

import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import me.shihao.library.XRadioGroup;

/**
 * 全局(桌面)歌词
 */
public class GlobalLyricView extends LinearLayout implements XRadioGroup.OnCheckedChangeListener {
    private static final String TAG = "GlobalLyricView";
    private static final int[] LYRIC_COLORS = new int[]{
            R.color.lyric_color0,
            R.color.lyric_color1,
            R.color.lyric_color2,
            R.color.lyric_color3,
            R.color.lyric_color4,
    };
    /**
     * 歌词颜色单选按钮Id 组
     */
    private static final int[] RADIO_BUTTONS = new int[]{
            R.id.rb_0,
            R.id.rb_1,
            R.id.rb_2,
            R.id.rb_3,
            R.id.rb_4,
    };
    /**
     * 全局歌词拖拽监听器
     */
    private OnGlobalLyricDragListener onGlobalLyricDragListener;
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

    /**
     * 自定义歌词样式容器
     */
    @BindView(R.id.ll_lyric_edit_container)
    View ll_lyric_edit_container;

    /**
     * 自定义歌词颜色单选按钮组
     */
    @BindView(R.id.rg)
    XRadioGroup rg;

    private GlobalLyricListener globalLyricListener;//全局歌词控件监听器
    private PreferenceUtil sp;
    private boolean isIntercept;//判断是否拦截该事件
    private float lastX;//按下X坐标
    private float lastY;//按下Y坐标
    private float touchSlop;//最博鳌滑动距离，目的是过滤不必要的滑动

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

        initDatum();
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

        //歌词颜色单选按钮组
        rg.setOnCheckedChangeListener(this);
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

        //获取View的配置
        ViewConfiguration viewConfiguration = ViewConfiguration.get(getContext());

        //获取最小滑动距离(getScaledTouchSlop:记住这样写就行了)
        touchSlop = viewConfiguration.getScaledTouchSlop();

    }

    /**
     * 初始化数据
     * <p>
     * 为啥在initDatum使用，因为关闭桌面控件（也就是本类对象），再次打开需要重新创建
     * 所以在initDatum获取保存的歌词大小
     */
    private void initDatum() {
        //最好是GlobalLyricManagerImpl中获取（不过我们还是在这里初始化）
        //这里是自定义View中，上下文的话传入: getContext()
        sp = PreferenceUtil.getInstance(getContext());

        //获取到偏好设置中到的字体大小(如果没有找到，有一个默认大小18（已转px）)
        int lyricTextSize = sp.getGlobalLyricTextSize();

        //设置到控件(setLyricTextSize:这个方法是我们之前定义的，里面已经设置大小到对象和设置画笔字体、调用了invalidate)
        llv1.setLyricTextSize(lyricTextSize);
        llv2.setLyricTextSize(lyricTextSize);

        //获取歌词颜色索引
        int lyricTextColorIndex = sp.getGlobalLyricTextColorIndex();
        //更新歌词颜色（updateLyricTextColor:这个方法是之前定义的）
        updateLyricTextColor(lyricTextColorIndex);

        //方法1
        //选中该颜色对应的单选按钮
        //这代码还可以在优化
//        switch(lyricTextColorIndex) {
//            case 0:
//                rg.check(R.id.rb_0);
//                break;
//            case 1:
//                rg.check(R.id.rb_1);
//                break;
//            case 2:
//                rg.check(R.id.rb_2);
//                break;
//            case 3:
//                rg.check(R.id.rb_3);
//                break;
//            case 4:
//                rg.check(R.id.rb_4);
//                break;
//        }

        //方法2 推荐
        int radioButtonId = RADIO_BUTTONS[lyricTextColorIndex];
        rg.check(radioButtonId);
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

        //隐藏编辑容器（就是控件最后一栏 RadioButton那父容器）
        ll_lyric_edit_container.setVisibility(GONE);
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

        if (ll_lyric_edit_container.getVisibility() == VISIBLE) {
            ll_lyric_edit_container.setVisibility(GONE);
        } else {
            ll_lyric_edit_container.setVisibility(VISIBLE);
        }
    }

    /**
     * 减小歌词字体大小按钮点击
     */
    @OnClick(R.id.iv_font_size_small)
    public void onLyricFontSizeDecrementClick() {
        //减小歌词字体大小
        int currentSize = llv1.decrementTextSize();
        //设置歌词控件字体大小（llv1 和llv2）
        setLyricTextSize(currentSize);

        //保存到配置文件
        //这个sp 放在View中可能不太好（不过我们还是写在这个view中）
        sp.setGlobalLyricTextSize(currentSize);
    }

    /**
     * 增大歌词字体大小按钮点击
     */
    @OnClick(R.id.iv_font_size_large)
    public void onLyricFontSizeIncrementClick() {
        //减小歌词字体大小
        int currentSize = llv1.IncrementTextSize();

        //设置个歌词控件字体大小
        setLyricTextSize(currentSize);

        //保存到配置文件
        //这个sp 放在View中可能不太好（不过我们还是写在这个view中）
        sp.setGlobalLyricTextSize(currentSize);
    }

    /**
     * 设置歌词文本大小（llv1 和llv2大小）
     *
     * @param currentSize
     */
    private void setLyricTextSize(int currentSize) {
        //设置到第二个歌词控件（这里设置到llv2，llv1的话，在调用decrementTextSize或者IncrementTextSize的时候已经刷新大小显示了）
        llv2.setLyricTextSize(currentSize);
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

    /**
     * 颜色单选按钮更改了
     *
     * @param group     前面通过id找到的XRadioGroup（布局中找到的）
     * @param checkedId 布局中通过id命名的那个命名id（选中id）
     */
    @Override
    public void onCheckedChanged(XRadioGroup group, int checkedId) {
        //通过tag解析出索引 （group.findViewById(checkedId):通过选中id找到这个控件）
        String tag = (String) group.findViewById(checkedId).getTag();

        //将tag解析为int
        int index = Integer.parseInt(tag);

        //更改歌词颜色(通过这个tag 更改歌词颜色)
        updateLyricTextColor(index);

        //保存到编号设置(保存tag int值到持久化设置)
        sp.setGlobalLyricTextColorIndex(index);
    }

    /**
     * 更改歌词颜色
     */
    private void updateLyricTextColor(int index) {
        //获取颜色
        int color = getResources().getColor(LYRIC_COLORS[index]);
        //设置颜色到歌词控件（这里只设置选中的那个颜色）
        llv1.setLyricSelectedTextColor(color);
    }

    /**
     * 用来判断是否拦截该事件
     */
    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        switch (ev.getActionMasked()) {
            case MotionEvent.ACTION_DOWN:
                //不拦截
                isIntercept = false;

                //获取第一次按下的坐标
                lastX = ev.getX();
                lastY = ev.getY();

                break;
            case MotionEvent.ACTION_MOVE:
                // 上下拉   下上拉（这个要绝对值） 所以最好加个返回值
                //touchSlop:就是系统的一个阀值
                if (Math.abs(getY() - lastY) > touchSlop) {
                    //如果在y轴
                    isIntercept = true;
                }
                break;

            case MotionEvent.ACTION_UP:
                //抬起不拦截
                isIntercept = false;
                break;
            default:
                break;
        }
        //返回是否拦截
        return isIntercept;
    }

    /**
     * 如果当前控件拦截了事件
     * 就会执行现在这个方法
     */
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getActionMasked()) {
            //这类监听不到ACTION_DOWN
            //因为onInterceptTouchEvent方法没有拦截
            //（因为如果拦截ACTION_DOWN事件，后续的move up事件都会交由该控件处理；那么子控件就接收不到事件了）
            case MotionEvent.ACTION_MOVE:
                //滑动的距离
                float distanceX = event.getX() - lastX;
                float distanceY = event.getY() - lastY;

                if (Math.abs(distanceX) > touchSlop) {
                    //要处理事件

                    //获取绝对坐标(包含状态栏)
                    //getRawY:这个是触摸的那个点 相对屏幕顶部的（相对根父容器）的Y距离
                    //getY：触摸的点击，相对本控件里面Y的距离
                    float rawY = event.getRawY();

                    //这里为啥是上一次的Y值，因为点击要控件移动，随拖拽而移动，里面的event.getY()是没有变的

                    float moveY = rawY - lastY;

                    //将拖拽的位置回调到外部
                    onGlobalLyricDragListener.onGlobalLyricDrag((int) moveY);

                    LogUtil.d(TAG, "onTouchEvent ACTION_MOVE:" + distanceY + ",rawY:" + rawY);
                }
                break;
            case MotionEvent.ACTION_UP:
                break;
        }
        return super.onTouchEvent(event);
    }

    /**
     * 设置全局歌词拖拽监听器
     */
    public void setOnGlobalLyricDragListener(OnGlobalLyricDragListener onGlobalLyricDragListener) {
        this.onGlobalLyricDragListener = onGlobalLyricDragListener;
    }
}
