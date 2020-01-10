package com.ixuea.courses.mymusicold.activity;

import android.util.Log;

import com.ixuea.courses.mymusicold.AppContext;
import com.ixuea.courses.mymusicold.MainActivity;
import com.ixuea.courses.mymusicold.R;
import com.ixuea.courses.mymusicold.api.Api;
import com.ixuea.courses.mymusicold.domain.Session;
import com.ixuea.courses.mymusicold.domain.User;
import com.ixuea.courses.mymusicold.domain.response.DetailResponse;
import com.ixuea.courses.mymusicold.listener.HttpObserver;
import com.ixuea.courses.mymusicold.util.ToastUtil;

/**
 * 登录相关功能
 */
public class BaseLoginActivity extends BaseTitleActivity {
    private static final String TAG = "BaseLoginActivity";

    /**
     * 登录
     *
     * @param phone    手机号
     * @param email    邮箱
     * @param password 密码
     */
    protected void login(String phone, String email, String password) {
        User user = new User();
        //这里虽然同时传递了手机号和邮箱
        //但服务端登录的时候有先后顺序(也就是说phone或者email其中的一个有值才会传递)
        user.setPhone(phone);
        user.setEmail(email);
        user.setPassword(password);
        Api.getInstance()
                .login(user)
                .subscribe(new HttpObserver<DetailResponse<Session>>() {
                    @Override
                    public void onSucceeded(DetailResponse<Session> data) {
                        Log.d(TAG, "onLongClick,onSucceeded: " + data.getData());

                        //把登录成功的事件通知到AppContext
                        //PreferenceUtil sp 是父类BaseCommonActivity初始化的
                        AppContext.getInstance().login(sp, data.getData());

                        ToastUtil.successLongToast(R.string.login_success);

                        //关闭当前界面并启动主界面
                        startActivityAfterFinishThis(MainActivity.class);
                    }
                });
    }
}
