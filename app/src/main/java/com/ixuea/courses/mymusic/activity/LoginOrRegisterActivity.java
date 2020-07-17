package com.ixuea.courses.mymusic.activity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;

import com.ixuea.courses.mymusic.AppContext;
import com.ixuea.courses.mymusic.MainActivity;
import com.ixuea.courses.mymusic.R;
import com.ixuea.courses.mymusic.api.Api;
import com.ixuea.courses.mymusic.domain.Session;
import com.ixuea.courses.mymusic.domain.User;
import com.ixuea.courses.mymusic.domain.event.LoginSuccessEvent;
import com.ixuea.courses.mymusic.domain.response.DetailResponse;
import com.ixuea.courses.mymusic.listener.HttpObserver;
import com.ixuea.courses.mymusic.util.AnalysisUtil;
import com.ixuea.courses.mymusic.util.Constant;
import com.ixuea.courses.mymusic.util.HandlerUtil;
import com.ixuea.courses.mymusic.util.LogUtil;
import com.ixuea.courses.mymusic.util.PushUtil;
import com.ixuea.courses.mymusic.util.ToastUtil;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.HashMap;

import butterknife.BindView;
import butterknife.OnClick;
import cn.sharesdk.framework.Platform;
import cn.sharesdk.framework.PlatformActionListener;
import cn.sharesdk.framework.PlatformDb;
import cn.sharesdk.framework.ShareSDK;
import cn.sharesdk.sina.weibo.SinaWeibo;
import cn.sharesdk.tencent.qq.QQ;

/**
 * 登录注册界面
 */
public class LoginOrRegisterActivity extends BaseCommonActivity  {

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
    /**
     * 第三方登录后用户信息
     */
    private User data;
    //    Button bt_register;//登录按钮

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
//        //注册按钮
//        bt_register = findViewById(R.id.bt_register);//这2个可以忽略了，因为上面已经找到实例了

    }

    @Override
    protected void initDatum() {
        super.initDatum();
        //它EventBus 怎么知道我这个界面activity有没有注册这些事件呢，
        //需要把当前界面注册到这些EventBus框架中
        //注册通知
        EventBus.getDefault().register(this);

        //思路：
        // 其实就是：AppContext(登录成功调用login 发送通知) -- > 当前界面 注册到EventBus-->通过监听onLoginSuccessEvent接收
        // 到发送的通知，然后关闭界面
    }

    @Override
    protected void initListeners() {
        super.initListeners();
//        bt_login.setOnClickListener(this);
//        bt_register.setOnClickListener(this);
    }

