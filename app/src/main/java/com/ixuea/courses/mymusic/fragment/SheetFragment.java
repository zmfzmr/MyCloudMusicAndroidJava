package com.ixuea.courses.mymusic.fragment;

import android.os.Bundle;

import com.ixuea.courses.mymusic.R;
import com.ixuea.courses.mymusic.activity.SheetDetailActivity;
import com.ixuea.courses.mymusic.adapter.SheetAdapter;
import com.ixuea.courses.mymusic.api.Api;
import com.ixuea.courses.mymusic.domain.Sheet;
import com.ixuea.courses.mymusic.domain.response.ListResponse;
import com.ixuea.courses.mymusic.listener.HttpObserver;
import com.ixuea.courses.mymusic.util.Constant;
import com.ixuea.courses.mymusic.util.LogUtil;

/**
 * 歌单搜索结果界面
 */
public class SheetFragment extends BaseSearchFragment {

    private static final String TAG = "SheetFragment";
    private SheetAdapter adapter;

    @Override
    protected void initDatum() {
        super.initDatum();

        //创建适配器
        adapter = new SheetAdapter(R.layout.item_topic);
        //这个rv是在父类初始化的
        rv.setAdapter(adapter);
    }

    @Override
    protected void initListeners() {
        super.initListeners();
        //设置item点击事件
        adapter.setOnItemClickListener((adapter, view, position) -> {
            //获取选中的数据
            Sheet data = (Sheet) adapter.getItem(position);
            //跳转到歌单详情(携带歌单id)
            startActivityExtraId(SheetDetailActivity.class, data.getId());
        });
    }

    @Override
    protected void fetchData(String data) {
        super.fetchData(data);

        LogUtil.d(TAG, "fetchData: " + data);

        Api.getInstance()
                .searchSheets(data)
                .subscribe(new HttpObserver<ListResponse<Sheet>>() {
                    @Override
                    public void onSucceeded(ListResponse<Sheet> data) {
                        LogUtil.d(TAG, "search sheets: :  " + data.getData().size());
                        //设置数据
                        adapter.replaceData(data.getData());
                    }
                });
    }

    public static SheetFragment newInstance(int position) {

        Bundle args = new Bundle();
        //传递参数
        args.putInt(Constant.ID, position);
        SheetFragment fragment = new SheetFragment();
        fragment.setArguments(args);
        return fragment;
    }
}
