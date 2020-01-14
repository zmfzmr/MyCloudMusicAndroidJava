package com.ixuea.courses.mymusic.activity;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;

import com.ixuea.courses.mymusic.R;
import com.ixuea.courses.mymusic.util.LogUtil;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * 忘记密码界面
 */
public class ForgetPasswordActivity extends BaseTitleActivity {

    private static final String TAG = "ForgetPasswordActivity";
    @BindView(R.id.et_username)//用户名输入框
            EditText et_username;

    @BindView(R.id.bt_send_code)//验证码按钮
            Button bt_send_code;

    @BindView(R.id.et_code)//验证码输入框
            EditText et_code;

    @BindView(R.id.et_password)//密码输入框
            EditText et_password;

    @BindView(R.id.et_confirm_password)//确认密码输入框
            EditText et_confirm_password;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forget_password);
    }

    /**
     * 发送验证码按钮点击了
     */
    @OnClick(R.id.bt_send_code)
    public void onSendCodeClick() {
        LogUtil.d(TAG, "onSendCodeClick");
    }

    /**
     * 找回密码按钮点击了
     */
    @OnClick(R.id.bt_forget_password)
    public void onForgetPasswordClick() {
        LogUtil.d(TAG, "onForgetPasswordClick");
    }
}
