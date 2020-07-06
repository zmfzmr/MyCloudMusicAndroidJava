package com.ixuea.courses.mymusic.interceptor;

import com.ixuea.courses.mymusic.exception.ResponseDecryptException;
import com.ixuea.courses.mymusic.exception.ResponseSignException;
import com.ixuea.courses.mymusic.util.Constant;
import com.ixuea.courses.mymusic.util.DigestUtil;
import com.ixuea.courses.mymusic.util.LogUtil;

import org.apache.commons.lang3.StringUtils;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.nio.charset.Charset;

import okhttp3.Interceptor;
import okhttp3.MediaType;
import okhttp3.Request;
import okhttp3.RequestBody;
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

            //请求前  处理网络请求
            LogUtil.d(TAG, "request before : " + method + ", " + url);

            //获取请求体(也就是请求的部分参数)
            RequestBody requestBody = request.body();

            if (url.endsWith("v2/orders") && method.equals("POST")) {
                //该接口需要添加参数签名（目前只对这个接口签名，如果对所有接口签名，那么就不需要判断了）

                //这部分共用的，放到外面来
//                //获取请求体(也就是请求的部分参数)
//                RequestBody requestBody = request.body();

                //把整个请求体转换成一个字符串(因为计算签名要字符串的)
                // 也就是请求体(json数据)：去掉空格后的字符串 比如： {"book_id":1,"source":10}
                String bodyString = getRequestBodyString(requestBody);
                //计算签名
                String sign = DigestUtil.sha1(bodyString);

                LogUtil.d(TAG, "sign params success: " + method + ", " + url + ", " + sign);

                //已经创建的request是不支持更改的，所以创建一个新的request添加请求头等参数

                request = request.newBuilder()
                        //添加原来的请求头
                        .headers(request.headers())
                        //请求头添加签名 (如sign = sdgdad132  这种形式)
                        //sign： 计算出来的签名值
                        .addHeader(Constant.SIGN, sign)
                        //method: 前面获取到的请求方式
                        //requestBody: 请求体(body部分)
                        //设置请求方式和请求体
                        .method(method, requestBody)
                        .build();

            } else if (url.endsWith("v3/orders") && method.equals("POST")) {
                //该接口参数需要加密

                //将请求体转为字符串
                String bodyString = getRequestBodyString(requestBody);
                //将参数加密
                String encryptBodyString = DigestUtil.encryptAES(bodyString);

                //使用新参数创建请求体

                //为什么更改header
                //因为对于加密的数据
                //有些服务端框架会自动处理
                //例如：我们后台用的Rails框架
                //如果传递的JSON他会解析为Hash
                //类似iOS中的字典
                //由于我们参数加密了
                //其实就是一段字符串
                //所以要更改Content-Type  用的文本方式 所以用plain (application/plain)
                //不然服务端后出错

                requestBody = RequestBody.create(encryptBodyString,
                        MediaType.parse("application/plain"));
                //创建请求
                request = request.newBuilder()
                        //添加原来的请求头
                        .headers(request.headers())
                        //设置请求方式和请求体
                        .method(method, requestBody)
                        .build();
                LogUtil.d(TAG, "encrypt param success:" + method + "," + url);
            }

            //执行网络请求(前面的步骤都没有请求网络，执行了这个方法后才真正的执行网络操作)
            Response response = chain.proceed(request);

            //---------------------------------------
            //请求后  处理网络请求
            LogUtil.d(TAG, "request after:" + method + "," + url);
            //思路：计算签名和服务器签名比较是否相等

            //这个服务器返回的签名(服务器端固定写死的)
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

            //解密-------------------------------------
            if (url.endsWith("v3/orders") && method.equals("GET")) {
                //该接口响应需要解密

                //真实项目中可能所有接口都会加密
                //所以就不用判断了

                //我们这里为了降低课程难度
                //所以只加密这个几个接口

                //获取响应字符串
                String dataString = getResponseString(response);

                //解密数据(这里解密后其实是一串json字符串了，只是还没有json格式化，下面的ResponseBody实现json格式化)
                String decryptString = DigestUtil.decryptAES(dataString);

                if (StringUtils.isNotBlank(decryptString)) {

                    //解密成功

                    //就认为数据没问题

                    //创建新的响应体
                    //参数1：字节数组
                    ResponseBody responseBody = ResponseBody.create(decryptString.getBytes(),
                            MediaType.parse("application/json"));

                    //解密后需要创建一个响应对象(因为原来的不能更改)

                    response = response.newBuilder()
                            //原来的请求头，注释是headers加s  不是addheader()
                            .headers(response.headers())
                            .body(responseBody)
                            .build();
                    LogUtil.d(TAG, "process decrypt success:" + method + "," + url);
                } else {
                    //解密失败(也就是更改了响应加密的字符串 这个时候是解密不出来的，这就为为null了)
                    LogUtil.d(TAG, "process decrypt error:" + method + "," + url);
                    throw new ResponseDecryptException();
                }
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
     * 将请求体转为字符串
     *
     * @param requestBody RequestBody
     * @return 请求体String
     */
    private String getRequestBodyString(RequestBody requestBody) throws IOException {
        //创建缓冲 Okhttp控件里面依赖的 okio里面的
        Buffer buffer = new Buffer();
        //将请求体内容写人缓冲
        //抛出异常
        requestBody.writeTo(buffer);
        //转为字符串
        return buffer.readUtf8();
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
