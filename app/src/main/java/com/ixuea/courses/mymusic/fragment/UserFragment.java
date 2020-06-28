package com.ixuea.courses.mymusic.fragment;

import android.os.Bundle;

import com.ixuea.courses.mymusic.R;
import com.ixuea.courses.mymusic.activity.UserDetailActivity;
import com.ixuea.courses.mymusic.adapter.UserAdapter;
import com.ixuea.courses.mymusic.api.Api;
import com.ixuea.courses.mymusic.domain.User;
import com.ixuea.courses.mymusic.domain.response.ListResponse;
import com.ixuea.courses.mymusic.listener.HttpObserver;
import com.ixuea.courses.mymusic.util.Constant;
import com.ixuea.courses.mymusic.util.LogUtil;

/**
 * 用户 搜索结果界面
 */
public class UserFragment extends BaseSearchFragment {

    private static final String TAG = "UserFragment";
    private UserAdapter adapter;//适配器

    @Override
    protected void initDatum() {
        super.initDatum();
        //创建适配器
        adapter = new UserAdapter(R.layout.item_user);
        //设置适配器
        rv.setAdapter(adapter);
    }

    @Override
    protected void initListeners() {
        super.initListeners();
        adapter.setOnItemClickListener((adapter, view, position) -> {
            //获取点击数据，并跳转到用户详情(携带id跳转)
            User data = (User) adapter.getItem(position);
            UserDetailActivity.startWithId(getMainActivity(), data.getId());
        });
    }

    @Override
    protected void fetchData(String data) {
        super.fetchData(data);
        LogUtil.d(TAG, "fetchData: " + data);

        Api.getInstance()
                .searchUsers(data)
                .subscribe(new HttpObserver<ListResponse<User>>() {
                    @Override
                    public void onSucceeded(ListResponse<User> data) {
                        adapter.replaceData(data.getData());
                    }
                });
    }

    /**
     * 创建方法
     * @param position
     */
    public static UserFragment newInstance(int position) {

        Bundle args = new Bundle();
        //传递参数
        args.putInt(Constant.ID, position);
        UserFragment fragment = new UserFragment();
        fragment.setArguments(args);
        return fragment;
    }
}
