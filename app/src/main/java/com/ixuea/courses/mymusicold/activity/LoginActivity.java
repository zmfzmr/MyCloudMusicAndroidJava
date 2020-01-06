package com.ixuea.courses.mymusicold.activity;

import android.os.Bundle;
import android.os.Handler;
import android.widget.Button;
import android.widget.EditText;

import com.ixuea.courses.mymusicold.R;
import com.ixuea.courses.mymusicold.util.LoadingUtil;
import com.ixuea.courses.mymusicold.util.LogUtil;

import butterknife.BindView;
import butterknife.OnClick;

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
        //测试网络请求

//        OkHttpClient.Builder okhttpClientBuilder = new OkHttpClient.Builder();
//        //构建者模式
//        //初始化Retrofit
//        Retrofit retrofit = new Retrofit.Builder()
//                //让Retrofit使用okhttp
//                .client(okhttpClientBuilder.build())
//                //api地址
//                .baseUrl(Constant.ENDPOINT)//这里使用的是地址的公共前缀
//                //适配Rxjava（就是所返回的对象以Rxjava这种方式来工作（比如我们使用了Observable这种方式，接口Service查看））
//                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
//                //使用gson解析json
//                //包括请求参数和响应
//                // （比如使用Retrofit请求框架请求数据，发送对象，也会转换成json（使用gson转换））
//                .addConverterFactory(GsonConverterFactory.create())
//                //创建Retrofit
//                .build();
//
//        //创建Service
//        Service service = retrofit.create(Service.class);

        //请求歌单详情
//        service.sheetDetail("1")
//        Api.getInstance().sheetDetail("1")
//                .subscribeOn(Schedulers.io())//在子线程执行
//                .observeOn(AndroidSchedulers.mainThread())//在主线程观察（操作UI在主线程）
//                //接口方法里面对应的对象：SheetDetailWrapper
//                .subscribe(new Observer<SheetDetailWrapper>() {//订阅回来的数据
//                    @Override
//                    public void onSubscribe(Disposable d) {
//                        Log.d(TAG, "onSubscribe: ");
//                    }
//
//                    /**
//                     * 请求成功
//                     *
//                     * @param sheetDetailWrapper 解析回来的对象
//                     */
//                    @Override
//                    public void onNext(SheetDetailWrapper sheetDetailWrapper) {
//                        LogUtil.d(TAG, "onNext:" + sheetDetailWrapper.getData().getTitle());
//
//                    }
//
//                    /**
//                     * 请求失败
//                     *
//                     * @param e Error
//                     */
//                    @Override
//                    public void onError(Throwable e) {
//                        Log.d(TAG, "onError: ");
////                        e.printStackTrace();
////                        LogUtil.d(TAG,"request sheet detail failed:" + e.getLocalizedMessage());
//
//                        //判断错误类型
//                        if (e instanceof UnknownHostException) {
//                            ToastUtil.errorShortToast(R.string.error_network_unknown_host);
//                        } else if (e instanceof ConnectException) {
//                            ToastUtil.errorShortToast(R.string.error_network_connect);
//                        } else if (e instanceof SocketTimeoutException) {
//                            ToastUtil.errorShortToast(R.string.error_network_timeout);
//                        } else if (e instanceof HttpException) {
//                            HttpException exception = (HttpException) e;
//                            int code = exception.code();
//                            if (code == 401) {
//                                ToastUtil.errorShortToast(R.string.error_network_not_auth);
//                            } else if (code == 403) {
//                                ToastUtil.errorShortToast(R.string.error_network_not_permission);
//                            } else if (code == 404) {
//                                ToastUtil.errorShortToast(R.string.error_network_not_found);
//                            } else if (code >= 500) {
//                                ToastUtil.errorShortToast(R.string.error_network_server);
//                            } else {
//                                ToastUtil.errorShortToast(R.string.error_network_unknown);
//                            }
//                        } else {
//                            ToastUtil.errorShortToast(R.string.error_network_unknown);
//                        }
//                    }
//
//                    /**
//                     * 请求结束
//                     */
//                    @Override
//                    public void onComplete() {
//
//                    }
//                });

        //测试加载提示框
        LoadingUtil.showLoading(getMainActivity());

        //3秒中隐藏加载提示框
        //因显示后无法点击后面的按钮（也就是当前页面点击的3s后关闭，在另一个页面关闭麻烦）
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                LoadingUtil.hideLoading();
            }
        }, 3000);

//        //获取用户名
//        String username = et_username.getText().toString().trim();
//        //注意：这里没有用isEmpty,而用的是isBlank
//        //isBlank：方法里面判断了 如果没有输入，或者输入的有空格 都会为true
//        if (StringUtils.isBlank(username)) {//如果用户名没有输入(也就是为空)
//            LogUtil.d(TAG, "onLoginClick user empty");
////            Toast.makeText(getMainActivity(), R.string.enter_username, Toast.LENGTH_SHORT).show();
////            Toasty.error(getMainActivity(),R.string.enter_username,Toasty.LENGTH_SHORT).show();
//            ToastUtil.errorShortToast(R.string.enter_username);
//            return;
//        }
//
//        //格式判断（正则表达式判断是否匹配）
//        //如果是用户名
//        //不是手机号也不是邮箱
//        //就是格式错误
//        //其实就是点击的时候，把值传入过去，看是否匹配正则表达式，匹配就返回true,否则就返回false
//        if (!(StringUtil.isPhone(username) || StringUtil.isEmail(username))) {
//            ToastUtil.errorShortToast(R.string.error_username_format);
//            return;
//        }
//
//        //获取密码
//        String password = et_password.getText().toString().trim();
//        //注意：也可以用 TextUtils.isEmpty(password) 这个就相当简单些，没有那么复杂了，可以点击进入查看
//        if (TextUtils.isEmpty(password)) {
//            //这里用的是w警告
//            LogUtil.w(TAG, "onLoginClick password empty");
////            Toast.makeText(getMainActivity(), R.string.enter_password, Toast.LENGTH_SHORT).show();
//            ToastUtil.errorShortToast(R.string.enter_password);
//            return;
//        }
//
//        //判断密码格式
//        if (!StringUtil.isPassword(password)) {
//            ToastUtil.errorShortToast(R.string.error_password_format);
//            return;
//        }
//
//        //TODO 调用登录方法
//        ToastUtil.successLongToast(R.string.login_success);


    }

    @OnClick(R.id.bt_forget_password)
    public void onForgetPasswordClick(Button view) {
        LogUtil.d(TAG, "onForgetPasswordClick: ");

    }

}
