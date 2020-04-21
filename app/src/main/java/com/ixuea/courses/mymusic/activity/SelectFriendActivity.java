package com.ixuea.courses.mymusic.activity;

import android.os.Bundle;
import android.view.View;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.ixuea.courses.mymusic.R;
import com.ixuea.courses.mymusic.adapter.FriendAdapter;
import com.ixuea.courses.mymusic.api.Api;
import com.ixuea.courses.mymusic.domain.User;
import com.ixuea.courses.mymusic.domain.event.SelectedFriendEvent;
import com.ixuea.courses.mymusic.domain.response.ListResponse;
import com.ixuea.courses.mymusic.listener.HttpObserver;

import org.greenrobot.eventbus.EventBus;

import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import butterknife.BindView;

/**
 * 选择好友界面
 */
public class SelectFriendActivity extends BaseTitleActivity {
    /**
     * 列表控件
     */
    @BindView(R.id.rv)
    RecyclerView rv;
    private FriendAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_friend);
    }

    @Override
    protected void initView() {
        super.initView();
        //设置尺寸固定
        rv.setHasFixedSize(true);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getMainActivity());
        rv.setLayoutManager(layoutManager);
        //分割线
        DividerItemDecoration decoration = new DividerItemDecoration(getMainActivity(), DividerItemDecoration.VERTICAL);
        rv.addItemDecoration(decoration);

    }

    @Override
    protected void initDatum() {
        super.initDatum();

        //创建适配器
        adapter = new FriendAdapter(R.layout.item_user);
        //设置适配器
        rv.setAdapter(adapter);

        //请求数据
        fetchData();
    }

    @Override
    protected void initListeners() {
        super.initListeners();

        //设置item监听器
        adapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                //获取点击的数据
                User data = (User) adapter.getItem(position);

                //发送数据
                EventBus.getDefault().post(new SelectedFriendEvent(data));

                //关闭界面
                finish();
            }
        });
    }

    private void fetchData() {
        //sp.getUserId()：保存到持久化的用户的id
        Api.getInstance()
                .friends(sp.getUserId())
                .subscribe(new HttpObserver<ListResponse<User>>() {
                    @Override
                    public void onSucceeded(ListResponse<User> data) {
                        //设置数据
                        adapter.replaceData(data.getData());
                    }
                });
    }
}