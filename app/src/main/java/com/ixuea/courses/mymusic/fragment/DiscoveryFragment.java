package com.ixuea.courses.mymusic.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.ixuea.courses.mymusic.R;
import com.ixuea.courses.mymusic.adapter.DiscoveryAdapter;
import com.ixuea.courses.mymusic.api.Api;
import com.ixuea.courses.mymusic.domain.BaseMultiItemEntity;
import com.ixuea.courses.mymusic.domain.Sheet;
import com.ixuea.courses.mymusic.domain.Title;
import com.ixuea.courses.mymusic.domain.response.ListResponse;
import com.ixuea.courses.mymusic.listener.HttpObserver;

import java.util.ArrayList;
import java.util.List;

import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import butterknife.BindView;
import io.reactivex.Observable;

/**
 * 首页-发现界面
 */
public class DiscoveryFragment extends BaseCommonFragment {

    /**
     * 列表控件
     */

    @BindView(R.id.rv)
    RecyclerView rv;
    private GridLayoutManager layoutManager;
    private DiscoveryAdapter adapter;

    @Override
    protected void initViews() {
        super.initViews();

        //高度固定
        //可以提交性能
        //但由于这里是项目课程
        //所以这里不讲解
        //会在《详解RecyclerView》课程中讲解
        //http://www.ixuea.com/courses/8
        rv.setHasFixedSize(true);

        //设置显示3列
        layoutManager = new GridLayoutManager(getMainActivity(), 3);
        rv.setLayoutManager(layoutManager);

    }

    @Override
    protected void initDatum() {
        super.initDatum();

        //创建适配器
        adapter = new DiscoveryAdapter();
        rv.setAdapter(adapter);

        //设置列宽度
//        adapter.setSpanSizeLookup(new BaseQuickAdapter.SpanSizeLookup() {
//            @Override
//            public int getSpanSize(GridLayoutManager gridLayoutManager, int position) {
//                //在这里
//                //获取模型上面的宽度
//                //adapter.getItem(position):返回的是DiscoveryAdapter extends BaseMultiItemQuickAdapter<BaseMultiItemEntity, BaseViewHolder>
//                //传入的BaseMultiItemEntity泛型参数，
//                //adapter.getItem(position).getSpanSize():返回的是BaseMultiItemEntity中getSpanSize
//                //如果有子类继承BaseMultiItemEntity，就是子类的getSpanSize；而这类用的是它的子类（如Title Sheet Song）
//                return adapter.getItem(position).getSpanSize();
//            }
//        });

        //lambda写法
        adapter.setSpanSizeLookup((gridLayoutManager, position)
                -> adapter.getItem(position).getSpanSize());

        //请求数据
        fetchData();
    }

    /**
     * 请求数据
     */
    private void fetchData() {
        //因为现在还没有请求数据
        //所以添加一些测试数据
        //目的是让列表显示出来
        List<BaseMultiItemEntity> datum = new ArrayList<>();

        //添加标题
        datum.add(new Title("推荐歌单"));

//        //添加歌单数据
//        for (int i = 0; i < 9; i++) {
//            datum.add(new Sheet());
//        }
//
//        //添加标题
//        datum.add(new Title("推荐单曲"));
//
//        //添加单曲数据
//        for (int i = 0; i < 9; i++) {
//            datum.add(new Song());
//        }
//
//        //将数据设置（替换）到适配器
//        adapter.replaceData(datum);

        //歌单Api
        Observable<ListResponse<Sheet>> sheets = Api.getInstance().sheets();

        //请求歌单数据
        sheets.subscribe(new HttpObserver<ListResponse<Sheet>>() {
            @Override
            public void onSucceeded(ListResponse<Sheet> data) {
                //添加歌单数据
                datum.addAll(data.getData());
            }
        });
    }

    /**
     * 构造方法（准确说是一个静态的创建方法）
     *
     * @return 本身实例
     */
    public static DiscoveryFragment newInstance() {

        Bundle args = new Bundle();

        DiscoveryFragment fragment = new DiscoveryFragment();
        fragment.setArguments(args);
        return fragment;
    }

    /**
     * 返回布局
     */
    @Override
    protected View getLayoutView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_discovery, null);
    }
}
