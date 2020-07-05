package com.ixuea.courses.mymusic.interceptor;

import com.ixuea.courses.mymusic.exception.ResponseSignException;
import com.ixuea.courses.mymusic.util.Constant;
import com.ixuea.courses.mymusic.util.DigestUtil;
import com.ixuea.courses.mymusic.util.LogUtil;

import org.apache.commons.lang3.StringUtils;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.nio.charset.Charset;

import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.ResponseBody;
import okio.Buffer;
import okio.BufferedSource;

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

            //请求后
            LogUtil.d(TAG, "request after:" + method + "," + url);

            //---------------------------------------
            //这个服务器返回的(服务器端固定写死的)
            String sign = response.header(Constant.SIGN);

            //判断是否有签名
            if (StringUtils.isNotBlank(sign)) {
                //获取响应字符串
                // (当然也可以获取byte ，直接通过byte去解码，或者通过byte去计算签名，这也可以的)
                String dataString = getResponseString(response);

                //获取本地数据签名(把接收的数据，通过算法计算)
                String localSign = DigestUtil.sha1(dataString);

                if (localSign.equals(sign)) {
                    //签名正确(通过算法计算的前面 是否和 服务器返回来的前面是否是一样的)

                    //什么也不做
                    LogUtil.d(TAG, "process sign correct:" + method + "," + url);
                } else {
                    //签名错误
                    LogUtil.d(TAG, "process sign incorrect:" + method + "," + url);

                    //抛出异常
                    //让后续的代码处理  ResponseSignException: 自定义的
                    throw new ResponseSignException();
                }

            } else {
                //没有签名

                //什么也不处理

                //真实项目中
                //如果所有接口响应有签名
                //如果判断没有签名
                //就直接抛出错误(因为是别人串改数据的时候，把前面给删除了)
                LogUtil.d(TAG, "process not sign:" + method + "," + url);
            }

            return response;

            //请求后
        } catch (IOException e) {
            //我们这样直接抛出去
            throw e;
        }
        //因为抛出了异常，所以永远执行不到这里来，所以就会报错
//        return null;
    }

    /**
     * 获取响应字符串
     *
     * @param response
     * @return
     */
    private String getResponseString(Response response) throws IOException {
        //获取响应数据
        ResponseBody responseBody = response.body();

        //这个一定要这样写,否则读取一次，再读取一次就会报错
        BufferedSource source = responseBody.source();

        //调用这个方法相当于把所有的数据读取出来，读取最大值
        //抛出异常
        source.request(Long.MAX_VALUE);

        //获取缓冲器(读取数据出来后，放到这个缓冲器对象里面了)
        Buffer buffer = source.getBuffer();

        //这个缓冲先拷贝一份，然后读取成字符串
        //编码UTF-8
        //注意： 是Charset,不是Character
        return buffer.clone().readString(Charset.forName("UTF-8"));
    }
}
