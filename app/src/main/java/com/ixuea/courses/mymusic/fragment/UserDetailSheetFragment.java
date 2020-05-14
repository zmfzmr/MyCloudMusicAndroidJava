package com.ixuea.courses.mymusic.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.ixuea.courses.mymusic.R;
import com.ixuea.courses.mymusic.activity.SheetDetailActivity;
import com.ixuea.courses.mymusic.adapter.UserDetailSheetAdapter;
import com.ixuea.courses.mymusic.api.Api;
import com.ixuea.courses.mymusic.domain.BaseMultiItemEntity;
import com.ixuea.courses.mymusic.domain.Sheet;
import com.ixuea.courses.mymusic.domain.Title;
import com.ixuea.courses.mymusic.domain.response.ListResponse;
import com.ixuea.courses.mymusic.listener.HttpObserver;
import com.ixuea.courses.mymusic.util.Constant;
import com.ixuea.courses.mymusic.util.LogUtil;

import java.util.ArrayList;
import java.util.List;

import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import butterknife.BindView;

/**
 * 用户详情-歌单界面
 */
public class UserDetailSheetFragment extends BaseCommonFragment {
    private static final String TAG = "UserDetailSheetFragment";
    /**
     * 列表控件
     */
    @BindView(R.id.rv)
    RecyclerView rv;
    private String userId;//用于id
    private UserDetailSheetAdapter adapter;

    @Override
    protected void initViews() {
        super.initViews();

        //初始化固定
        rv.setHasFixedSize(true);

        LinearLayoutManager layoutManager = new LinearLayoutManager(getMainActivity());
        rv.setLayoutManager(layoutManager);

        DividerItemDecoration decoration = new DividerItemDecoration(getMainActivity(), DividerItemDecoration.VERTICAL);
        rv.addItemDecoration(decoration);
    }

    @Override
    protected void initDatum() {
        super.initDatum();
        //获取用户id
        userId = extraId();

        //创建适配器
        adapter = new UserDetailSheetAdapter();
        //设置适配器
        rv.setAdapter(adapter);

        fetchData();
    }

    @Override
    protected void initListeners() {
        super.initListeners();

        //设置item点击事件
        adapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                //获取当前点击的数据
                Object data = adapter.getItem(position);

                if (data instanceof Sheet) {
                    //转为歌单
                    Sheet sheet = (Sheet) data;

                    //跳转界面(携带歌单id 跳转) (歌单详情那边 根据这个id 获取到 网络数据)
                    startActivityExtraId(SheetDetailActivity.class, sheet.getId());
                }
            }
        });
    }

    private void fetchData() {
        //创建数据列表 (多类型数据用的是 BaseMultiItemEntity )
        List<BaseMultiItemEntity> datum = new ArrayList<>();

        //添加标题 (注意：这里传入的是一个对象Title，不能单独的 文本内容)
        datum.add(new Title("创建的歌单"));

        //创建的歌单
        Api.getInstance()
                //userId: 用户id  用户点击头像或者@爱学啊 传递进来的用户id(就是点击那个用户，那个用户的id就传入了进来)
                //注意：这里不能传入sp里面的id，我们这里是点击那个用户，就传入哪个用户的id
                .createSheets(userId)
                .subscribe(new HttpObserver<ListResponse<Sheet>>() {
                    @Override
                    public void onSucceeded(ListResponse<Sheet> data) {
                        LogUtil.d(TAG, "create sheets: " + data.getData().size());
                        //添加集合数据
                        datum.addAll(data.getData());

                        //请求收藏的歌单
                        Api.getInstance()
                                .collectSheets(userId)
                                .subscribe(new HttpObserver<ListResponse<Sheet>>() {
                                    @Override
                                    public void onSucceeded(ListResponse<Sheet> data) {
                                        //添加标题
                                        datum.add(new Title("收藏的歌单"));

                                        //添加集合数据
                                        datum.addAll(data.getData());

                                        LogUtil.d(TAG, "collect sheets: " + datum.size());

                                        // 设置数据到适配器
                                        adapter.replaceData(datum);

                                    }
                                });
                    }
                });
    }

    /**
     * 返回显示的布局
     */
    @Override
    protected View getLayoutView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_user_detail_sheet, container, false);
    }

    /**
     * 创建方法
     * @param userId
     */
    public static UserDetailSheetFragment newInstance(String userId) {

        Bundle args = new Bundle();
        //添加用户id
        args.putString(Constant.ID, userId);

        UserDetailSheetFragment fragment = new UserDetailSheetFragment();
        fragment.setArguments(args);
        return fragment;
    }
}
