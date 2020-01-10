package com.ixuea.courses.mymusicold.activity;

import android.os.Bundle;
import android.widget.EditText;

import com.ixuea.courses.mymusicold.R;
import com.ixuea.courses.mymusicold.api.Api;
import com.ixuea.courses.mymusicold.domain.BaseModel;
import com.ixuea.courses.mymusicold.domain.User;
import com.ixuea.courses.mymusicold.domain.response.DetailResponse;
import com.ixuea.courses.mymusicold.listener.HttpObserver;
import com.ixuea.courses.mymusicold.util.LogUtil;
import com.ixuea.courses.mymusicold.util.StringUtil;
import com.ixuea.courses.mymusicold.util.ToastUtil;

import org.apache.commons.lang3.StringUtils;

import butterknife.BindView;
import butterknife.OnClick;

public class RegisterActivity extends BaseTitleActivity {

    private static final String TAG = "RegisterActivity";
    @BindView(R.id.et_nickname)//昵称输入框
            EditText et_nickname;

    @BindView(R.id.et_phone)//手机
            EditText et_phone;

    @BindView(R.id.et_email)//邮箱
            EditText et_email;

    @BindView(R.id.et_password)//密码
            EditText et_password;

    @BindView(R.id.et_confirm_password)//确认密码输入框
            EditText et_confirm_password;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
    }

    /**
     * 注册按钮点击了
     */
    @OnClick(R.id.bt_register)
    public void onRegisterClick() {
        //获取昵称
        String nickname = et_nickname.getText().toString().trim();
        if (StringUtils.isBlank(nickname)) {
            ToastUtil.errorShortToast(R.string.enter_nickname);
            return;
        }

        //判断昵称格式
        if (!StringUtil.isNickName(nickname)) {
            ToastUtil.errorShortToast(R.string.error_nickname_format);
            return;
        }

        //获取手机号
        String phone = et_phone.getText().toString().trim();
        if (StringUtils.isBlank(nickname)) {
            ToastUtil.errorShortToast(R.string.enter_phone);
            return;
        }

        //判断手机号格式
        if (!StringUtil.isPhone(phone)) {
            ToastUtil.errorShortToast(R.string.error_phone_format);
            return;
        }

        //获取邮箱
        String email = et_email.getText().toString().trim();
        if (StringUtils.isBlank(email)) {
            ToastUtil.errorShortToast(R.string.enter_email);
            return;
        }

        //判断邮箱格式
        if (!StringUtil.isEmail(email)) {
            ToastUtil.errorShortToast(R.string.error_email_format);
            return;
        }

        //获取密码
        String password = et_password.getText().toString().trim();
        if (StringUtils.isBlank(password)) {
            ToastUtil.errorShortToast(R.string.enter_password);
            return;
        }

        //判断密码格式
        if (!StringUtil.isPassword(password)) {
            ToastUtil.errorShortToast(R.string.error_password_format);
            return;
        }

        //确认密码
        String confirmPassword = et_confirm_password.getText().toString().trim();
        if (StringUtils.isBlank(confirmPassword)) {
            ToastUtil.errorShortToast(R.string.enter_confirm_password);
            return;
        }

        //确定密码格式
        if (!StringUtil.isPassword(password)) {
            ToastUtil.errorShortToast(R.string.error_confirm_password_format);
            return;
        }

        //判断密码和确定密码是否一致
        if (!password.equals(confirmPassword)) {
            ToastUtil.errorShortToast(R.string.error_confirm_password);
            return;
        }

        //调用注册接口完成注册
        User data = new User();
        //将信息设置到对象上
        data.setNickname(nickname);
        data.setPhone(phone);
        data.setEmail(email);
        data.setPassword(password);
        //将信息设置到
        Api.getInstance().register(data)
                .subscribe(new HttpObserver<DetailResponse<BaseModel>>() {
                    @Override
                    public void onSucceeded(DetailResponse<BaseModel> data) {
                        LogUtil.d(TAG, "register success: " + data.getData().getId());

                        //TODO 自动登录

                    }
                });

    }


}
