package com.ixuea.courses.mymusic.activity;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.ixuea.courses.mymusic.R;
import com.ixuea.courses.mymusic.api.Api;
import com.ixuea.courses.mymusic.domain.Order;
import com.ixuea.courses.mymusic.domain.Pay;
import com.ixuea.courses.mymusic.domain.PayParam;
import com.ixuea.courses.mymusic.domain.response.DetailResponse;
import com.ixuea.courses.mymusic.listener.HttpObserver;
import com.ixuea.courses.mymusic.util.ImageUtil;
import com.ixuea.courses.mymusic.util.LogUtil;
import com.ixuea.courses.mymusic.util.PayUtil;
import com.ixuea.courses.mymusic.util.TimeUtil;
import com.ixuea.courses.mymusic.util.ToastUtil;

import butterknife.BindView;
import butterknife.OnClick;
import me.shihao.library.XRadioGroup;

import static com.ixuea.courses.mymusic.domain.Order.ALIPAY;
import static com.ixuea.courses.mymusic.domain.Order.WECHAT;

/**
 * 订单详情
 */
public class OrderDetailActivity extends BaseTitleActivity {
    private static final String TAG = "OrderDetailActivity";
    /**
     * 订单状态
     */
    @BindView(R.id.tv_status)
    TextView tv_status;

    /**
     * 订单编号
     */
    @BindView(R.id.tv_number)
    TextView tv_number;

    /**
     * 封面
     */
    @BindView(R.id.iv_banner)
    ImageView iv_banner;

    /**
     * 标题
     */
    @BindView(R.id.tv_title)
    TextView tv_title;

    /**
     * 下单日期
     */
    @BindView(R.id.tv_created_at)
    TextView tv_created_at;

    /**
     * 订单来源
     */
    @BindView(R.id.tv_source)
    TextView tv_source;

    /**
     * 支付平台(来源) (比如在android上下了一个订单，不方便支付，我想放在ios那边支付)
     */
    @BindView(R.id.tv_origin)
    TextView tv_origin;
    /**
     * 支付渠道(支付宝 微信)
     */
    @BindView(R.id.tv_channel)
    TextView tv_channel;

    /**
     * 支付容器
     */
    @BindView(R.id.pay_container)
    View pay_container;

    /**
     * 支付单选按钮组
     */
    @BindView(R.id.rg_pay)
    XRadioGroup rg_pay;

    /**
     * 控制容器
     */
    @BindView(R.id.control_container)
    View control_container;

