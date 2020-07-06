package com.ixuea.courses.mymusic.util;

import android.text.TextUtils;

import com.ixuea.courses.mymusic.AppContext;
import com.ixuea.courses.mymusic.R;
import com.ixuea.courses.mymusic.domain.response.BaseResponse;
import com.ixuea.courses.mymusic.exception.ResponseDecryptException;
import com.ixuea.courses.mymusic.exception.ResponseSignException;

import java.net.ConnectException;
import java.net.SocketTimeoutException;
import java.net.UnknownHostException;

import retrofit2.HttpException;
import retrofit2.Response;

/**
 * 网络请求相关辅助方法
 */
public class HttpUtil {
    /**
     * 网络请求错误处理
     *
     * @param data  Object
     * @param error Throwable
     *              这个static后面的<T>是必须要的，否则参数那里会找不到这个泛型T
     *              也可以不用泛型T，直接用Object
     */
//    public static <T> void handlerRequest(T data, Throwable error) {
    public static void handlerRequest(Object data, Throwable error) {
        if (error != null) {
            //判断错误类型
            if (error instanceof UnknownHostException) {
                ToastUtil.errorShortToast(R.string.error_network_unknown_host);
            } else if (error instanceof ConnectException) {
                ToastUtil.errorShortToast(R.string.error_network_connect);
            } else if (error instanceof SocketTimeoutException) {
                ToastUtil.errorShortToast(R.string.error_network_timeout);
            } else if (error instanceof HttpException) {
                HttpException exception = (HttpException) error;
                int code = exception.code();
                handleHttpError(code);//调用重构方法处理错误（401 403 404 500和其他错误）
            } else if (error instanceof ResponseSignException) {
                //真实项目中一般很少会提示这么明显
                //因为错误提示的越详细
                //对攻击者也就越详细了
                ToastUtil.errorShortToast(R.string.error_response_sign);
            } else if (error instanceof ResponseDecryptException) {
                //真实项目中一般很少会提示这么明显
                //因为错误提示的越详细
                //对攻击者也就越详细了
                ToastUtil.errorShortToast(R.string.error_response_decript);
            } else {
                ToastUtil.errorShortToast(R.string.error_network_unknown);
            }
        } else {//error为null（这个时候是走onNext-->else-->requestErrorHandler）
            if (data instanceof Response) {
                //retrofit里面的对象

                //获取响应对象
                Response response = (Response) data;

                //获取响应码
                int code = response.code();

                //判断响应码
                if (code >= 200 && code <= 299) {
                    //网络请求正常

                } else {//网络请求失败
                    handleHttpError(code);
                }
            } else if (data instanceof BaseResponse) { //(登录失败的这种错误)
                //判断具体的业务请求是否成功
                BaseResponse response = (BaseResponse) data;
                if (TextUtils.isEmpty(response.getMessage())) {
                    //没有错误提示信息（message可能没有错误提示信息） (未知错误，请稍后再试！)
                    ToastUtil.errorShortToast(R.string.error_network_unknown);
                } else {//message不为空
                    ToastUtil.errorShortToast(response.getMessage());
                }
            }
        }
    }

    /**
     * 处理Http错误
     *
     * @param code
     */
    private static void handleHttpError(int code) {
        if (code == 401) {
            ToastUtil.errorShortToast(R.string.error_network_not_auth);

            //登录信息过期调用logout()跳转到登录界面
            //不过这样写有个问题，比如在歌单详情SheetDetailActivity中点击收藏(在这之前先用Postman登录下本账号)，
            //如果登录信息过期，那么就会跳转到登录界面；但是你会发现,点击返回还是回到歌单详情界面
            //这不符合我们的逻辑，因为在登录界面，返回的话，是返回到桌面的(只有登录才会跳转到主页面)
            //也就是说：跳转到登录界面，歌单详情和主界面没有关闭； 那么这个问题后面再实现
            AppContext.getInstance().logout();
        } else if (code == 403) {
            ToastUtil.errorShortToast(R.string.error_network_not_permission);
        } else if (code == 404) {
            ToastUtil.errorShortToast(R.string.error_network_not_found);
        } else if (code >= 500) {
            ToastUtil.errorShortToast(R.string.error_network_server);
        } else {
            ToastUtil.errorShortToast(R.string.error_network_unknown);
        }
    }
}
