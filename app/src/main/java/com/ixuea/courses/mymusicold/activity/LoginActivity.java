package com.ixuea.courses.mymusicold.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import butterknife.BindView;
import butterknife.OnClick;

import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;


import com.ixuea.courses.mymusicold.R;
import com.ixuea.courses.mymusicold.util.LogUtil;

import org.apache.commons.lang3.StringUtils;

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

        //获取用户名
        String username = et_username.getText().toString().trim();
        //注意：这里没有用isEmpty,而用的是isBlank
        //isBlank：方法里面判断了 如果没有输入，或者输入的有空格 都会为true
        if (StringUtils.isBlank(username)) {//如果用户名没有输入(也就是为空)
            LogUtil.d(TAG, "onLoginClick user empty");
            Toast.makeText(getMainActivity(), R.string.enter_username, Toast.LENGTH_SHORT).show();
            return;
        }

        //获取密码
        String password = et_password.getText().toString().trim();
        //注意：也可以用 TextUtils.isEmpty(password) 这个就相当简单些，没有那么复杂了，可以点击进入查看
        if (TextUtils.isEmpty(password)) {
            //这里用的是w警告
            LogUtil.w(TAG, "onLoginClick password empty");
            Toast.makeText(getMainActivity(), R.string.enter_password, Toast.LENGTH_SHORT).show();
            return;
        }

        //TODO 调用登录方法


    }

    @OnClick(R.id.bt_forget_password)
    public void onForgetPasswordClick(Button view) {
        LogUtil.d(TAG, "onForgetPasswordClick: ");

    }

}
