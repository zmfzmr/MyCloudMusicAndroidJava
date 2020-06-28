package com.ixuea.courses.mymusic.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.ixuea.courses.mymusic.R;
import com.ixuea.courses.mymusic.domain.event.OnSearchEvent;
import com.ixuea.courses.mymusic.util.Constant;
import com.ixuea.courses.mymusic.util.ViewUtil;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import androidx.recyclerview.widget.RecyclerView;
import butterknife.BindView;

/**
 * 通过搜索fragment逻辑
 * <p>
 * abstract：表示不能创建实例，必须继承我写其他逻辑
 */
public abstract class BaseSearchFragment extends BaseCommonFragment {
    /**
     * 列表控件
     */
    @BindView(R.id.rv)
    RecyclerView rv;

    private int index;//当前界面索引

    @Override
    protected void initViews() {
        super.initViews();
        //初始化列表控件
        ViewUtil.initVerticalLinearRecyclerView(getMainActivity(), rv);
    }

    @Override
    protected void initDatum() {
        super.initDatum();

        //获取索引
        index = extraInt(Constant.ID);

        //注册
        EventBus.getDefault().register(this);
    }

    /**
     * 返回布局
     */
    @Override
    protected View getLayoutView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_sheet, container, false);
    }

    /**
     * 搜索事件
     *
     * @param event
     */
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onSearchEvent(OnSearchEvent event) {
        //event.getSelectedIndex():跳转到tab后保存发送过来的索引
        //index:当前界面索引(ViewPager那个成绩当前item的时候发送过来的)
        if (index == event.getSelectedIndex()) {
            //只有索引一样才搜索
            //这样可以避免同时搜索多个界面 event.getData(): 搜索关键字
            fetchData(event.getData());
        }
    }

    /**
     * 执行搜索
     *
     * @param data
     */
    protected void fetchData(String data) {

    }

    @Override
    public void onDestroy() {
        EventBus.getDefault().unregister(this);
        super.onDestroy();
    }
}
