package com.ixuea.courses.mymusic.domain;

import static com.ixuea.courses.mymusic.domain.Order.ANDROID;

/**
 * 这个主要往接口传入参数(完我们的服务器传入参数)
 */
public class PayParam {
    /**
     * 来源： android ios 或者web  这里写死是Android 的值10
     * Order.ANDROID= 10
     * <p>
     * 支付平台
     * 默认值为android
     * 却不能更改
     * 因为Android平台的来说肯定就是Android
     */
    private final int origin = ANDROID;

    /**
     * 支付渠道
     */
    private int channel;

    //origin 用了final 修饰，不能更改，所以没有set方法
    public int getOrigin() {
        return origin;
    }

    public int getChannel() {
        return channel;
    }

    public void setChannel(int channel) {
        this.channel = channel;
    }
}
