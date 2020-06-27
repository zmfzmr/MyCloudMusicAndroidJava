package com.ixuea.courses.mymusic.activity;

import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.google.android.material.tabs.TabLayout;
import com.ixuea.courses.mymusic.R;
import com.ixuea.courses.mymusic.util.LogUtil;
import com.ixuea.courses.mymusic.util.ViewUtil;

import androidx.appcompat.widget.SearchView;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;
import butterknife.BindView;

/**
 * 搜索界面
 */
public class SearchActivity extends BaseTitleActivity {

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
     * @param query
     */
    private void performSearch(String query) {
        this.query = query;

        LogUtil.d(TAG, "performSearch");
    }
}
