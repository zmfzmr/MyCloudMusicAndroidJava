package com.ixuea.courses.mymusic.activity;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.ixuea.courses.mymusic.R;
import com.ixuea.courses.mymusic.api.Api;
import com.ixuea.courses.mymusic.domain.Order;
import com.ixuea.courses.mymusic.domain.response.DetailResponse;
import com.ixuea.courses.mymusic.listener.HttpObserver;
import com.ixuea.courses.mymusic.util.ImageUtil;
import com.ixuea.courses.mymusic.util.LogUtil;
import com.ixuea.courses.mymusic.util.TimeUtil;

import butterknife.BindView;
import butterknife.OnClick;
import me.shihao.library.XRadioGroup;

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
     */
    @OnClick(R.id.bt_control)
    public void onControlClick() {
        LogUtil.d(TAG, "onControlClick: ");
    }
}
