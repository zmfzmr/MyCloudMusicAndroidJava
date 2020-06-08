package com.ixuea.regionselector;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.ixuea.regionselector.db.RegionDao;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import static com.ixuea.regionselector.RegionSelector.REGION_AREA;
import static com.ixuea.regionselector.RegionSelector.REGION_CITY;

/**
 * 地区选择
 */
public class RegionSelectActivity extends AppCompatActivity implements BaseQuickAdapter.OnItemClickListener {

    private RecyclerView mRecyclerView;
    private RegionAdapter mAdapter;
    private RegionDao mRegionDao;

    private List<Region> mList;

    private List<Region> mProvinceList;
    private List<Region> mCityList;
    private List<Region> mAreaList;
    private int state = 0;

    private Region mProvince;
    private Region mCity;
    private Region mArea;
    private Toolbar toolbar;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_region);

        toolbar = findViewById(R.id.toolbar);
        //初始化Toolbar
        setSupportActionBar(toolbar);

        //是否显示返回按钮
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        mRegionDao = new RegionDao(this);

        mList = new ArrayList<>();
        mAdapter = new RegionAdapter(mList);
        mAdapter.setOnItemClickListener(this);

        mRecyclerView = (RecyclerView) findViewById(R.id.rv);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mRecyclerView.setAdapter(mAdapter);


        setTitle("选择省份");
        mProvinceList = mRegionDao.loadProvinceList();
        mAdapter.addData(mProvinceList);
    }

    /**
     * 完成
     */
    private void finishSelect() {
        Intent data = new Intent();
        data.putExtra(RegionSelector.REGION_PROVINCE, mProvince);
        data.putExtra(REGION_CITY, mCity);
        data.putExtra(REGION_AREA, mArea);

        setResult(RESULT_OK, data);
        finish();
    }

    @Override
    public void onBackPressed() {
        if (state == 0) {
            super.onBackPressed();
        }

        if (state == 1) {
            setTitle("选择省份");
            mList.clear();
            mAdapter.addData(mProvinceList);
            state--;
        } else if (state == 2) {
            setTitle("选择城市");
            mList.clear();
            mAdapter.addData(mCityList);
            state--;
        }
    }

    @Override
    public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
        Region region = mAdapter.getItem(position);

        if (state == 0) {
            setTitle("选择城市");
            mCityList = mRegionDao.loadCityList(region.getId());

            mList.clear();
            mAdapter.addData(mCityList);

            mProvince = region;
            state++;
        } else if (state == 1) {
            setTitle("选择地区");
            mAreaList = mRegionDao.loadCountyList(region.getId());
            mCity = region;

            if (mAreaList.size() == 0) {
                //防止有的城市没有县级
                finishSelect();

            } else {
                mList.clear();
                mAdapter.addData(mAreaList);

                state++;
            }


        } else if (state == 2) {
            mArea = region;
            state++;

            finishSelect();
        }
    }

    /**
     * 菜单点击了回调
     *
     * @param item
     * @return
     */
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                //Toolbar返回按钮点击
                finish();
                break;
        }
        return super.onOptionsItemSelected(item);
    }
}
