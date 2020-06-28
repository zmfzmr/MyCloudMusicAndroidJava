package com.ixuea.courses.mymusic.activity;

import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.google.android.material.tabs.TabLayout;
import com.ixuea.courses.mymusic.R;
import com.ixuea.courses.mymusic.adapter.SearchResultAdapter;
import com.ixuea.courses.mymusic.domain.event.OnSearchEvent;
import com.ixuea.courses.mymusic.util.LogUtil;
import com.ixuea.courses.mymusic.util.ViewUtil;

import org.apache.commons.lang3.StringUtils;
import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;

import androidx.appcompat.widget.SearchView;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;
import butterknife.BindView;

/**
 * 搜索界面
 */
public class SearchActivity extends BaseTitleActivity implements ViewPager.OnPageChangeListener {

    private static final String TAG = "SearchActivity";
    private String query;//当前搜索内容(可以是关键字或者其他)
    /**
     * 列表控件
     */
    @BindView(R.id.rv)
    RecyclerView rv;

    /**
     * 搜索结果 容器
     */
    @BindView(R.id.search_result_container)
    View search_result_container;
    /**
     * 指示器
     */
    @BindView(R.id.tl)
    TabLayout tl;
    /**
     * 搜索结果左右滚动控件
     */
    @BindView(R.id.vp)
    ViewPager vp;

    /**
     * 搜索控件
     */
    private SearchView searchView;//搜索控件
    private SearchResultAdapter searchResultAdapter;//搜索结果适配器
    private int selectedIndex;//当前显示的搜索结果界面索引

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
    }

    @Override
    protected void initView() {
        super.initView();
        //初始化RecyclerView
        ViewUtil.initVerticalLinearRecyclerView(getMainActivity(), rv);
    }

    @Override
    protected void initDatum() {
        super.initDatum();

        //创建适配器
        searchResultAdapter = new SearchResultAdapter(getMainActivity(), getSupportFragmentManager());

        //设置搜索结果适配器
        vp.setAdapter(searchResultAdapter);

        //创建占位数据
        //当然也可以在SearchResultAdapter中的getCount返回数量3或者其他数量，可以不用写这些占位数据了
        //当然我们这里还是写一下
        ArrayList<Integer> datum = new ArrayList<>();
        datum.add(0);
        datum.add(1);
        datum.add(2);
        datum.add(3);
        datum.add(4);
        datum.add(5);
        datum.add(6);

        //设置数据
        searchResultAdapter.setDatum(datum);

        //让指示器和ViewPager配合工作
        tl.setupWithViewPager(vp);
    }

    @Override
    protected void initListeners() {
        super.initListeners();
        //设置搜索结果滚动界面监听器
        vp.setOnPageChangeListener(this);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.search, menu);

        //查找搜索按钮
        MenuItem searchItem = menu.findItem(R.id.action_search);

        //查找搜索控件
        searchView = (SearchView) searchItem.getActionView();

        //可以在这里配置SearchView

        //可以搜索监听器
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            /**
             * 提交了搜索
             * 回车搜索调用两次
             * 点击键盘上搜索
             * @param query
             * @return
             */
            @Override
            public boolean onQueryTextSubmit(String query) {
                performSearch(query);
                return true;
            }

            /**
             * 搜索输入框文本改变了
             * @param newText
             * @return
             */
            @Override
            public boolean onQueryTextChange(String newText) {
                //搜索建议(这个去服务端获取)
                fetchSuggestion(newText);
                return true;
            }
        });

        //是否进入界面就打开搜索栏
        //false为默认打开
        //默认为true
        searchView.setIconified(false);

        return super.onCreateOptionsMenu(menu);
    }

    private void fetchSuggestion(String data) {
        LogUtil.d(TAG, "fetchSuggestion:  " + data);
    }

    /**
     * 执行搜索
     *
     * @param data 搜索关键字
     */
    private void performSearch(String data) {
        this.query = data;

        if (StringUtils.isEmpty(data)) {
            //没有数据直接返回
            return;
        }

        LogUtil.d(TAG, "performSearch");

        //发布搜索Key  query:搜索关键字  selectedIndex：滚动tab标签后保存的索引(成员变量，默认为0)
        EventBus.getDefault().post(new OnSearchEvent(query, selectedIndex));
    }

    //ViewPager监听器

    /**
     * 滚动中
     */
    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

    }

    /**
     * 滚动完成了
     */
    @Override
    public void onPageSelected(int position) {
        //保存索引
        selectedIndex = position;

        //执行搜索(这里第一次搜搜的时候保存的query(搜索文字)，
        // 这里滚动到另一个tab标签后，这个query是上一次搜索的那个query)
        performSearch(query);
    }

    /**
     * 滚动状态改变了
     */
    @Override
    public void onPageScrollStateChanged(int state) {

    }
    //end ViewPager监听器
}
