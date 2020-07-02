package com.ixuea.courses.mymusic.activity;

import android.app.SearchManager;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.google.android.material.tabs.TabLayout;
import com.ixuea.courses.mymusic.R;
import com.ixuea.courses.mymusic.adapter.SearchHistoryAdapter;
import com.ixuea.courses.mymusic.adapter.SearchResultAdapter;
import com.ixuea.courses.mymusic.api.Api;
import com.ixuea.courses.mymusic.domain.SearchHistory;
import com.ixuea.courses.mymusic.domain.SearchTitle;
import com.ixuea.courses.mymusic.domain.Suggest;
import com.ixuea.courses.mymusic.domain.event.OnSearchEvent;
import com.ixuea.courses.mymusic.domain.response.DetailResponse;
import com.ixuea.courses.mymusic.listener.HttpObserver;
import com.ixuea.courses.mymusic.util.KeyBoardUtil;
import com.ixuea.courses.mymusic.util.LiteORMUtil;
import com.ixuea.courses.mymusic.util.LogUtil;
import com.ixuea.courses.mymusic.util.ViewUtil;
import com.nex3z.flowlayout.FlowLayout;

import org.apache.commons.lang3.StringUtils;
import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;
import java.util.List;

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
    private LiteORMUtil orm;//数据库框架工具类
    private SearchHistoryAdapter searchHistoryAdapter;
    private FlowLayout fl;//标签流
    private SearchView.SearchAutoComplete searchAutoComplete;//搜索建议控件
    private ArrayAdapter<String> suggestAdapter;//搜索建议适配器

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

        //初始化数据库
        orm = LiteORMUtil.getInstance(getApplicationContext());

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

        //创建搜索历史适配器
        searchHistoryAdapter = new SearchHistoryAdapter(R.layout.item_search_history);

        searchHistoryAdapter.addHeaderView(createHeaderView());
        //设置搜索历史适配器
        rv.setAdapter(searchHistoryAdapter);

        //获取热门搜索数据
        fetchPopularData();

        //已进入界面，也要获取一次搜索历史
        fetchSearchHistoryData();
    }

    /**
     * 搜索热门搜索数据
     */
    private void fetchPopularData() {
        //添加测试数据
        //因为服务端没有实现热门搜索功能(这个热门搜索有可能很复杂，服务端没有实现，这里只负责在客户端展示即可)
        List<String> datum = new ArrayList<>();
        datum.add("爱学啊");
        datum.add("我的云音乐");
        datum.add("Android项目实战");
        datum.add("人生苦短");
        datum.add("我们只做好课");
        datum.add("android开发");
        datum.add("项目课程");
        //设置热门搜索数据 popular:受欢迎 （热门的，也可以用这个词替代）
        setPopularData(datum);
    }

    /**
     * 设置热门搜索数据
     */
    private void setPopularData(List<String> datum) {
        for (String data : datum) {
            //循环每一个数据

            //创建布局 item_tag： 流式标签的item布局
            TextView tv = (TextView) View.inflate(getMainActivity(), R.layout.item_tag, null);
            //设置数据
            tv.setText(data);

            //设置点击事件
            tv.setOnClickListener(v -> {
                //这里用的是 复用前面点击搜索历史 item 用的那个方法
                setSearchData(data);
            });

            //添加控件(FlowLayout里面添加控件)
            fl.addView(tv);
        }
    }

    /**
     * 创建header
     *
     * @return
     */
    private View createHeaderView() {
        //从布局加载view  这个参数3根布局
        //这种情况第三个参数不能写rv，这样会报错，换getLayoutInflater这种写法
//        View view = View.inflate(getMainActivity(), R.layout.header_search, rv);
        //参数3：在SheetDetailActivity那边写的是(ViewGroup) rv.getParent()，
        //所以写 (ViewGroup) rv.getParent() 和 rv 都是可以的
        //因为item布局: header_search 写rv或者(ViewGroup) rv.getParent()都是可以的
        View view = getLayoutInflater().inflate(R.layout.header_search, rv, false);

        //查找标签流id
        fl = view.findViewById(R.id.fl);
        return view;
    }

    @Override
    protected void initListeners() {
        super.initListeners();
        //设置搜索结果滚动界面监听器
        vp.setOnPageChangeListener(this);

        //设置搜索历史item点击事件
        searchHistoryAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                //获取点击的数据
                SearchHistory data = (SearchHistory) adapter.getItem(position);
                setSearchData(data.getContent());
            }
        });

        //child点击事件  注意:  是child
        searchHistoryAdapter.setOnItemChildClickListener(new BaseQuickAdapter.OnItemChildClickListener() {
            @Override
            public void onItemChildClick(BaseQuickAdapter adapter, View view, int position) {
                //获取点击的数据
                SearchHistory data = (SearchHistory) adapter.getItem(position);

                //从数据库中删除这个历史记录
                orm.deleteSearchHistory(data);

                //从适配中删除
                adapter.remove(position);
            }
        });

    }

    /**
     * 设置搜索数据并搜索
     *
     * @param data
     */
    private void setSearchData(String data) {
        //将内容设置到搜索控件
        //并马上执行搜索  true:表示马上执行搜索
        searchView.setQuery(data, true);

        //        //进入搜索状态
        searchView.setIconified(false);

        //隐藏软键盘
        KeyBoardUtil.hideKeyBoard(getMainActivity());
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

        //设置关闭监听器
        searchView.setOnCloseListener(new SearchView.OnCloseListener() {
            @Override
            public boolean onClose() {
                //显示搜索历史
                showSearchHistoryView();
                return false;
            }
        });

        //查找搜索建议控件
        searchAutoComplete = searchView.findViewById(androidx.appcompat.R.id.search_src_text);

        //默认要输入两个字符才显示提示，可以这样更改
        searchAutoComplete.setThreshold(1);

        //获取搜索管理器 SEARCH_SERVICE: Context里面的字段常量
        SearchManager searchManager = (SearchManager) getSystemService(SEARCH_SERVICE);

        //设置搜索信息(searchView: 搜索的那个控件)
        searchView.setSearchableInfo(searchManager.getSearchableInfo(getComponentName()));

        //设置搜索建议点击回调
        //suggestAdapter: ArrayAdapter<String>  //搜索建议适配器
        //suggestAdapter.getItem(position): 获取适配器里面的搜索数据(String类型)
        searchAutoComplete.setOnItemClickListener((parent, view, position, id)
//                -> performSearch(suggestAdapter.getItem(position)));
                -> setSearchData(suggestAdapter.getItem(position)));

        return super.onCreateOptionsMenu(menu);
    }

    private void fetchSuggestion(String data) {
        LogUtil.d(TAG, "fetchSuggestion:  " + data);
        Api.getInstance()
                .searchSuggest(data)
                .subscribe(new HttpObserver<DetailResponse<Suggest>>() {
                    @Override
                    public void onSucceeded(DetailResponse<Suggest> data) {
                        setSuggest(data.getData());
                    }
                });

    }

    //设置搜索建议
    private void setSuggest(Suggest data) {
        LogUtil.d(TAG, "setSuggest: " + data);

        //处理搜索建议数据

        //像变换这个中操作
        //如果是Kotlin语言中就一句话的事
        List<String> datum = new ArrayList<>();

        //处理歌单搜索建议  不等于null表示有这个歌单搜索建议数据
        if (data.getSheets() != null) {
            for (SearchTitle title : data.getSheets()) {
                datum.add(title.getTitle());
            }
        }

        //处理用户搜索建议
        if (data.getUsers() != null) {
            for (SearchTitle title : data.getUsers()) {
                datum.add(title.getTitle());
            }
        }

        //创建适配器  参数3：我显示控件的时候，这个控件的id是哪个
        //ArrayAdapter: 这个是系统里面的类
        suggestAdapter = new ArrayAdapter<>(getMainActivity(),
                R.layout.item_suggest,
                R.id.tv_title,
                datum);

        //设置到控件
        //searchAutoComplete: SearchView.SearchAutoComplete //搜索建议控件
        searchAutoComplete.setAdapter(suggestAdapter);
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

//        //发布搜索Key  query:搜索关键字  selectedIndex：滚动tab标签后保存的索引(成员变量，默认为0)
//        EventBus.getDefault().post(new OnSearchEvent(query, selectedIndex));

        //保存搜索历史
        SearchHistory searchHistory = new SearchHistory();
        searchHistory.setContent(data);//data: 搜索关键字
        searchHistory.setCreated_at(System.currentTimeMillis());

        //保存(搜索历史)  (里面是调用了：  orm.save(data) )
        orm.createOrUpdate(searchHistory);

        //获取搜索历史()
        //显示搜索历史界面的时候再获取
        //效率更高
        fetchSearchHistoryData();

        //显示搜索结果控件
        showSearchResultView();

        //发布搜索Key  query:搜索关键字  selectedIndex：滚动tab标签后保存的索引(成员变量，默认为0)
        EventBus.getDefault().post(new OnSearchEvent(query, selectedIndex));
    }

    /**
     * 获取搜索历史
     */
    private void fetchSearchHistoryData() {
        //查询搜索历史  按照： created_at 字段排序
        // orm: LiteORMUtil对象
        List<SearchHistory> datum = orm.querySearchHistory();

        LogUtil.d(TAG, "fetchSearchHistoryData: " + datum.size());
        //设置到适配器
        searchHistoryAdapter.replaceData(datum);
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

    /**
     * 显示(搜索结果) 控件
     */
    private void showSearchResultView() {
        //显示搜索结果容器  隐藏rv(搜索历史)
        search_result_container.setVisibility(View.VISIBLE);
        rv.setVisibility(View.GONE);
    }

    /**
     * 显示(搜索历史) 控件
     */
    private void showSearchHistoryView() {
        search_result_container.setVisibility(View.GONE);
        rv.setVisibility(View.VISIBLE);
    }

    /**
     * 菜单点击了
     *
     * @param item
     * @return
     */
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                //返回按钮点击
                onBackPressed();
                break;
            default:
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    /**
     * 物理按键返回调用
     */
    @Override
    public void onBackPressed() {
        if (searchView.isIconified()) {
            //不是在搜索状态

            //正常返回
            super.onBackPressed();
        } else {
            //是搜索状态

            //关闭搜索状态  默认false：开启   true: 关闭
            searchView.setIconified(true);
        }
    }
}
