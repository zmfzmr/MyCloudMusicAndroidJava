package com.ixuea.courses.mymusic.activity;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;

import com.ixuea.courses.mymusic.R;
import com.ixuea.courses.mymusic.api.Api;
import com.ixuea.courses.mymusic.domain.BaseModel;
import com.ixuea.courses.mymusic.domain.User;
import com.ixuea.courses.mymusic.domain.response.DetailResponse;
import com.ixuea.courses.mymusic.listener.HttpObserver;
import com.ixuea.courses.mymusic.util.LogUtil;
import com.ixuea.courses.mymusic.util.StringUtil;
import com.ixuea.courses.mymusic.util.ToastUtil;

import org.apache.commons.lang3.StringUtils;
import org.jetbrains.annotations.NotNull;

import butterknife.BindView;
import butterknife.OnClick;

public class RegisterActivity extends BaseLoginActivity {

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

    @BindView(R.id.bt_register)//注册按钮
            Button bt_register;
    /**
     * 第三方登录完成后的用户信息
     */
    private User data;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
    }

    @Override
    protected void initDatum() {
        super.initDatum();
        //获取传递的用户信息
        data = (User) extraData();
        if (data != null && (
                StringUtils.isNotBlank(data.getQq_id())
                        || StringUtils.isNotBlank(data.getWeibo_id())
        )) {
            //第三方登录跳转过来的

            //设置标题
            setTitle(R.string.enter_register2);

            //将昵称显示到输入框
            et_nickname.setText(data.getNickname());

            //更改注册按钮标题
            bt_register.setText(R.string.complete_register);

        }
//        else {
//            //不是第三方登录
//            //不用做任何处理
//
//        }
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
        User data = getData();
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
                        //注册后自动登录(调用父类自动登录方法)
                        login(phone, email, password);
                    }
                });

    }

    /**
     * 获取用户对象
     * 如果传递了用户对象直接复用
     * 否则创建一个新对象
     * <p>
     * QQ登录后传递过来的对象包含了昵称
     * 关于上面 再次data.setNickname(nickname);
     * 设置昵称的原因是，用户可能再次修改昵称，需要获取输入框的新昵称后注册
     */
    @NotNull
    private User getData() {
        if (data == null) {
            data = new User();
        }
        return data;
    }

    /**
     * 用户协议点击
     */
    @OnClick(R.id.bt_agreement)
    public void onAgreementClick() {
        //使用webView界面打开用户协议
        //这里打开的是爱学啊官网用户协议
        //大家可以根据实际情况修改
        WebViewActivity.start(getMainActivity(), "用户协议", "http://www.ixuea.com/posts/1");
    }

}
