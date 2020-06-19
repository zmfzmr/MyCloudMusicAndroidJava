package com.ixuea.courses.mymusic.activity;

import android.os.Bundle;

import com.ixuea.courses.mymusic.R;
import com.ixuea.courses.mymusic.adapter.OrderAdapter;
import com.ixuea.courses.mymusic.api.Api;
import com.ixuea.courses.mymusic.domain.Order;
import com.ixuea.courses.mymusic.domain.response.ListResponse;
import com.ixuea.courses.mymusic.listener.HttpObserver;
import com.ixuea.courses.mymusic.util.LogUtil;

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

        //创建适配器
        adapter = new OrderAdapter(R.layout.item_order);
        rv.setAdapter(adapter);

        fetchData();
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
}
