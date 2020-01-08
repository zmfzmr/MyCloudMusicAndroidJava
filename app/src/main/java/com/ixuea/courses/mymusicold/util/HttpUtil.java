package com.ixuea.courses.mymusicold.util;

import android.text.TextUtils;

import com.ixuea.courses.mymusicold.R;
import com.ixuea.courses.mymusicold.domain.response.BaseResponse;

import java.net.ConnectException;
import java.net.SocketTimeoutException;
import java.net.UnknownHostException;

import retrofit2.HttpException;

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
                if (code == 401) {
                    ToastUtil.errorShortToast(R.string.error_network_not_auth);
                } else if (code == 403) {
                    ToastUtil.errorShortToast(R.string.error_network_not_permission);
                } else if (code == 404) {
                    ToastUtil.errorShortToast(R.string.error_network_not_found);
                } else if (code >= 500) {
                    ToastUtil.errorShortToast(R.string.error_network_server);
                } else {
                    ToastUtil.errorShortToast(R.string.error_network_unknown);
                }
            } else {
                ToastUtil.errorShortToast(R.string.error_network_unknown);
            }
        } else {//error为null（这个时候是走onNext-->else-->requestErrorHandler）
            //(登录失败的这种错误)
            if (data instanceof BaseResponse) {
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
}
