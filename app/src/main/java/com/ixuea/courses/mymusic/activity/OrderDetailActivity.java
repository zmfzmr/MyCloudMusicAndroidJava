package com.ixuea.courses.mymusic.activity;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.ixuea.courses.mymusic.R;
import com.ixuea.courses.mymusic.util.LogUtil;

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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_detail);
    }

    @OnClick(R.id.bt_control)
    public void onControlClick() {
        LogUtil.d(TAG, "onControlClick: ");
    }
}
