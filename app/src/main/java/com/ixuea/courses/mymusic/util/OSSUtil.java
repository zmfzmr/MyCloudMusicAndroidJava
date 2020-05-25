package com.ixuea.courses.mymusic.util;

import android.content.Context;

import com.alibaba.sdk.android.oss.ClientConfiguration;
import com.alibaba.sdk.android.oss.OSSClient;
import com.alibaba.sdk.android.oss.common.auth.OSSPlainTextAKSKCredentialProvider;

/**
 * 阿里云OSS工具类
 * <p>
 * OSS客户端可能会在项目中多个位置，使用建议封装，
 * 我们这里只封装了OSSClient获取，真实项目中，建议将逻辑也封装到工具类中。
 * <p>
 * bucket
 * <p>
 * 所有图片 a bucket
 * 所有音乐 b bucket
 * <p>
 * 而我们项目中用到的都是一个bucket，所以我们设置成单例的
 */
public class OSSUtil {
    /**
     * 阿里云oss
     */
    private static OSSClient instance;

    /**
     * 获取单例
     *
     * @param context
     * @return
     */
    public static OSSClient getInstance(Context context) {
        if (instance == null) {
            init(context);
        }
        return instance;
    }

    /**
     * 初始化
     *
     * @param context
     */
    private static void init(Context context) {
        //推荐使用OSSAuthCredentialsProvider
        //因为他有token过期可以及时更新
        //我们这里为了课程难度
        //就直接使用AK(访问key)和SK(可以理解为安全key)

        //请勿泄漏该key
        //和非法使用

        //AK 和SK 权限是非常大的，可以访问阿里云的全局资源
        // （但是这里老师 做了限定，只能访问bucket(简单理解为阿里云上一个文件夹，并不能真正的理解为文件夹)）
        OSSPlainTextAKSKCredentialProvider credentialProvider = new OSSPlainTextAKSKCredentialProvider(
                Constant.ALIYUN_OSS_AK,
                Constant.ALIYUN_OSS_SK);

        //该配置类如果不设置
        //会有默认配置
        //具体可看该类
        ClientConfiguration config = new ClientConfiguration();
        //连接超时，默认15秒
        //连接超时 （解析服务器超时）
        config.setConnectionTimeout(15 * 1000);

        //socket超时，默认15秒
        //socket 读取数据超时(可以理解为为连接上服务器了，但是读取数据超时)
        config.setSocketTimeout(15 * 1000);

        //最大并发请求数，默认5个
        config.setMaxConcurrentRequest(5);

        //失败后最大重试次数，默认2次
        config.setMaxErrorRetry(2);

        //初始化oss客户端
        instance = new OSSClient(context.getApplicationContext(),
                //地址 String RESOURCE_ENDPOINT = "http://dev-courses-misuc.ixuea.com/%s"
                //用个空格就会把后面的字符取消
                String.format(Constant.RESOURCE_ENDPOINT, ""),
                //前面创建的OSSPlainTextAKSKCredentialProvider对象(包含AK和SK)
                credentialProvider);
    }
}
