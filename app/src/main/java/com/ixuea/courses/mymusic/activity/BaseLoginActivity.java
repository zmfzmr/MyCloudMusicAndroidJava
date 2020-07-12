package com.ixuea.courses.mymusic.activity;

import android.util.Log;

import com.ixuea.courses.mymusic.AppContext;
import com.ixuea.courses.mymusic.MainActivity;
import com.ixuea.courses.mymusic.R;
import com.ixuea.courses.mymusic.api.Api;
import com.ixuea.courses.mymusic.domain.Session;
import com.ixuea.courses.mymusic.domain.User;
import com.ixuea.courses.mymusic.domain.response.DetailResponse;
import com.ixuea.courses.mymusic.listener.HttpObserver;
import com.ixuea.courses.mymusic.util.AnalysisUtil;
import com.ixuea.courses.mymusic.util.ToastUtil;

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
                        //统计登录事件
                        //因为这个外层方法login方法里面只有phone email这2个参数，传递这2个即可
                        AnalysisUtil.onLogin(getMainActivity(), true,
                                AnalysisUtil.getMethod(phone, email, null, null),
                                phone, email, null, null);

//                        toLogin(data);
                        toLogin(data.getData());

                    }

                    /**
                     * 登录失败
                     *
                     * @param data
                     * @param e
                     * @return
                     */
                    @Override
                    public boolean onFailed(DetailResponse<Session> data, Throwable e) {

                        //统计登录事件

                        //因为这个外层方法login方法里面只有phone email这2个参数，传递这2个即可
                        AnalysisUtil.onLogin(getMainActivity(), true,
                                AnalysisUtil.getMethod(phone, email, null, null),
                                phone, email, null, null);

                        //统计登录失败事件
                        //像无网络
                        //用户名密码格式不正确这些就不统计了
                        //当然真实项目中可能也需要统计

                        //让框架自动处理错误(可以 查看 HttpObserver.java 中查看)
                        return false;
                    }
                });
    }

    /**
     * 登录成功
     */
//    private void toLogin(DetailResponse<Session> data) {
    private void toLogin(Session data) {
        //把登录成功的事件通知到AppContext
        //PreferenceUtil sp 是父类BaseCommonActivity初始化的
//        AppContext.getInstance().login(sp, data.getData());
        AppContext.getInstance().login(data);

        ToastUtil.successShortToast(R.string.login_success);

        //关闭当前界面并启动主界面
        startActivityAfterFinishThis(MainActivity.class);
    }
}
