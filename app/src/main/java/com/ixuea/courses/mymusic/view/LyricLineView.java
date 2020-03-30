package com.ixuea.courses.mymusic.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.Build;
import android.text.TextPaint;
import android.util.AttributeSet;
import android.view.View;

import com.ixuea.courses.mymusic.R;
import com.ixuea.courses.mymusic.domain.lyric.Line;
import com.ixuea.courses.mymusic.util.DensityUtil;
import com.ixuea.courses.mymusic.util.TextUtil;

import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;

/**
 * 一行歌词控件
 */
public class LyricLineView extends View {
    private static final String TAG = "LyricLineView";
    /**
     * 默认歌词大小
     * 单位：dp
     */
    private static final float DEFAULT_LYRIC_TEXT_SIZE = 16;//float类型

    /**
     * 默认歌词颜色
     * 颜色其实就是一个32位的值，用int就行
     */
    private static final int DEFAULT_LYRIC_TEXT_COLOR = Color.WHITE;
    private static final int DEFAULT_LYRIC_SELECTED_TEXT_COLOR = Color.parseColor("#dd0000");//默认歌词高亮颜色
    private static final String HINT_LYRIC_EMPTY = "我的云音乐，听你想听";//默认歌词显示的内容
    private Line data;//歌词行对象
    private boolean accurate;//是否精确到字歌词
    private boolean lineSelected;//是否选中（行选中）
    private float lyricTextSize;//float类型  歌词文字大小
    private int lyricTextColor;//歌词颜色
    private int lyricSelectedTextColor;//选中的高亮歌词颜色
    private Paint backgroundTextPaint;//默认歌词画笔
    private Paint foregroundTextPaint;//高亮画笔
    private Paint textPaint;
    private int lyricCurrentWordIndex;//当前播放时间点，在该行的第几个字（索引）
    private float lineLyricPlayedWidth;//当前行歌词已经唱过的宽度，也就是歌词高亮的宽度
    private float wordPlayedTime;//当前字，已经播放的时间


    public LyricLineView(Context context) {
        super(context);
        init(null);//没有AttributeSet 参数 传入null
    }

