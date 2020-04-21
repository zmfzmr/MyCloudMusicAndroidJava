package com.ixuea.courses.mymusic.activity;

import android.os.Bundle;

import com.ixuea.courses.mymusic.R;
import com.ixuea.courses.mymusic.adapter.TopicAdapter;
import com.ixuea.courses.mymusic.api.Api;
import com.ixuea.courses.mymusic.domain.Topic;
import com.ixuea.courses.mymusic.domain.response.ListResponse;
import com.ixuea.courses.mymusic.listener.HttpObserver;
import com.ixuea.courses.mymusic.util.LogUtil;

import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import butterknife.BindView;

/**
 * 选择话题界面
 */
public class SelectTopicActivity extends BaseTitleActivity {

    private static final String TAG = "SelectTopicActivity";

    /**
     * 列表控件
     */
    @BindView(R.id.rv)
    RecyclerView rv;
    private TopicAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_topic);
    }

    @Override
    protected void initView() {
        super.initView();
        //设置尺寸固定
        rv.setHasFixedSize(true);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getMainActivity());
        rv.setLayoutManager(layoutManager);

        DividerItemDecoration decoration = new DividerItemDecoration(getMainActivity(), RecyclerView.VERTICAL);
        rv.addItemDecoration(decoration);
    }

    @Override
    protected void initDatum() {
        super.initDatum();
        //创建适配器
        adapter = new TopicAdapter(R.layout.item_topic);
        //设置适配器
        rv.setAdapter(adapter);

        //请求数据
        fetchData();
    }

    private void fetchData() {
        Api.getInstance()
                .topics()
                .subscribe(new HttpObserver<ListResponse<Topic>>() {
                    @Override
                    public void onSucceeded(ListResponse<Topic> data) {
                        LogUtil.d(TAG, "get topic success " + data.getData().size());
                        //设置数据到适配器
                        adapter.replaceData(data.getData());
                    }
                });
    }
}