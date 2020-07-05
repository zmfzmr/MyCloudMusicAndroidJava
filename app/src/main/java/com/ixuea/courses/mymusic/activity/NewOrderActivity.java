package com.ixuea.courses.mymusic.activity;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.ixuea.courses.mymusic.R;
import com.ixuea.courses.mymusic.api.Api;
import com.ixuea.courses.mymusic.domain.BaseModel;
import com.ixuea.courses.mymusic.domain.Order;
import com.ixuea.courses.mymusic.domain.param.OrderParam;
import com.ixuea.courses.mymusic.domain.response.DetailResponse;
import com.ixuea.courses.mymusic.domain.response.ListResponse;
import com.ixuea.courses.mymusic.listener.HttpObserver;
import com.ixuea.courses.mymusic.util.LogUtil;

import butterknife.BindView;

/**
 * 演示订单接口签名和加密功能界面
 */
public class NewOrderActivity extends BaseTitleActivity {

    private static final String TAG = "NewOrderActivity";
    /**
     * 提示控件
     */
    @BindView(R.id.tv_info)
    TextView tv_info;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_order);
    }

    /**
     * 订单列表(响应签名)
     */
    public void onOrderListSignClick(View view) {
        LogUtil.d(TAG, "onOrderListSignClick: ");

        clearInfo();

        Api.getInstance()
                .ordersV2()
                .subscribe(new HttpObserver<ListResponse<Order>>() {
                    @Override
                    public void onSucceeded(ListResponse<Order> data) {
                        tv_info.setText("(响应签名)订单数量： " + data.getData().size());
                    }
                });
    }

    /**
     * 创建订单(参数签名)
     *
     * @param view
     */
    public void onCreateOrderSignClick(View view) {
        LogUtil.d(TAG, "onCreateOrderSignClick: ");

        clearInfo();

        OrderParam data = new OrderParam();

        data.setBook_id("1");

        //调用登录接口
        Api.getInstance()
                .createOrderV2(data)
                .subscribe(new HttpObserver<DetailResponse<BaseModel>>() {
                    @Override
                    public void onSucceeded(DetailResponse<BaseModel> data) {
                        tv_info.setText("(签名接口)订单创建成功");
                    }
                });
    }

    /**
     * 订单列表(响应加密)
     */
    public void onOrderListEncryptClick(View view) {
        LogUtil.d(TAG, "onOrderListEncryptClick: ");
        clearInfo();
    }

    /**
     * 创建订单(参数加密)
     */
    public void onCreateOrderEncryptClick(View view) {
        LogUtil.d(TAG, "onCreateOrderEncryptClick: ");
        clearInfo();
    }

    /**
     * 清除提示
     */
    private void clearInfo() {
        tv_info.setText("");
    }
}