    public LyricLineView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init(attrs);
    }

    public LyricLineView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(attrs);
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public LyricLineView(Context context, @Nullable AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init(attrs);
    }

    /**
     * 自定义初始化方法
     * @param attrs
     */
    private void init(AttributeSet attrs) {
        //将16dp转换为px
        lyricTextSize = DensityUtil.dip2px(getContext(), DEFAULT_LYRIC_TEXT_SIZE);
        // lyricTextColor：变量    DEFAULT_LYRIC_TEXT_COLOR:常量   后面可能需要一些自定义配置 可能需要这样转换
        lyricTextColor = DEFAULT_LYRIC_TEXT_COLOR;

        lyricSelectedTextColor = DEFAULT_LYRIC_SELECTED_TEXT_COLOR;

        //解析自定义属性
        if (attrs != null) {//只要 这个lyric_line_view_attrs.xml 设置了LyricLineView的属性，那么这个AttributeSet就不等于null
            //获取属性值
            //R.styleable.LyricLineView:这个就是我们定义的 <declare-styleable name="LyricLineView">
            TypedArray typedArray = getContext().obtainStyledAttributes(attrs, R.styleable.LyricLineView);

            //这个属性名为
            //declare-styleable name名称+属性名
            //获取歌词大小

            //lyricTextSize:已经由dp 转换成了px，所以直接用这个就可以了
            lyricTextSize = typedArray.getDimension(R.styleable.LyricLineView_text_size, lyricTextSize);
            //歌词默认颜色
            lyricTextColor = typedArray.getColor(R.styleable.LyricLineView_text_color, lyricTextColor);

            //歌词高亮颜色
            //DEFAULT_LYRIC_TEXT_COLOR = lyricTextColor  2个属性都是相等的，因为前面赋值了
            lyricSelectedTextColor = typedArray.getColor(R.styleable.LyricLineView_selected_text_color, DEFAULT_LYRIC_TEXT_COLOR);

            //复用固定写法
            typedArray.recycle();//记得回收

        }

        //初始化画笔

        //默认歌词画笔
        //以下内容都是Java/Android绘图API知识
        backgroundTextPaint = new Paint();
        //设置图像防抖动
        backgroundTextPaint.setDither(true);
        //抗抗锯齿
        backgroundTextPaint.setAntiAlias(true);
        //设置文本大小
        backgroundTextPaint.setTextSize(lyricTextSize);
        //设置文本颜色
        backgroundTextPaint.setColor(lyricTextColor);

        //高亮歌词画笔(下面这些跟前面是一样的，不过画笔不同而已  和颜色不同)
        foregroundTextPaint = new Paint();
        foregroundTextPaint.setDither(true);
        foregroundTextPaint.setAntiAlias(true);
        foregroundTextPaint.setTextSize(lyricTextSize);
        foregroundTextPaint.setColor(lyricSelectedTextColor);//注意：这个颜色跟前面的不一样

        //测试画笔
        textPaint = new TextPaint();
        foregroundTextPaint.setDither(true);
        foregroundTextPaint.setAntiAlias(true);
        foregroundTextPaint.setColor(Color.BLUE);

        //只绘制边框不填充
        textPaint.setStyle(Paint.Style.STROKE);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
//        canvas.drawCircle(x /2, y /2, x /2, paint);
//        paint.setColor(Color.BLUE);
//        canvas.drawLine(x /2, y /8, x /2,0, paint);
//        canvas.save();//注意，这里调用了保存canvas的方法
//        for(inti =0; i <3; i++) {
//            canvas.rotate(30, x /2, y /2);canvas.drawLine(x /2, y /8, x /2,0, paint);
//        }
//
//
//        //画一条黄色粗线，便于区分canvas.drawLine(x/2,y/2,x/2,y/4, paint);
//        paint.setStrokeWidth(10);
//        paint.setColor(Color.YELLOW);
//        //观察运行效果图，最新绘制的黄线已经被旋转了90度。为什么呢？因为画布已经在之前旋转了90度了。
//        //下面在绘制黄线的代码之前，调用canvas.restore()恢复画布的状态
//
//        canvas.restore();//恢复画布状态paint.setStrokeWidth(10);paint.setColor(Color.YELLOW);canvas.drawLine(x/2,y/2,x/2,y/4, paint);
        //上面的代码知识测试 Canvas保存 和恢复状态
        //参考网址：https://www.jianshu.com/p/619618905432

        //保存状态(这个调用save之前什么都没改变，相当于这个画布是空的，保存这种没改变的状态)
        canvas.save();

        if (isEmptyLyric()) {
            //如果没有歌词
            //绘制默认文本
            drawDefaultText(canvas);


        } else {
            drawLyricText(canvas);//绘制真实文本
        }

        //恢复上下文（绘制完成后，恢复之前画布没有改变的状态）
        canvas.restore();
    }

    /**
     * 绘制真实文本
     */
    private void drawLyricText(Canvas canvas) {
        //data：Line对象
//        LogUtil.d(TAG,"drawLyricText:" + data.getData());

        //绘制背景文字

        //当前歌词的宽高
        //data（Line对象）:是从适配器LyricAdapter设置进来的
        //data.getData():获取整行歌词
        float textWidth = TextUtil.getTextWidth(foregroundTextPaint, data.getData());
        float textHeight = TextUtil.getTextHeight(foregroundTextPaint);
        //水平中心位置
        float centerX = getCenterX(textWidth);
        //TextView绘制值从baseLine开始
        //而不是左上角
        Paint.FontMetrics fontMetrics = foregroundTextPaint.getFontMetrics();
        float centerY = (getMeasuredHeight() - textHeight) / 2 + Math.abs(fontMetrics.top);
        //先绘制背景歌词
        canvas.drawText(data.getData(), centerX, centerY, backgroundTextPaint);

        if (lineSelected) {
            //选中了

            if (accurate) {//LyricAdapter那边传入进来的
                //精确到字
                if (lyricCurrentWordIndex == -1) { //注意：lyricCurrentWordIndex 和  lineLyricPlayedWidth是不同的
                    //该行已经播放完了
                    lineLyricPlayedWidth = textWidth;//行歌词播放宽度 == 整行歌词文本的宽度
                } else {
                    //逻辑：当前字前面的字的宽度 + 当前字播放宽度 = 播放总宽度

                    //字数组（行 字数组）
                    String[] lyricWords = data.getWords();
                    //字 时间 数组
                    Integer[] wordDurations = data.getWordDurations();
                    //lyricCurrentWordIndex:当前播放的最后一个字的索引
                    //获取 当前时间前面的文字
                    String beforeText = data.getData().substring(0, lyricCurrentWordIndex);
                    float beforeTextWidth = TextUtil.getTextWidth(foregroundTextPaint, beforeText);

                    //获取当前字（根据lyricCurrentWordIndex索引 在字 数组 中 lyricWords 找）
                    String currentWord = lyricWords[lyricCurrentWordIndex];
                    //当前位置文本的宽度
                    float currentWordTextWidth = TextUtil.getTextWidth(foregroundTextPaint, currentWord);

                    //这里就是 路程（当前文字宽度 currentWordTextWidth）  速度 时间的概念

                    //速度:currentWordTextWidth / wordDurations[lyricCurrentWordIndex] (路程 / 时间 = 速度)
                    //时间：wordPlayedTime

                    //currentWordTextWidth / wordDurations[lyricCurrentWordIndex] * wordPlayedTime:
                    //速度 * wordPlayedTime时间 ：这个字播放的距离（长度）

                    float currentWordPlayedWidth = currentWordTextWidth / wordDurations[lyricCurrentWordIndex] * wordPlayedTime;

                    //这一行已经演唱的宽度
                    lineLyricPlayedWidth = beforeTextWidth + currentWordPlayedWidth;
                }

                //绘制矩形宽高
//                canvas.drawRect(centerX, 0, centerX + lineLyricPlayedWidth, getMeasuredHeight(),textPaint);

                //裁剪矩形
                //用来绘制已经唱的歌词
                //这里：Top：0  bottom：控件的高  right：centerX + lineLyricPlayedWidth left：centerX（文字开始绘制的地方）
                canvas.clipRect(centerX, 0, centerX + lineLyricPlayedWidth, getMeasuredHeight());
            }

            //精确到行歌词(绘制高亮)（如果是精确的话，就会走上面的canvas.clipRect ）
            canvas.drawText(data.getData(), centerX, centerY, foregroundTextPaint);

        }
        //上面的是KSC歌词的写法，后面要根据这个写法实现（先实现背景歌词，后实现选中歌词）

//        //这种是LRC歌词的写法
//        if (lineSelected) {
//            //选中了
//
//            //精确到行歌词
//            canvas.drawText(data.getData(), centerX, centerY, backgroundTextPaint);
//        } else {
//            canvas.drawText(data.getData(),centerX,centerY,foregroundTextPaint);
//        }

    }

    /**
     * 歌词是否为空（也就是Line对象是否为null）
     * 这个data（Line对象，是LyricAdapter适配器那边传过来的）
     */
    private boolean isEmptyLyric() {
        return data == null;
    }

    /**
     * 绘制默认提示文本
     * <p>
     * 文本宽度：画笔 测量文本 就知道宽度
     * 文本高度：画笔
     */
    private void drawDefaultText(Canvas canvas) {

//        LogUtil.d(TAG,"drawDefaultText");

        //获取歌词内容的宽度
        float textWidth = TextUtil.getTextWidth(backgroundTextPaint, HINT_LYRIC_EMPTY);

        //获取歌词内容的高度(这个高度，只和字体和字体大小有关，和你的文本没有关系
        float textHeight = TextUtil.getTextHeight(backgroundTextPaint);
        //计算水平居中的坐标
        float centerX = getCenterX(textWidth);
        //这里用画笔获取这个这个字体测量标准（//获取和字体绘制相关测量类）
        Paint.FontMetrics fontMetrics = backgroundTextPaint.getFontMetrics();
        //计算垂直居中的坐标
        //Math.abs :绝对值  高的话还需要加上这点绝对值，才行
        float centerY = (getMeasuredHeight() - textHeight) / 2 + Math.abs(fontMetrics.top);
        //绘制文本
        //这里根据这个字体，在坐标位置话（用画笔在画布上画）
        canvas.drawText(HINT_LYRIC_EMPTY, centerX, centerY, backgroundTextPaint);
    }

    /**
     *
     */
    private float getCenterX(float textWidth) {
        return (getMeasuredWidth() - textWidth) / 2;
    }

    /**
     * 设置歌词行
     */
    public void setData(Line data) {
        this.data = data;
    }

    /**
     * 设置是否精确到字歌词
     */
    public void setAccurate(boolean accurate) {
        this.accurate = accurate;
    }

    /**
     * 设置是否选中（行选中）
     */
    public void setLineSelected(boolean selected) {
        this.lineSelected = selected;
    }

    /**
     * 设置当前字索引
     */
    public void setLyricCurrentWordIndex(int lyricCurrentWordIndex) {
        this.lyricCurrentWordIndex = lyricCurrentWordIndex;
    }

    /**
     * 设置当前字已经播放的时间
     */
    public void setWordPlayedTime(float wordPlayedTime) {
        this.wordPlayedTime = wordPlayedTime;
    }

    /**
     * 歌词进度
     */
    public void onProgress() {
        if (!isEmptyLyric()) {
            //有歌词就刷新控件
            invalidate();//会触发onDraw方法 16ms毫秒调用一次
        }
    }
}
