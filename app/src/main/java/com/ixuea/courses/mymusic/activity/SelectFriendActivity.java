package com.ixuea.courses.mymusic.activity;

import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import com.ixuea.courses.mymusic.R;
import com.ixuea.courses.mymusic.adapter.FriendAdapter;
import com.ixuea.courses.mymusic.api.Api;
import com.ixuea.courses.mymusic.domain.User;
import com.ixuea.courses.mymusic.domain.UserResult;
import com.ixuea.courses.mymusic.domain.response.ListResponse;
import com.ixuea.courses.mymusic.listener.HttpObserver;
import com.ixuea.courses.mymusic.util.DataUtil;
import com.ixuea.courses.mymusic.util.LogUtil;

import java.util.List;

import androidx.appcompat.widget.SearchView;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import butterknife.BindView;

/**
 * 选择好友界面
 */
public class SelectFriendActivity extends BaseTitleActivity {
    private String TAG = "SelectFriendActivity";
    /**
     * 列表控件
     */
    @BindView(R.id.rv)
    RecyclerView rv;
    private FriendAdapter adapter;
    /**
     * 用户数据处理结果(里面包含了分组的集合)
     */
    private UserResult userResult;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_friend);
    }

    @Override
    protected void initView() {
        super.initView();
        //设置尺寸固定
        rv.setHasFixedSize(true);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getMainActivity());
        rv.setLayoutManager(layoutManager);
        //分割线
        DividerItemDecoration decoration = new DividerItemDecoration(getMainActivity(), DividerItemDecoration.VERTICAL);
        rv.addItemDecoration(decoration);

    }

    @Override
    protected void initDatum() {
        super.initDatum();

        //创建适配器
//        adapter = new FriendAdapter(R.layout.item_user);
        adapter = new FriendAdapter(getMainActivity());
        //设置适配器
        rv.setAdapter(adapter);

        //请求数据
        fetchData();
    }

    @Override
    protected void initListeners() {
        super.initListeners();

//        //设置item监听器
//        adapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
//            @Override
//            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
//                //获取点击的数据
//                User data = (User) adapter.getItem(position);
//
//                //发送数据
//                EventBus.getDefault().post(new SelectedFriendEvent(data));
//
//                //关闭界面
//                finish();
//            }
//        });
    }

    private void fetchData() {
        //sp.getUserId()：保存到持久化的用户的id
        Api.getInstance()
                .friends(sp.getUserId())
                .subscribe(new HttpObserver<ListResponse<User>>() {
                    @Override
                    public void onSucceeded(ListResponse<User> data) {
                        //设置数据
//                        adapter.replaceData(data.getData());

                        //获取( 好友列表(我关注的人) 集合)
                        List<User> datum = data.getData();
                        //处理拼音
                        datum = DataUtil.processUserPinyin(datum);
                        //设置数据
                        setData(datum);
                    }
                });
    }

    /**
     * 设置数据
     *
     * @param datum
     */
    private void setData(List<User> datum) {
        //处理数据(分组) (processUser:方法里面是根据拼音来分组的)
        userResult = DataUtil.processUser(datum);

        //设置到适配器
        adapter.setDatum(userResult.getDatum());
    }

    /**
     * 创建菜单方法
     */
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        //返回men布局
        getMenuInflater().inflate(R.menu.search, menu);
        //找到搜索按钮
        MenuItem searchItem = menu.findItem(R.id.action_search);

        //这里找到的menu 布局里面的那个app:actionViewClass="androidx.appcompat.widget.SearchView" 类
        SearchView searchView = (SearchView) searchItem.getActionView();

        //在这里就可以配置SearchView

        //搜索监听器
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            /**
             * 提交了搜索
             *
             * 在输入框中按回车
             * 点击了键盘上的搜索按钮
             * @param query
             * @return
             */
            @Override
            public boolean onQueryTextSubmit(String query) {
                LogUtil.d(TAG, "onQueryTextSubmit");
                onSearchTextChanged(query);

                return true;//true表示处理了事件
            }

            /**
             * 搜索框内容改变了(键盘点击搜索按钮的时候也会调用这个方法)
             * @param newText
             * @return
             */
            @Override
            public boolean onQueryTextChange(String newText) {
                LogUtil.d(TAG, "onQueryTextChange");
                onSearchTextChanged(newText);
                return true;////true表示处理了事件
            }
        });

        //是否进入界面就打开搜索栏，false为默认打开，默认为true
        searchView.setIconified(false);
        //搜索提示
        searchView.setQueryHint(getString(R.string.hint_search_user));


        //在搜索按钮里面查询搜索控件
        return true;//这里设置返回true
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.action_search) {
            //如果不需要特殊处理
            //直接返回true就行了
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    /**
     * 搜索我的好友
     * 一般情况下好友数量比较少
     * 可以直接在本地过滤
     * 所以我们这里就讲解本地过滤
     * 如果是网络搜索
     * 那其实也是调用一个接口而已
     * 所以也没多大难度
     */
    private void onSearchTextChanged(String query) {
        LogUtil.d(TAG, "onSearchTextChanged");
    }
}