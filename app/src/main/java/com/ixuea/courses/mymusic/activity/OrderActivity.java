package com.ixuea.courses.mymusic.activity;

import android.os.Bundle;

import com.ixuea.courses.mymusic.R;
import com.ixuea.courses.mymusic.adapter.OrderAdapter;
import com.ixuea.courses.mymusic.api.Api;
import com.ixuea.courses.mymusic.domain.OnPaySuccessEvent;
import com.ixuea.courses.mymusic.domain.Order;
import com.ixuea.courses.mymusic.domain.response.ListResponse;
import com.ixuea.courses.mymusic.listener.HttpObserver;
import com.ixuea.courses.mymusic.util.LogUtil;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import butterknife.BindView;

/**
 * 订单列表界面
 */
public class OrderActivity extends BaseTitleActivity {

    private static final String TAG = "OrderActivity";

    @BindView(R.id.rv)
    RecyclerView rv;
    private OrderAdapter adapter;//适配器

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //复用商品的布局
        setContentView(R.layout.activity_shop);
    }

    @Override
    protected void initView() {
        super.initView();

        rv.setHasFixedSize(true);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getMainActivity());
        rv.setLayoutManager(layoutManager);

        DividerItemDecoration decoration = new DividerItemDecoration(getMainActivity(), DividerItemDecoration.VERTICAL);
        rv.addItemDecoration(decoration);
    }

    @Override
    protected void initDatum() {
        super.initDatum();
        //注册
        EventBus.getDefault().register(this);

        //创建适配器
        adapter = new OrderAdapter(R.layout.item_order);
        rv.setAdapter(adapter);

        fetchData();
    }

    @Override
    protected void initListeners() {
        super.initListeners();

        //设置item点击事件
        adapter.setOnItemClickListener((adapter, view, position) -> {
            Order data = (Order) adapter.getItem(position);

            //跳转到订单详情界面(注意： 在商品详情界面中 购买后也是跳转到订单详情界面)
            //注意：这个是Order订单的id，进入到OrderDetailActivty后，根据这个订单id 请求数据;
            //     如果这个订单已支付，则隐藏部分控件；否则就显示
            startActivityExtraId(OrderDetailActivity.class, data.getId());
        });
    }

    private void fetchData() {
        Api.getInstance()
                .orders()
                .subscribe(new HttpObserver<ListResponse<Order>>() {
                    @Override
                    public void onSucceeded(ListResponse<Order> data) {
                        LogUtil.d(TAG, "orders: " + data.getData().size());

                        //设置数据
                        adapter.replaceData(data.getData());
                    }
                });
    }

    /**
     * 支付成功了通知
     */
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onPaySuccessEvent(OnPaySuccessEvent event) {
        fetchData();
    }

    @Override
    protected void onDestroy() {
        //取消注册
        EventBus.getDefault().unregister(this);
        super.onDestroy();
    }
}
