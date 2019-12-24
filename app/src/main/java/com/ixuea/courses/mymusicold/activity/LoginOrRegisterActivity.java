package com.ixuea.courses.mymusicold.activity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.ixuea.courses.mymusicold.R;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * 登录注册界面
 */
public class LoginOrRegisterActivity extends BaseCommonActivity implements View.OnClickListener {

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
    Button bt_register;//登录按钮

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
        //注册按钮
        bt_register = findViewById(R.id.bt_register);//这2个可以忽略了，因为上面已经找到实例了

    }

    @Override
    protected void initListeners() {
        super.initListeners();
//        bt_login.setOnClickListener(this);
        bt_register.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
//            case R.id.bt_login:
//                Log.d(TAG, "onClick login");
//
//                startActivity(LoginActivity.class);
//                break;
            case R.id.bt_register:
                //注册界面
                Log.d(TAG, "onClick register");

                startActivity(RegisterActivity.class);
                break;
            default:
                break;
        }
    }

    @OnClick(R.id.bt_login)
    public void onLoginClick() {
        startActivity(LoginActivity.class);
    }
}
