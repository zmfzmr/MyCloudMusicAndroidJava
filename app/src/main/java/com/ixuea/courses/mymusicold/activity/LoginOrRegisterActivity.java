package com.ixuea.courses.mymusicold.activity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.ixuea.courses.mymusicold.R;

/**
 * 登录注册界面
 */
public class LoginOrRegisterActivity extends BaseCommonActivity implements View.OnClickListener {

    private static final String TAG = "LoginOrRegisterActivity";
    private Button bt_login;//登录按钮
    private Button bt_register;//登录按钮

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_or_register);
    }

    @Override
    protected void initView() {
        super.initView();
        //登录按钮
        bt_login = findViewById(R.id.bt_login);

        //注册按钮
        bt_register = findViewById(R.id.bt_register);
    }

    @Override
    protected void initListeners() {
        super.initListeners();
        bt_login.setOnClickListener(this);
        bt_register.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch(v.getId()) {
            case R.id.bt_login:
                Log.d(TAG, "onClick login");
                break;
            case R.id.bt_register:
                Log.d(TAG, "onClick register");
                break;
            default:
                break;
        }
    }
}
