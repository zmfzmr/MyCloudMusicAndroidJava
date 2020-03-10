package com.ixuea.courses.mymusic.activity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.text.TextUtils;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

import com.ixuea.courses.mymusic.util.Constant;
import com.ixuea.courses.mymusic.util.ORMUtil;
import com.ixuea.courses.mymusic.util.PreferenceUtil;

import java.io.Serializable;

import butterknife.ButterKnife;

/**
 * 通用界面逻辑
 */
public class BaseCommonActivity extends BaseActivity {
    /**
     * 偏好设置工具实例
     * protected：这样子类才能访问
     */
    protected PreferenceUtil sp;
    //数据库对象（这个应该是数据库工具类）
    protected ORMUtil orm;

    @Override
    protected void initView() {
        super.initView();
        //初始化注解找控件
        //绑定方法框架
        if (isBindView()) {
            bindView();
        }
    }



    /**
     * 是否绑定View
     * protected:
     * @return
     */
    protected boolean isBindView() {
        return true;
    }

    /**
     * 绑定View
     */
    protected void bindView() {
        ButterKnife.bind(this);
    }

    @Override
    protected void initDatum() {
        super.initDatum();
        sp = PreferenceUtil.getInstance(getMainActivity());
        //数据库工具类 (在父初始化这个数据库工具类，这样每个子类都能使用了)
        orm = ORMUtil.getInstance(getApplicationContext());
    }

    /**
     * 全屏
     */
    protected void fullScreen() {
        //设置界面全屏

        //获取decorView
        View decorView = getWindow().getDecorView();

        //判断版本
        if (Build.VERSION.SDK_INT >= 16 && Build.VERSION.SDK_INT < 19) {
            //11~18版本
            decorView.setSystemUiVisibility(View.GONE);
        } else if(Build.VERSION.SDK_INT >= 19) {
            //19及以上版本
            //SYSTEM_UI_FLAG_HIDE_NAVIGATION:隐藏导航栏
            //SYSTEM_UI_FLAG_IMMERSIVE_STICKY:从状态栏下拉会半透明悬浮显示一会儿状态栏和导航栏
            //SYSTEM_UI_FLAG_FULLSCREEN:全屏
            int options = View.SYSTEM_UI_FLAG_HIDE_NAVIGATION |
                    View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY |
                    View.SYSTEM_UI_FLAG_FULLSCREEN;

            //设置到控件
            decorView.setSystemUiVisibility(options);
        }
    }

    /**
     * 隐藏状态栏
     */
    protected void hideStatusBar() {
        //隐藏状态栏
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
    }

    /**
     * 状态栏文字显示白色
     * 内容显示到状态栏下
     */
    protected void lightStatusBar() {
        // LOLLIPOP = 21 可以点击进去看里面的单词 比如22 23 其他不变的单词
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            //状态栏颜色设置为透明
            Window window = getWindow();

            //背景颜色透明
            window.setStatusBarColor(Color.TRANSPARENT);

            //去除半透明状态栏(如果有)
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);

            //SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN：让内容显示到状态栏
            //SYSTEM_UI_FLAG_LAYOUT_STABLE：状态栏文字显示白色
            //SYSTEM_UI_FLAG_LIGHT_STATUS_BAR：状态栏文字显示黑色
            window.getDecorView().setSystemUiVisibility(
                    View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                            | View.SYSTEM_UI_FLAG_LAYOUT_STABLE
            );
        }
    }

    /**
     * 启动界面
     * @param clazz
     */
    protected void startActivity(Class<?> clazz) {
        //创建Intent
        Intent intent = new Intent(getMainActivity(), clazz);
        startActivity(intent);
    }

    /**
     * 启动界面，可以传递一个字符串参数
     *
     * @param clazz Class
     * @param id    String(字符串id)
     */
    protected void startActivityExtraId(Class<?> clazz, String id) {
        //创建Intent
        Intent intent = new Intent(getMainActivity(), clazz);

        //传递数据
        if (!TextUtils.isEmpty(id)) {
            //不为空才传递
            intent.putExtra(Constant.ID, id);//这个id是字符串类型
        }

        //启动界面
        startActivity(intent);
    }

    protected void startActivityAfterFinishThis(Class<?> clazz) {
        startActivity(clazz);
        //关闭当前界面
        finish();
    }

    /**
     * 获取字符串类型Id(这个Id是字符串类型的)
     *
     * @return String
     */
    protected String extraId() {
        return extraString(Constant.ID);
    }

    /**
     * 获取字符串
     *
     * @param key Key
     * @return
     */
    protected String extraString(String key) {
        return getIntent().getStringExtra(key);
    }

    /**
     * 获取data对象
     *
     * @return
     */
    protected Serializable extraData() {
        return getIntent().getSerializableExtra(Constant.DATA);
    }

    protected BaseCommonActivity getMainActivity() {
        return this;
    }

}
