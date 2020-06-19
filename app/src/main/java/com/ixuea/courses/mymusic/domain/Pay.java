package com.ixuea.courses.mymusic.domain;

import org.apache.commons.lang3.builder.ToStringBuilder;

/**
 * 支付参数
 * 网络请求响应(这个参数值：主要是往支付宝 微信那边传入的参数，而不是请求我们接口的参数)
 */
public class Pay {

    /**
     * 支付渠道
     */
    private int channel;

    /**
     * 支付参数
     */
    private String pay;

    public int getChannel() {
        return channel;
    }

    public void setChannel(int channel) {
        this.channel = channel;
    }

    public String getPay() {
        return pay;
    }

    public void setPay(String pay) {
        this.pay = pay;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this)
                .append("channel", channel)
                .append("pay", pay)
                .toString();
    }
}
