package com.ixuea.courses.mymusicold.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;

import com.ixuea.courses.mymusicold.MainActivity;
import com.ixuea.courses.mymusicold.R;
import com.ixuea.courses.mymusicold.adapter.GuideAdapter;
import com.ixuea.courses.mymusicold.fragment.GuideFragment;
import com.ixuea.courses.mymusicold.util.PreferenceUtil;

import java.util.ArrayList;
import java.util.List;

/**
 * 引导界面
 */
public class GuideActivity extends BaseCommonActivity implements View.OnClickListener {

    private static final String TAG = "GuideActivity";
    private Button bt_login_or_register;//登录注册
    private Button bt_enter;
    private ViewPager vp;
    private GuideAdapter adapter;



    /**
     * 当前界面创建的时候
     * 一定要注意：
     * 要用onCreate的方法参数是Bundle的这个方法
     * @param savedInstanceState
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //将activity_guide布局设置当前Activity的界面
        //很明显也设置为其他布局文件
        setContentView(R.layout.activity_guide);

    }

    @Override
    protected void initView() {
        super.initView();

        //隐藏状态栏
        hideStatusBar();

        vp = findViewById(R.id.vp);
        adapter = new GuideAdapter(getSupportFragmentManager());
        vp.setAdapter(adapter);
        //准备数据
        List<Integer> datum = new ArrayList<>();
        datum.add(R.drawable.guide1);
        datum.add(R.drawable.guide2);
        datum.add(R.drawable.guide3);
        datum.add(R.drawable.guide4);
        datum.add(R.drawable.guide5);
        adapter.setDatum(datum);//设置数据到适配器


        //找控件
        //登录注册按钮 (ctrl + alt + f创建实例变量（又局部变为全局变量）)
        bt_login_or_register = findViewById(R.id.bt_login_or_register);
        //立即体验按钮
        bt_enter = findViewById(R.id.bt_enter);

        //推荐使用里面的静态方法创建fragment：GuideFragment.newInstance()
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.vp, GuideFragment.newInstance(R.drawable.guide1))
                .commit();
    }

    @Override
    protected void initListeners() {
        super.initListeners();
        //设置点击事件
        bt_login_or_register.setOnClickListener(this);
        bt_enter.setOnClickListener(this);

    }

    /**
     *点击回调方法
     * @param v
     */
    @Override
    public void onClick(View v) {
        switch(v.getId()) {
            case R.id.bt_login_or_register:
                //注册登录按钮点击
                Log.d(TAG, "onClick login or register");
                startActivityAfterFinishThis(LoginOrRegisterActivity.class);

                setShowGuide();
                break;
            case R.id.bt_enter:
                //进入按钮
                Log.d(TAG, "onClick enter");
                startActivityAfterFinishThis(MainActivity.class);

                setShowGuide();
                break;
            default:
                break;
        }

    }

    private void setShowGuide() {
//        PreferenceUtil.getInstance(getMainActivity()).setShowGuide(false);
        sp.setShowGuide(false);
    }
}
