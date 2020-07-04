package com.ixuea.courses.mymusic.activity;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.ixuea.courses.mymusic.R;
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
    }

    /**
     * 创建订单(参数签名)
     *
     * @param view
     */
    public void onCreateOrderSignClick(View view) {
        LogUtil.d(TAG, "onCreateOrderSignClick: ");
    }

    /**
     * 订单列表(响应加密)
     */
    public void onOrderListEncryptClick(View view) {
        LogUtil.d(TAG, "onOrderListEncryptClick: ");
    }

    /**
     * 创建订单(参数加密)
     */
    public void onCreateOrderEncryptClick(View view) {
        LogUtil.d(TAG, "onCreateOrderEncryptClick: ");
    }
}
