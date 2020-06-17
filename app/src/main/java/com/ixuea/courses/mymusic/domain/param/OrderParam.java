package com.ixuea.courses.mymusic.domain.param;

import com.ixuea.courses.mymusic.domain.Order;

/**
 * 创建订单参数(因为是参数 所以可以不用继承BaseModel)
 * 可以复用Order
 */
public class OrderParam {
    /**
     * 商品id
     * 为什么我们这里不写price价格呢，因为价格，我们不应该从本地去取，应该从服务端去取
     */
    private String book_id;
    /**
     * 创建订单的平台
     * 默认值为android
     * 且不能更改
     * 因为Android平台的来说肯定就是Android
     */
    private final int source = Order.ANDROID;

    public String getBook_id() {
        return book_id;
    }

    public void setBook_id(String book_id) {
        this.book_id = book_id;
    }

    public int getSource() {
        return source;
    }
}
