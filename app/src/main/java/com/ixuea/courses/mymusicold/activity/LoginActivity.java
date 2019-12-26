package com.ixuea.courses.mymusicold.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import butterknife.BindView;
import butterknife.OnClick;

import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;


import com.ixuea.courses.mymusicold.R;
import com.ixuea.courses.mymusicold.util.LogUtil;

/**
 * 登录界面
 */
public class LoginActivity extends BaseTitleActivity {

    private static final String TAG = "LoginActivity";
    @BindView(R.id.et_username)//用户名输入框
    EditText et_username;

    @BindView(R.id.et_password)//密码输入框
    EditText et_password;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
    }

    /**
     * 登录按钮点击了
     *
     * 如果想要获取到这个按钮，可以直接传入参数Button view
     */
    @OnClick(R.id.bt_login)
    public void onLoginClick(Button view) {
        LogUtil.d(TAG, "onLoginClick: ");

    }

    @OnClick(R.id.bt_forget_password)
    public void onForgetPasswordClick(Button view) {
        LogUtil.d(TAG, "onForgetPasswordClick: ");

    }

}