    /**
     * 价格
     */
    @BindView(R.id.tv_price)
    TextView tv_price;
    private String id;
    private Order data;//订单详情对象
    /**
     * 支付渠道  默认是支付宝  Order.ALIPAY = 10
     */
    private int channel = ALIPAY;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_detail);
    }

    @Override
    protected void initView() {
        super.initView();

        //默认选中支付宝
        rg_pay.check(R.id.rb_alipay);
    }

    @Override
    protected void initDatum() {
        super.initDatum();
        //商品的id (商品详情那边传递过来的)
        id = extraId();
        fetchData();
    }

    private void fetchData() {
        Api.getInstance()
                .orderDetail(id)
                .subscribe(new HttpObserver<DetailResponse<Order>>() {
                    @Override
                    public void onSucceeded(DetailResponse<Order> data) {
                        showData(data.getData());
                    }
                });
    }

    /**
     * 显示数据
     */
    private void showData(Order data) {
        this.data = data;

        //订单号
        tv_number.setText(getResources().getString(R.string.order_number_value, data.getNumber()));

        //封面 注意：这里要先获取Book，然后再获取封面banner
        ImageUtil.show(getMainActivity(), iv_banner, data.getBook().getBanner());

        //标题
        tv_title.setText(data.getBook().getTitle());

        //创建日期
        String created = TimeUtil.yyyyMMddHHmmss(data.getCreated_at());
        tv_created_at.setText(getResources().getString(R.string.order_created_value, created));

        //订单来源(Android iso web)
        tv_source.setText(getResources().getString(R.string.order_source_value,
                data.getSourceFormat()));

        //价格 注意：order模型中的price 是double类型
        tv_price.setText(getResources().getString(R.string.price, data.getPrice()));

        //显示订单状态
        showOrderStatus();


    }

    /**
     * 显示订单状态(支付平台 渠道 支付状态) 因为这个状态会变，所以放在一个方法里面
     */
    private void showOrderStatus() {
        //支付状态  "支付完成";订单关闭 待支付
        tv_status.setText(getResources().getString(R.string.order_status_value,
                data.getStatusFormat()));

        //订单状态颜色(支付完成：绿色 ; 否则就是亮灰色)
        tv_status.setTextColor(getResources().getColor(data.getStatusColor()));

        //支付平台(这个也是 Android iso web)
        tv_origin.setText(getResources().getString(R.string.origin_source_value,
                data.getOriginFormat()));

        //支付渠道(支付宝 微信 "" )
        tv_channel.setText(getResources().getString(R.string.origin_channel_value,
                data.getChannelFormat()));

        //判断订单状态 支付完成和关闭 隐藏支付渠道和控制容器; 否则就显示
        switch (data.getStatus()) {
            case Order.PAYED:
            case Order.CLOSE:
                //支付成功 和支付(订单)关闭都要隐藏 支付渠道 和支付控制容器

                //隐藏支付渠道
                pay_container.setVisibility(View.GONE);

                //隐藏支付控制容器
                control_container.setVisibility(View.GONE);

                break;
            default:
                //显示支付渠道
                pay_container.setVisibility(View.VISIBLE);

                //显示控制容器
                control_container.setVisibility(View.VISIBLE);
                break;
        }
    }

    /**
     * 控制按钮点击了(立即购买按钮点击)
     *
     * 思路： 1.点击支付按钮，获取支付参数
     *       2.判断渠道，调用不同支付方法
     */
    @OnClick(R.id.bt_control)
    public void onControlClick() {
        LogUtil.d(TAG, "onControlClick: ");

        //支付宝支付
        //这个支付宝支付参数是真实的（是在PostMan 请求过来的）
//        processAlipay("app_id=2019013063161737&charset=UTF-8&sign_type=RSA2&version=1.0&timestamp=2020-06-19+17%3A48%3A10&method=alipay.trade.app.pay&notify_url=http%3A%2F%2Fdev-my-cloud-music-api-rails.ixuea.com%2Fv1%2Fcallbacks%2Falipay&biz_content=%7B%22out_trade_no%22%3A%22202006170402148050%22%2C%22product_code%22%3A%22FAST_INSTANT_TRADE_PAY%22%2C%22total_amount%22%3A%222.0%22%2C%22subject%22%3A%22202006170402148050%22%2C%22passback_params%22%3A92%7D&sign=XtaWZJnMc7e5I6OVAy2ufjOpbEI3zci9S57Er2YEeAifFLvsw00kR7Is47%2BG0auYPOEBli%2BXeAbYUOCzx293MB%2FGUEPF3FUxahW1%2BUoNWduyfaiFYBHp1NNc35XY%2Bocz1hE5%2F%2FRDsiCjzDJ0FEdXvf6u7ggkkGFDQROKQ4FmYBRvKO6grh0BUJEZxjeYarplTu14UrIJNePh9%2FfGQIU4j01hGH7C3uuKllQPHNvaw9bLJIcpCcB9kakTsOim2N9oGJbMys0HFvvGZ4z92Sgi5018zsvngKJTVF1ApZtpliInZb59U3em%2B34Wb7fQdS8EvWlbP608lEBsYgCvUSJMRQ%3D%3D");

        fetchPayData();
    }

    /**
     * 获取支付参数
     */
    private void fetchPayData() {
        //创建参数
        PayParam data = new PayParam();
        //设置支付渠道 这个渠道模式支付宝 ALIPAY    Order.ALIPAY = 10
        data.setChannel(channel);

        //请求支付参数
        Api.getInstance()
                //data: 是PayParam参数对象
                .orderPay(id, data)
                .subscribe(new HttpObserver<DetailResponse<Pay>>() {
                    @Override
                    public void onSucceeded(DetailResponse<Pay> data) {
                        processPay(data.getData());
                    }
                });
    }

    /**
     * 处理支付
     *
     * @param data 请求支付宝参数格式是这样子的   里面为： 渠道(10: 表示支付宝  支付参数为： pay: "123achangdf")
     *             "data": {
     *             channel = 10
     *             pay = "123achangdf"
     *             }
     */
    private void processPay(Pay data) {
        //data: Pay对象
        switch (data.getChannel()) {
            case ALIPAY:
                //支付宝支付 这个data.getPay()就是支付参数，根据这个支付参数可以调用支付宝接口支付
                processAlipay(data.getPay());
                break;
            case WECHAT:
                //微信支付 (可能微信那边，可能不能把所有参数拼接到一个字符串里面，我们处理下就行了)
                processWechat(data.getPay());
                break;
            default:
                ToastUtil.errorShortToast(R.string.error_pay_channel);
                break;
        }
    }

    /**
     * TODO 处理微信支付
     * 这微信支付还没有实现
     */
    private void processWechat(String data) {

    }

    /**
     * 处理支付宝支付
     *
     * @param data
     */
    private void processAlipay(String data) {
        PayUtil.alipay(getMainActivity(), data);
    }
}
