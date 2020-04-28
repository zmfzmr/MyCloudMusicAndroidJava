package com.ixuea.courses.mymusic.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ExpandableListView;

import com.ixuea.courses.mymusic.R;
import com.ixuea.courses.mymusic.activity.SheetDetailActivity;
import com.ixuea.courses.mymusic.adapter.MeAdapter;
import com.ixuea.courses.mymusic.api.Api;
import com.ixuea.courses.mymusic.domain.Sheet;
import com.ixuea.courses.mymusic.domain.event.CreateSheetClickEvent;
import com.ixuea.courses.mymusic.domain.response.ListResponse;
import com.ixuea.courses.mymusic.domain.ui.MeGroup;
import com.ixuea.courses.mymusic.listener.HttpObserver;
import com.ixuea.courses.mymusic.util.LogUtil;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;

import butterknife.BindView;
import io.reactivex.Observable;

/**
 * 首页-我的界面
 *
 * 他可以实现列表展开功能，RecyclerView也可以实现折叠效果，
 * 只是要自己实现，或者使用第三方框架；顶部是通过头部实现的。
 */
public class MeFragment extends BaseCommonFragment implements ExpandableListView.OnChildClickListener {

    private static final String TAG = "MeFragment";
    /**
     * 列表控件
     */
    @BindView(R.id.elv)
    ExpandableListView elv;
    private MeAdapter adapter;

    @Override
    protected void initDatum() {
        super.initDatum();

        //注册
        EventBus.getDefault().register(this);

        //创建适配器
        adapter = new MeAdapter(getMainActivity());

        //设置适配器
        elv.setAdapter(adapter);

        //数据类表
        ArrayList<MeGroup> datum = new ArrayList<>();

        //创建歌单请求
        Observable<ListResponse<Sheet>> createSheetApi = Api.getInstance()
                .createSheets(sp.getUserId());
        //收藏到的歌单请求
        Observable<ListResponse<Sheet>> collectSheetsApi = Api.getInstance()
                .collectSheets(sp.getUserId());

        //普通的方式
        //请求是线性(一个人干活，干完了另外一个人干)
        //(外请求：获取用户创建的歌单 内请求：获取用户收藏的歌单)
        createSheetApi.subscribe(new HttpObserver<ListResponse<Sheet>>() {
            @Override
            public void onSucceeded(ListResponse<Sheet> data) {
                //添加数据
                //这里MeGroup 参数3：是否显示右侧按钮 这是是创建的歌单，就显示右侧按钮
                datum.add(new MeGroup("创建的歌单", data.getData(), true));

                collectSheetsApi.subscribe(new HttpObserver<ListResponse<Sheet>>() {
                    @Override
                    public void onSucceeded(ListResponse<Sheet> data) {
                        //添加数据(这里是收藏歌单，所以MeGroup 参数3：表示不显示右侧按钮)
                        //这里传入了false，表示不显示右侧按钮
                        datum.add(new MeGroup("收藏的歌单", data.getData(), false));

                        //设置数据到适配器
                        LogUtil.d(TAG, "get sheets success: " + datum.size());

                        adapter.setDatum(datum);

                        //展开所有组(显示完数据后才展开所有的组)
                        expandedAll();
                    }
                });
            }
        });
    }

    /**
     * 展开所有组
     */
    private void expandedAll() {
        //adapter.getGroupCount():组的个数
        for (int i = 0; i < adapter.getGroupCount(); i++) {
            elv.expandGroup(i);
        }
    }

    @Override
    protected void initListeners() {
        super.initListeners();
        //设置子item点击监听器
        //注意：是有个child的
        elv.setOnChildClickListener(this);
    }

    /**
     * 构造方法
     * 固定写法
     *
     * @return MeFragment本身
     */
    public static MeFragment newInstance() {

        Bundle args = new Bundle();

        MeFragment fragment = new MeFragment();
        fragment.setArguments(args);
        return fragment;
    }

    /**
     * 返回布局文件
     *
     * @param inflater           LayoutInflater
     * @param container          ViewGroup
     * @param savedInstanceState Bundle
     * @return
     */
    @Override
    protected View getLayoutView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_me, null);
    }

    /**
     * 子元素点击回调
     *
     * @param parent
     * @param v
     * @param groupPosition
     * @param childPosition
     * @param id
     * @return true:我们处理了事件
     */
    @Override
    public boolean onChildClick(ExpandableListView parent, View v, int groupPosition, int childPosition, long id) {
        //获取点击的数据
        Sheet data = adapter.getChildData(groupPosition, childPosition);

        //跳转到详情(这里是携带一个歌单id获取的)
        startActivityExtraId(SheetDetailActivity.class, data.getId());
        return true;//记得返回true
    }

    /**
     * 点击了歌单创建按钮事件
     */
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onCreateSheetClickEvent(CreateSheetClickEvent event) {
        LogUtil.d(TAG, "onCreateSheetClickEvent");
    }

    /**
     * 界面销毁时调用
     */
    @Override
    public void onDestroy() {
        //解除注册
        EventBus.getDefault().unregister(this);
        super.onDestroy();
    }
}
