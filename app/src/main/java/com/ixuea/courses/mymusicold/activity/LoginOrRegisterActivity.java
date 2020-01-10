package com.ixuea.courses.mymusicold.activity;

import android.os.Bundle;
import android.util.Log;
import android.widget.Button;

import com.ixuea.courses.mymusicold.R;
import com.ixuea.courses.mymusicold.domain.event.LoginSuccessEvent;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * 登录注册界面
 */
public class LoginOrRegisterActivity extends BaseCommonActivity  {

    private static final String TAG = "LoginOrRegisterActivity";
//    private Button bt_login;//登录按钮
//    private Button bt_register;//登录按钮

    /**
     * 登录按钮
     * <p>
     * 字段不能申明为private
     */
    @BindView(R.id.bt_login)
    Button bt_login;//登录按钮
//    Button bt_register;//登录按钮

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_or_register);
    }

    @Override
    protected void initView() {
        super.initView();
//        //初始化ButterKnife
//        ButterKnife.bind(this);

        //显示亮色状态
        lightStatusBar();

//        //登录按钮
//        bt_login = findViewById(R.id.bt_login);
//
//        //注册按钮
//        bt_register = findViewById(R.id.bt_register);//这2个可以忽略了，因为上面已经找到实例了

    }

    @Override
    protected void initDatum() {
        super.initDatum();
        //它EventBus 怎么知道我这个界面activity有没有注册这些事件呢，
        //需要把当前界面注册到这些EventBus框架中
        //注册通知
        EventBus.getDefault().register(this);

        //思路：
        // 其实就是：AppContext(登录成功调用login 发送通知) -- > 当前界面 注册到EventBus-->通过监听onLoginSuccessEvent接收
        // 到发送的通知，然后关闭界面
    }

    @Override
    protected void initListeners() {
        super.initListeners();
//        bt_login.setOnClickListener(this);
//        bt_register.setOnClickListener(this);
    }

//    @Override
//    public void onClick(View v) {
//        switch (v.getId()) {
////            case R.id.bt_login:
////                Log.d(TAG, "onClick login");
////
////                startActivity(LoginActivity.class);
////                break;
//            case R.id.bt_register:
//                //注册界面
//                Log.d(TAG, "onClick register");
//
//                startActivity(RegisterActivity.class);
//                break;
//            default:
//                break;
//        }
//    }

    @OnClick(R.id.bt_login)
    public void onLoginClick() {
        Log.d(TAG, "onClick login");
        startActivity(LoginActivity.class);
    }

    @OnClick(R.id.bt_register)
    public void onRegisterClick() {
        //注册界面
        Log.d(TAG, "onClick register");
        startActivity(RegisterActivity.class);
    }

    /**
     * 登录成功事件
     * 接受该事件的目的是关闭该界面
     *
     * @param event LoginSuccessEvent 也就是监听的事件（这里是一个空类，没有实现）
     *              <p>
     *              onLoginSuccessEvent：习惯在前面加个on 表示当事件发生时
     */
    @Subscribe(threadMode = ThreadMode.MAIN)//线程模式 是在主线程（主线更新UI）
    public void onLoginSuccessEvent(LoginSuccessEvent event) {
        finish();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        //解除注册
        EventBus.getDefault().unregister(this);
    }
}
