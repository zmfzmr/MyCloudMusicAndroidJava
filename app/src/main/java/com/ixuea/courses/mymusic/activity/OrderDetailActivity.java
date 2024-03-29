package com.ixuea.courses.mymusic.activity;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.ixuea.courses.mymusic.R;
import com.ixuea.courses.mymusic.api.Api;
import com.ixuea.courses.mymusic.domain.OnPaySuccessEvent;
import com.ixuea.courses.mymusic.domain.Order;
import com.ixuea.courses.mymusic.domain.Pay;
import com.ixuea.courses.mymusic.domain.PayParam;
import com.ixuea.courses.mymusic.domain.event.OnAliPayStatusChangedEvent;
import com.ixuea.courses.mymusic.domain.response.DetailResponse;
import com.ixuea.courses.mymusic.listener.HttpObserver;
import com.ixuea.courses.mymusic.util.AnalysisUtil;
import com.ixuea.courses.mymusic.util.ImageUtil;
import com.ixuea.courses.mymusic.util.LoadingUtil;
import com.ixuea.courses.mymusic.util.LogUtil;
import com.ixuea.courses.mymusic.util.PayUtil;
import com.ixuea.courses.mymusic.util.TimeUtil;
import com.ixuea.courses.mymusic.util.ToastUtil;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import butterknife.BindView;
import butterknife.OnClick;
import me.shihao.library.XRadioGroup;

import static com.ixuea.courses.mymusic.domain.Order.ALIPAY;
import static com.ixuea.courses.mymusic.domain.Order.WECHAT;

/**
 * 订单详情
 */
public class OrderDetailActivity extends BaseTitleActivity implements XRadioGroup.OnCheckedChangeListener {
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
    private boolean isNotifyPayStatus;//是否通知支付状态

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
        //注册
        EventBus.getDefault().register(this);

        //商品的id (商品详情那边传递过来的)
        id = extraId();
        fetchData();
    }

    @Override
    protected void initListeners() {
        super.initListeners();

        //设置支付方式改变监听器(单选按钮的监听)
        rg_pay.setOnCheckedChangeListener(this);
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

                //订单是支付成功状态 并且这个为true(默认为false，支付成功后会置为true)

                //这里为什么要这样写呢？
                //第一次进入订单详情界面,会调用initeDatum初始化网络数据，如果不判断的话，
                // 如果是订单状态是已支付的状态，那么就会重复发送事件
                // 而这个if什么时候执行你，支付成功过后，把这个isNotifyPayStatus置为true;
                // 当刷新订单状态的时候，这个if就会执行了
                if (data.getStatus() == Order.PAYED && isNotifyPayStatus) {
                    //发送支付成功通知
                    EventBus.getDefault().post(new OnPaySuccessEvent());

                    //支付成功后，就置为false，防止重复发送通知
                    isNotifyPayStatus = false;
                }

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

    /**
     * 支付宝支付状态改变了(这个事件是 PayUtil.alipay 发送过来的)
     *
     * @param event OnAliPayStatusChangedEvent
     */
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onAlipayStatusChanged(OnAliPayStatusChangedEvent event) {
        //event.getData(): 获取的是 PayResult(里面包含支付成功和失败)
        String resultStatus = event.getData().getResultStatus();

        //到过来写的目的是，防止破解我们应用的人，把整个9000改成一个空对象，
        //倒过来写的，因为resultStatus是支付宝那边返回的，无法修改，所以这样安全点
        //否则：如果是resultStatus.equals("9000") 可能就有问题

        //注意：这里9000 是支付宝本地的SDK告诉我们支付成功了
//        if (true) {//这个主要用来测试成功了
        if ("9000".equals(resultStatus)) {
            //本地支付成功

            //不能依赖本地支付结果
            //一定要以服务端为准
            LoadingUtil.showLoading(getMainActivity(), R.string.hint_pay_wait);

            //延时3秒(这里延迟3s的原因是：前面的9000是本地的支付宝SDK告诉我们支付成功，但是不能完全依赖本地，
            //       等待3秒(在这3到5s内，支付宝那边应该是返回数据给服务器了，否则的话，就是出什么问题了)，
            //       等支付宝返回数据给我们的服务器，然后从服务器请求刷新支付状态)
            //因为支付宝回调我们服务端可能有延迟
            //用这个TextView的延时方法
            tv_status.postDelayed(new Runnable() {
                /**
                 * 注意：这个是在主线程的
                 */
                @Override
                public void run() {
                    //检查支付状态
                    checkPayStatus();
                }
            }, 3000);

            //这里就不根据服务端判断了
            //购买成功统计
            AnalysisUtil.onPurchase(getMainActivity(), true, data);

        } else {
            //支付取消
            //支付失败
            ToastUtil.errorShortToast(R.string.error_pay_failed);

            //购买事件
            AnalysisUtil.onPurchase(getMainActivity(), false, data);
        }
    }

    /**
     * 检查支付状态
     */
    private void checkPayStatus() {
        //通知支付结果
        isNotifyPayStatus = true;

        //隐藏加载对话框
        LoadingUtil.hideLoading();

        //请求订单详情
        //因为走到这一步，是已经支付成功了的，所以刷新支付状态
        fetchData();
    }

    @Override
    protected void onDestroy() {
        //取消注册
        EventBus.getDefault().unregister(this);
        super.onDestroy();
    }

    /**
     * 支付方式单选按钮改变了
     *
     * @param group
     * @param checkedId
     */
    @Override
    public void onCheckedChanged(XRadioGroup group, int checkedId) {
        switch (checkedId) {
            case R.id.rb_alipay:
                //ALIPAY: 就是Order.ALIPAY
                channel = ALIPAY;
                break;
            case R.id.rb_wechat:

                //Order.WECHAT
                channel = WECHAT;

                //点击微信那个按钮后，赋值给了 channel （这个WECHAT这个值是跟服务器协商好的）
                //然后当我们点击 支付按钮后，-->fetchPayData 中传给给PayParam中，通过post请求给服务器
                //如果是微信的值，一起传递过去，那么就会提示支付渠道错误(因为服务器没有实现微信这个功能)
                //注意：这个  (支付渠道错误) 在fetchPayData网络请求的时候，错误的话，
                // 在里面的那个HttpObserver里面已经把这条message处理好，所以就通过ToastUtil显示了出来
                break;
        }
    }
}
