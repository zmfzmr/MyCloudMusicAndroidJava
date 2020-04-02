package com.ixuea.courses.mymusic.view;

import android.content.Context;
import android.os.Build;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;

import com.ixuea.courses.mymusic.R;

import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;

/**
 * 全局(桌面)歌词
 */
public class GlobalLyricView extends LinearLayout {
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
    }

    /**
     * 设置背景
     */
    private void setBackground() {
        setBackgroundColor(getResources().getColor(R.color.global_lyric_background));
    }

}
