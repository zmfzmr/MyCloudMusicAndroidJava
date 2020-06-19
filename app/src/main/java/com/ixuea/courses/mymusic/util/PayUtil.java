package com.ixuea.courses.mymusic.util;

import android.app.Activity;

import com.alipay.sdk.app.PayTask;
import com.ixuea.courses.mymusic.domain.alipay.PayResult;
import com.ixuea.courses.mymusic.domain.event.OnAliPayStatusChangedEvent;

import org.greenrobot.eventbus.EventBus;

import java.util.Map;

/**
 * 支付工具类
 */
public class PayUtil {
    private static final String TAG = "PayUtil";

    /**
     * 支付宝支付
     * 支付宝官方开发文档：https://docs.open.alipay.com/204/105295/
     *
     * @param activity
     * @param data
     */
    public static void alipay(Activity activity, String data) {
        //创建运行对象
        Runnable runnable = new Runnable() {
            /**
             * 子线程(并不是所有的Runnable run 方法都是在主线程进行。
             * 主要是看是依附在谁身上决定的, 这里依附在Thead  上，那么这里就是子线程
             */
            @Override
            public void run() {
                //创建支付宝支付任务(传入activity，表示在这个页面上创建支付任务)
                PayTask alipay = new PayTask(activity);

                //支付结果(1.支付的数据，2：true: 弹出一个加载对话框之类)
                Map<String, String> result = alipay.payV2(data, true);

                //解析支付结果(支付失败，还是支付成功，结果都在这个对象里面)
                PayResult resultData = new PayResult(result);

                LogUtil.d(TAG, "alipay: " + resultData);

                //发布解析状态
                EventBus.getDefault().post(new OnAliPayStatusChangedEvent(resultData));

            }
        };
        //创建一个线程
        Thread thread = new Thread(runnable);

        thread.start();//启动线程
    }
}
