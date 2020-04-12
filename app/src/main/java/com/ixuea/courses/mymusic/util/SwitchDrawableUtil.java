package com.ixuea.courses.mymusic.util;

import android.animation.ValueAnimator;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.LayerDrawable;
import android.view.animation.AccelerateInterpolator;

/**
 * 使用动画方式切换Drawable工具类
 */
public class SwitchDrawableUtil {
    /**
     * 背景（原来的图片）索引
     */
    private static final int INDEX_BACKGROUND = 0;
    /**
     * 前景（新图片）索引
     */
    private static final int INDEX_FOREGROUND = 1;
    private static final int DURATION_ANIMATION = 300;//动画执行时间 单位：毫秒
    private static final String TAG = "SwitchDrawableUtil";
    private final LayerDrawable layerDrawable;//多层drawable(图层)
    private ValueAnimator animator;//属性动画者

    /**
     * 构造方法
     *
     * @param backgroundDrawable 背景（原来的图片）
     * @param foregroundDrawable 前景 （新的图片）
     */
    public SwitchDrawableUtil(Drawable backgroundDrawable, Drawable foregroundDrawable) {
        ////创建数据
        Drawable[] drawables = new Drawable[2];
        //如果在API为28的版本上崩溃
        //为啥崩溃呢，因为传入进来的iv_background.getDrawable()中的 这个ImageView控件没有设置图片
        //backgroundDrawable就会在低版本的手机上为null，但是如果设置图片的话，这个drawable就会变形

        //解决办法里面2个都为foregroundDrawable（背景drawable设置为：前景drawable）
        //不是null的话，就保持原来的drawable
        if (backgroundDrawable == null) {
            //添加drawable(这个前景的drawable 当做背景的drawable也是没有问题的)
            drawables[INDEX_BACKGROUND] = foregroundDrawable;
            drawables[INDEX_FOREGROUND] = foregroundDrawable;
        } else {
            //添加drawable
            drawables[INDEX_BACKGROUND] = backgroundDrawable;
            drawables[INDEX_FOREGROUND] = foregroundDrawable;
        }

        //创建多层drawable
        layerDrawable = new LayerDrawable(drawables);
        //初始化动画
        initAnimation();
    }

    /**
     * 初始化动画
     */
    private void initAnimation() {
        //属性动画
        //从0变为255
        animator = ValueAnimator.ofFloat(0f, 255.0f);
        //设置执行时间
        animator.setDuration(DURATION_ANIMATION);
        //插值器
        //加速插值器
        animator.setInterpolator(new AccelerateInterpolator());
        //添加监听器
        animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            /**
             * 每次回调方法
             */
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                //获取动画值
                float foregroundAlpha = (float) animation.getAnimatedValue();
                LogUtil.d(TAG, "" + foregroundAlpha);
                //layerDrawable中的第二个drawable  设置这个alpha值（把监听到的0 到 255 变换的）alpha值设置进去
                //前景慢慢变的不透明
                layerDrawable.getDrawable(INDEX_FOREGROUND).setAlpha((int) foregroundAlpha);
            }
        });
    }

    /**
     * 获取多层drawable
     */
    public Drawable getDrawable() {
        return layerDrawable;
    }

    /**
     * 开始执行动画
     */
    public void start() {
        animator.start();
    }

    //属性动画：在Android中来说，我把这个控件的位置把它移动了，位置旋转了，大小缩放了
    //值的属性动画，我们给它一个时间，给它一个值，它会去变化我们这个值，并不会针对这个控件
    //我们这里用到的是值属性动画

    //0 变化255这样一个动画（以动画的方式更改这样一个透明度 （0：完全透明 255：完全不透明）
    // 从完全透明 到完全不透明 （淡入动画）

    //我设置了0~255，和时间，那么它内部就会计算，我1毫秒改增加多少值，这就是这个动画的原理

    //差值器，其实就控制这里面是怎样变换的；我给你一个值，通过一定的数学公式，变换出一个值
    //如果不使用差值器，可以理解为默认就是个匀速动画

    //添加监听器（动画执行的过程中，希望监听值的一个变化，从而做出什么处理）
}
