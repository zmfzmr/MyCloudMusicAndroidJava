package com.ixuea.courses.mymusic.domain;

import com.ixuea.courses.mymusic.R;

import org.jetbrains.annotations.NotNull;

/**
 * 订单模型
 */
public class Order extends BaseModel {

    public static final int WAIT_PAY = 0;//待支付
    public static final int PAYED = 10;//支付完成
    public static final int CLOSE = 20;//订单关闭

    public static final int UNKNOWN = 0;//未知
    /**
     * android  值是和服务端协商好的
     */
    public static final int ANDROID = 10;
    /**
     * ios
     */
    public static final int IOS = 20;
    /**
     * web
     */
    public static final int WEB = 30;
    /**
     * 支付宝
     */
    public static final int ALIPAY = 10;
    /**
     * 微信
     */
    public static final int WECHAT = 20;

    /**
     * 订单状态
     * 0:待支付;10:支付完成;20:订单关闭
     * <p>
     * 注意：这个不是常量，改成私有的就行
     */
    private int status;

    /**
     * 价格 (虽说返回的json里面是String类型，但是价格有小数，所以这里用double类型)
     */
    private double price;
    /**
     * 订单来源
     * 一般订单来源不会返回给客户端
     * 我们这样返回来只是给大家演示如何显示这些字段而已
     * 也就是在那个平台创建的订单
     */
    private int source;

    /**
     * 翻译：origin:起源
     * <p>
     * 支付来源
     * 因为可能有创建订单是web网站
     * 用户是在手机上支付的
     */
    private int origin;
    /**
     * 支付渠道
     */
    private int channel;

    /**
     * 订单号
     */
    private String number;
    /**
     * 订单所关联的商品
     */
    private Book book;

    //辅助方法

    /**
     * 支付渠道格式化
     *
     * @return
     */
    public String getChannelFormat() {
        //channel: 支付渠道(比如支付宝 微信)
        switch (channel) {
            case ALIPAY:
                return "支付宝";
            case WECHAT:
                return "微信";
            default:
                //有可能是后台我们把它改成支付的
                return "";
        }
    }

    /**
     * 支付来源格式化
     */
    public String getOriginFormat() {
        //origin:支付来源
        return sourceFormat(origin);
    }

    /**
     * 来源格式化
     */
    @NotNull
    private String sourceFormat(int data) {
        switch (data) {
            case ANDROID:
                return "Android";
            case IOS:
                return "ios";
            case WEB:
                return "Web";
            default:
                return "";
        }
    }

    /**
     * 订单来源格式化
     */
    public String getSourceFormat() {
        //source:订单来源 （也就是在哪个平台创建的订单） 跟上面的那个支付来源是一样大的
        return sourceFormat(source);

    }

    /**
     * 支付状态格式化
     */
    public String getStatusFormat() {
        //订单状态： 0:待支付;10:支付完成;20:订单关闭
        switch (status) {
            case PAYED:
                return "支付完成";
            case CLOSE:
                return "订单关闭";
            default:
                return "待支付";
        }
    }

    /**
     * 获取状态颜色
     */
    public int getStatusColor() {
        switch (status) {
            case PAYED:
                return R.color.color_pass;
            default:
                return R.color.light_grey;
        }
    }
    //end辅助方法


    //生成set get 方法时候，那些常量不需要生成

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public int getSource() {
        return source;
    }

    public void setSource(int source) {
        this.source = source;
    }

    public int getOrigin() {
        return origin;
    }

    public void setOrigin(int origin) {
        this.origin = origin;
    }

    public int getChannel() {
        return channel;
    }

    public void setChannel(int channel) {
        this.channel = channel;
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public Book getBook() {
        return book;
    }

    public void setBook(Book book) {
        this.book = book;
    }
}
