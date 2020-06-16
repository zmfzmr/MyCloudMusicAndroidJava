package com.ixuea.courses.mymusic.activity;

import android.os.Bundle;

import com.ixuea.courses.mymusic.R;
import com.ixuea.courses.mymusic.adapter.ShopAdapter;
import com.ixuea.courses.mymusic.api.Api;
import com.ixuea.courses.mymusic.domain.Book;
import com.ixuea.courses.mymusic.domain.response.ListResponse;
import com.ixuea.courses.mymusic.listener.HttpObserver;
import com.ixuea.courses.mymusic.util.LogUtil;

import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import butterknife.BindView;

/**
 * 商城界面
 */
public class ShopActivity extends BaseTitleActivity {

    private static final String TAG = "ShopActivity";
    @BindView(R.id.rv)
    RecyclerView rv;
    private ShopAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
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
        adapter = new ShopAdapter(R.layout.item_shop);
        rv.setAdapter(adapter);

        Api.getInstance()
                .shops()
                .subscribe(new HttpObserver<ListResponse<Book>>() {
                    @Override
                    public void onSucceeded(ListResponse<Book> data) {
                        LogUtil.d(TAG, "books: " + data.getData().size());

                        //设置数据
                        adapter.replaceData(data.getData());
                    }
                });
    }

    @Override
    protected void initListeners() {
        super.initListeners();
        //设置item点击事件
        adapter.setOnItemClickListener((adapter, view, position) -> {
            Book data = (Book) adapter.getItem(position);

            //跳转到商品详情(携带id跳转到商品详情)
            startActivityExtraId(ShopDetailActivity.class, data.getId());
        });
    }
}
