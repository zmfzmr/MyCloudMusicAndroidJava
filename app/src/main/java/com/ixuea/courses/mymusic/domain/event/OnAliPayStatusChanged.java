package com.ixuea.courses.mymusic.domain.event;

import com.ixuea.courses.mymusic.domain.alipay.PayResult;

/**
 * 支付宝 支付状态改变了事件
 */
public class OnAliPayStatusChanged {
    private PayResult data;//注意：这里不能用final

    public OnAliPayStatusChanged(PayResult data) {
        this.data = data;
    }

    public PayResult getData() {
        return data;
    }

    public void setData(PayResult data) {
        this.data = data;
    }
}