//    @Override
//    public void onClick(View v) {
//        switch (v.getId()) {
////            case R.id.bt_login:
////                Log.d(TAG, "onClick login");
////
////                startActivity(LoginActivity.class);
////                break;
//            case R.id.bt_register:
//                //注册界面
//                Log.d(TAG, "onClick register");
//
//                startActivity(RegisterActivity.class);
//                break;
//            default:
//                break;
//        }
//    }

    @OnClick(R.id.bt_login)
    public void onLoginClick() {
        Log.d(TAG, "onClick login");
        startActivity(LoginActivity.class);
    }

    @OnClick(R.id.bt_register)
    public void onRegisterClick() {
        //注册界面
        Log.d(TAG, "onClick register");
        //清楚第三方登录用户信息(因为点击的qq登录，data是不为null的)
        data = null;
        toRegister();
    }

    /**
     * 跳转到注册界面
     */
    private void toRegister() {
        Intent intent = new Intent(this, RegisterActivity.class);
        if (data != null) {
            //传递用户数据
            intent.putExtra(Constant.DATA, data);
        }
//        startActivity(RegisterActivity.class);
        startActivity(intent);
    }

    @OnClick(R.id.iv_qq)
    public void onQQLoginClick() {
        otherLogin(QQ.NAME);

    }

    @OnClick(R.id.iv_weibo)
    public void onWeiboLoginClick() {
        otherLogin(SinaWeibo.NAME);

    }

    private void otherLogin(String name) {
        //初始化具体的平台 Platform：翻译：平台  这里表示获取QQ这个平台的Platform对象
        Platform platform = ShareSDK.getPlatform(name);

        //设置false表示使用SSO(单点登录)授权方式
        platform.SSOSetting(false);

        //回调信息
        //可以在这里获取基本的授权返回的信息
        platform.setPlatformActionListener(new PlatformActionListener() {
            /**
             * 登录成功了
             *
             * @param platform Platform
             * @param i        i
             * @param hashMap  HashMap
             */
            @Override
            public void onComplete(Platform platform, int i, HashMap<String, Object> hashMap) {
                //登录成功了

                //就可以获取到昵称，头像，OpenId
                //该方法回调不是在主线程

                //从数据库获取信息
                //也可以通过user参数获取
                PlatformDb db = platform.getDb();//从平台那边获取到数据库，数据库返回信息

                data = new User();
                data.setNickname(db.getUserName());
                data.setAvatar(db.getUserIcon());//db.getUserIcon():获取用户头像

                if (QQ.NAME.equals(name)) {
                    data.setQq_id(db.getUserId());//db.getUserId() 平台用户的id
                } else {
                    data.setWeibo_id(db.getUserId());
                }

//                data.setQq_id("zmf1");//只要这个qq_id(OpenId不一样)，只要不一样就能注册成功

//                //跳转到注册界面（直接调用按钮点击事件方法）
//                toRegister();

                //继续登录
                continueLogin();
//                LogUtil.d(TAG, "other login success:" + nickname + ", " + avatar + ", " + openId + ", " + HandlerUtil.isMainThread());

            }

            /**
             * 登录失败了
             */
            @Override
            public void onError(Platform platform, int i, Throwable throwable) {
                LogUtil.d(TAG, "other login error:" + throwable.getLocalizedMessage() + "," + HandlerUtil.isMainThread());
            }

            /**
             * 取消登录了
             */
            @Override
            public void onCancel(Platform platform, int i) {
                LogUtil.d(TAG, "other login cancel:" + i + "," + HandlerUtil.isMainThread());
            }
        });

        //authorize与showUser单独调用一个即可
        //授权并获取用户信息
        platform.showUser(null);
    }

    /**
     * 继续登录
     * <p>
     * 第三方登录完成以后，如果判断第三方登录的QQ是否已经注册呢
     * <p>
     * <p>
     * 其实只要调用下登录方法，如果不可以登录，说明未注册，跳转注册界面；
     * 否则可以登录，直接通知关闭登录注册界面并跳转到主页
     */
    private void continueLogin() {
        //在这个User对象上添加个 pushId
        data.setPush(PushUtil.getPushId(getApplicationContext()));
        Api.getInstance()
                .login(data)
                .subscribe(new HttpObserver<DetailResponse<Session>>() {
                    /**
                     * 登录成功
                     */
                    @Override
                    public void onSucceeded(DetailResponse<Session> d) {
                        if (d != null) {

                            //统计登录事件

                            //因为这个外层方法login方法里面只有phone email这2个参数，传递这2个即可
                            AnalysisUtil.onLogin(getMainActivity(), true,
                                    AnalysisUtil.getMethod(null, null, data.getQq_id(), data.getWeibo_id()),
                                    null, null, data.getQq_id(), data.getWeibo_id());

                            AppContext.getInstance().login(d.getData());

                            ToastUtil.successShortToast(R.string.login_success);
                            startActivityAfterFinishThis(MainActivity.class);
                        }
                    }

                    /**
                     * 登录失败
                     */
                    @Override
                    public boolean onFailed(DetailResponse<Session> d, Throwable e) {

                        //统计登录事件

                        //因为这个外层方法login方法里面只有phone email这2个参数，传递这2个即可
                        AnalysisUtil.onLogin(getMainActivity(), false,
                                AnalysisUtil.getMethod(null, null, data.getQq_id(), data.getWeibo_id()),
                                null, null, data.getQq_id(), data.getWeibo_id());

                        if (d != null) {
                            //请求成功了
                            //并且服务端还返回了错误信息

                            //判断错误码
                            if (1010 == d.getStatus()) {
                                //用户未注册
                                //跳转到补充用户资料界面
                                toRegister();

                                //返回true表示我们处理了错误
                                return true;
                            }
                        }
                        //其他错误直接让父类处理
                        return super.onFailed(d, e);//默认是返回false的
                    }


                });


    }

    /**
     * 登录成功事件
     * 接受该事件的目的是关闭该界面
     *
     * @param event LoginSuccessEvent 也就是监听的事件（这里是一个空类，没有实现）
     *              <p>
     *              onLoginSuccessEvent：习惯在前面加个on 表示当事件发生时
     *
     * 因为我们实现了ActivityManager和ActivityLifeCycle 一起使用(实现了对所有界面Activity的监听)
     *              所以可以在另外一个地方(登录成功后),在调用下activityManager.clear();就可以移除前面的界面
     *
     *              如果这里的下面的代码要移除的话，就必须要把上面的 注册EventBus和取消EventBus 代码移除
     *              否则会报错
     */
    @Subscribe(threadMode = ThreadMode.MAIN)//线程模式 是在主线程（主线更新UI）
    public void onLoginSuccessEvent(LoginSuccessEvent event) {
        finish();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        //解除注册
        EventBus.getDefault().unregister(this);
    }
}
