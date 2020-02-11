package com.ixuea.courses.mymusic.view;

import android.content.Context;
import android.os.Build;
import android.util.AttributeSet;
import android.widget.LinearLayout;

import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;

/**
 * 矩形LinearLayout
 */
public class SquareLinearLayout extends LinearLayout {
    /**
     * 构造方法
     *
     * @param context Context
     */
    public SquareLinearLayout(Context context) {
        super(context);
    }

    public SquareLinearLayout(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public SquareLinearLayout(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    /**
     * 构造方法
     */
    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public SquareLinearLayout(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        //设置系统测量好的尺寸
        //这里是根据父类里的规则存储测量到的宽高
        // （如果没有调用setMeasuredDimension这个方法，那么下面getMeasuredWidth应该是为null的）
        //也可以用第二种方式（把super.onMeasure()放在前面，后面再获取宽高的话，应该没有问题）
        setMeasuredDimension(getDefaultSize(0, widthMeasureSpec), getDefaultSize(0, heightMeasureSpec));

        //创建一个测量规则
        int width = getMeasuredWidth();
        widthMeasureSpec = heightMeasureSpec = MeasureSpec.makeMeasureSpec(width, MeasureSpec.EXACTLY);
        //让父类设置尺寸
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }
}
