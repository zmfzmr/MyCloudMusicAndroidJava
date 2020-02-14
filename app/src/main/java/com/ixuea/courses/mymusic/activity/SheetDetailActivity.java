package com.ixuea.courses.mymusic.activity;

import android.os.Bundle;

import com.ixuea.courses.mymusic.R;
import com.ixuea.courses.mymusic.adapter.SongAdapter;
import com.ixuea.courses.mymusic.api.Api;
import com.ixuea.courses.mymusic.domain.Sheet;
import com.ixuea.courses.mymusic.domain.response.DetailResponse;
import com.ixuea.courses.mymusic.listener.HttpObserver;
import com.ixuea.courses.mymusic.util.Constant;
import com.ixuea.courses.mymusic.util.LogUtil;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import butterknife.BindView;

/**
 * 歌单详情界面
 */
public class SheetDetailActivity extends BaseTitleActivity {

    private static final String TAG = "SheetDetailActivity";

    /**
     * 列表控件
     */
    @BindView(R.id.rv)
    RecyclerView rv;

    /**
     * 歌单Id
     */
    private String id;
    private Sheet data;//歌单对象数据
    private SongAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sheet_detail);
    }

    @Override
    protected void initView() {
        super.initView();

        //尺寸固定
        rv.setHasFixedSize(true);

        //设置布局管理器(这里是线性布局管理器)
        LinearLayoutManager layoutManager = new LinearLayoutManager(getMainActivity());
        //设置管理器
        rv.setLayoutManager(layoutManager);
    }

    @Override
    protected void initDatum() {
        super.initDatum();

        //获取传递的参数
        getIntent().getStringExtra(Constant.ID);

        //使用重构后的方法
        id = extraId();

        //创建适配器
        adapter = new SongAdapter(R.layout.item_sheet_detail);

        //设置适配器
        rv.setAdapter(adapter);
        //这里RecyclerView在前面，请求网络数据在后

        //请求数据
        fetchData();
    }

    /**
     * //请求数据
     */
    private void fetchData() {
        Api.getInstance()
                .sheetDetail(id)
                .subscribe(new HttpObserver<DetailResponse<Sheet>>() {
                    @Override
                    public void onSucceeded(DetailResponse<Sheet> data) {
                        next(data.getData());
                    }
                });
    }

    /**
     * 显示数据
     *
     * @param data Sheet数据
     */
    private void next(Sheet data) {
        this.data = data;

        LogUtil.d(TAG, "next:" + data);
        if (data.getSongs() != null && data.getSongs().size() > 0) {
            //有音乐才设置
            //设置数据
            adapter.replaceData(data.getSongs());
        }
    }
}