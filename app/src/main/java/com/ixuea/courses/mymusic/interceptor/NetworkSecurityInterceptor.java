package com.ixuea.courses.mymusic.interceptor;

import com.ixuea.courses.mymusic.util.LogUtil;

import org.jetbrains.annotations.NotNull;

import java.io.IOException;

import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;

/**
 * okhttp插件
 * 主要用来处理一些接口数据签名和加密功能
 * <p>
 * Network：网络
 * Security : 安全  interceptor: 拦截器
 */
public class NetworkSecurityInterceptor implements Interceptor {

    private static final String TAG = "NetworkSecurityInterceptor";

    /**
     * 每个拦截器都会调用
     *
     * @param chain 拦截器 链
     * @return
     * @throws IOException
     */
    @NotNull
    @Override
    public Response intercept(@NotNull Chain chain) throws IOException {
        try {
            //获取request
            Request request = chain.request();
            //获取请求的url
            String url = request.url().toString();
            //请求方式
            String method = request.method();

            //请求前
            LogUtil.d(TAG, "request before : " + method + ", " + url);

            //处理网络请求(这个后面处理)

            //执行网络请求(前面的步骤都没有请求网络，执行了这个方法后才真正的执行网络操作)
            Response response = chain.proceed(request);

            LogUtil.d(TAG, "request after:" + method + "," + url);

            return response;

            //请求后
        } catch (IOException e) {
            //我们这样直接抛出去
            throw e;
        }
        //因为抛出了异常，所以永远执行不到这里来，所以就会报错
//        return null;
    }
}
