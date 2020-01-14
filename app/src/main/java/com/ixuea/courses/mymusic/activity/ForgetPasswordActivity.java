package com.ixuea.courses.mymusic.activity;

import android.os.Bundle;
import android.os.CountDownTimer;
import android.widget.Button;
import android.widget.EditText;

import com.ixuea.courses.mymusic.R;
import com.ixuea.courses.mymusic.api.Api;
import com.ixuea.courses.mymusic.domain.User;
import com.ixuea.courses.mymusic.domain.response.BaseResponse;
import com.ixuea.courses.mymusic.listener.HttpObserver;
import com.ixuea.courses.mymusic.util.LogUtil;
import com.ixuea.courses.mymusic.util.StringUtil;
import com.ixuea.courses.mymusic.util.ToastUtil;

import org.apache.commons.lang3.StringUtils;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * 忘记密码界面
 */
public class ForgetPasswordActivity extends BaseLoginActivity {

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
    private String phone;//手机号
    private String email;//邮箱
    private CountDownTimer countDownTimer;


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

        startCountDown();
    }


    /**
     * 找回密码按钮点击了
     */
    @OnClick(R.id.bt_forget_password)
    public void onForgetPasswordClick() {
        LogUtil.d(TAG, "onForgetPasswordClick");

        //获取用户
        String username = et_username.getText().toString().trim();

        if (StringUtils.isBlank(username)) {
            ToastUtil.errorShortToast(R.string.enter_username);
            return;
        }

        //就是格式错误
        if (!(StringUtil.isPhone(username) || StringUtil.isEmail(username))) {
            ToastUtil.errorShortToast(R.string.error_username_format);
            return;
        }

        //验证码
        String code = et_code.getText().toString().trim();
        if (StringUtils.isBlank(code)) {
            ToastUtil.errorShortToast(R.string.enter_code);
            return;
        }

        if (!(StringUtil.isCode(code))) {
            ToastUtil.errorShortToast(R.string.error_code_format);
            return;
        }

        //获取密码
        String password = et_password.getText().toString().trim();
        if (StringUtils.isBlank(password)) {
            ToastUtil.errorShortToast(R.string.enter_password);
            return;
        }

        if (!(StringUtil.isPassword(password))) {//格式
            ToastUtil.errorShortToast(R.string.error_password_format);
            return;
        }

        //获取确认密码
        String confirmPassword = et_confirm_password.getText().toString().trim();
        if (StringUtils.isBlank(confirmPassword)) {
            ToastUtil.errorShortToast(R.string.error_confirm_password);
            return;
        }

        //判断密码和确认密码是否一样
        if (!password.equals(confirmPassword)) {
            //两次密码不正确
            ToastUtil.errorShortToast(R.string.error_confirm_password);
            return;
        }

//        String phone = null;
//        String email = null;

        //判断是手机号还是邮箱
        if (StringUtil.isPhone(username)) {
            //手机号
            phone = username;
        } else {
            //邮箱
            email = username;
        }

        //发送重置密码要求
        User data = new User();

        //将信息设置到对象上
        data.setPhone(phone);
        data.setEmail(email);
        data.setCode(code);
        data.setPassword(password);

        //调用接口
        Api.getInstance().resetPassword(data)
                .subscribe(new HttpObserver<BaseResponse>() {
                    @Override
                    public void onSucceeded(BaseResponse data) {
                        //重置成功

                        //调用父类登录方法
                        login(phone, email, password);
                    }
                });

    }

    /**
     * 开始倒计时
     * 现在没有保存退出的状态
     * 也就说，返回在进来就可以点击了
     */
    private void startCountDown() {
        //倒计时的总时间,间隔
        //单位是毫秒
        countDownTimer = new CountDownTimer(60000, 1000) {

            /**
             * 每间隔时间调用
             * @param millisUntilFinished 这个相当于剩余结束的时间
             */
            @Override
            public void onTick(long millisUntilFinished) {
                //%1：表示第一个参数  $d：表示是数字   连在一起到时候显示为59 58 ..50 49 这种形式 -->
                bt_send_code.setText(getString(R.string.count_second, millisUntilFinished / 1000));
            }

            /**
             * 倒计时完成时
             */

            @Override
            public void onFinish() {
                //结束完成后恢复原来文字和开启按钮
                bt_send_code.setText(R.string.send_code);
                bt_send_code.setEnabled(true);
            }
        };
        //启动
        countDownTimer.start();//记得开启
        //禁用按钮
        bt_send_code.setEnabled(false);

    }

    @Override
    protected void onDestroy() {//这步很重要，记得销毁，否则造成内存泄漏
        //销毁定时器
        if (countDownTimer != null) {
            countDownTimer.cancel();
            countDownTimer = null;
        }
        super.onDestroy();
    }
}
