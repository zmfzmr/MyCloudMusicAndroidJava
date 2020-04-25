package com.ixuea.courses.mymusic.activity;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.ixuea.courses.mymusic.R;
import com.ixuea.courses.mymusic.adapter.FriendAdapter;
import com.ixuea.courses.mymusic.api.Api;
import com.ixuea.courses.mymusic.domain.User;
import com.ixuea.courses.mymusic.domain.UserResult;
import com.ixuea.courses.mymusic.domain.response.ListResponse;
import com.ixuea.courses.mymusic.listener.HttpObserver;
import com.ixuea.courses.mymusic.util.DataUtil;
import com.ixuea.courses.mymusic.util.LogUtil;
import com.lwkandroid.widget.indexbar.IndexBar;

import java.util.List;

import androidx.appcompat.widget.SearchView;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import butterknife.BindView;

/**
 * 选择好友界面
 */
public class SelectFriendActivity extends BaseTitleActivity implements IndexBar.OnIndexLetterChangedListener {
    private String TAG = "SelectFriendActivity";
    /**
     * 列表控件
     */
    @BindView(R.id.rv)
    RecyclerView rv;
    /**
     * 字母索引控件
     */
    @BindView(R.id.ib)
    IndexBar ib;
    /**
     * 索引提示控件
     */
    @BindView(R.id.tv_index)
    TextView tv_index;

    private FriendAdapter adapter;
    /**
     * 用户数据处理结果(里面包含了分组的集合)
     */
    private UserResult userResult;
    private List<User> datum;//用户数据，这个集合list中user已经有了拼音
    private LinearLayoutManager layoutManager;//布局管理器

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
        layoutManager = new LinearLayoutManager(getMainActivity());
        rv.setLayoutManager(layoutManager);
        //分割线
        DividerItemDecoration decoration = new DividerItemDecoration(getMainActivity(), DividerItemDecoration.VERTICAL);
        rv.addItemDecoration(decoration);

        //索引默认为空(因为默认会有A到Z的字符，设置为null（传入空字符串数组）)
        ib.setTextArray(new String[]{});

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

        //添加字母索引监听器
        ib.setOnIndexLetterChangedListener(this);
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
//                        datum = data.getData();

                        //上面的那个是网络数据，我们这里自己创建数据来测试
                        datum = DataUtil.getTestUserData();

                        //处理拼音
                        datum = DataUtil.processUserPinyin(datum);
                        //设置数据
                        setData(datum);
                    }
                });
    }

    /**
     * 设置数据
     * (第一次进来网络请求和过滤数据的时候用到)
     * @param datum
     */
    private void setData(List<User> datum) {
        //处理数据(分组) (processUser:方法里面是根据拼音来分组的)
        userResult = DataUtil.processUser(datum);

        //设置到适配器
        adapter.setDatum(userResult.getDatum());

        //设置索引(这里面获取的是 字母数组(比如：[A B C])
        ib.setTextArray(userResult.getLetters());
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

        filterData(query);
    }

    /**
     * 过滤数据(本地数据过滤(这个数据已经从网络请求下来后经过添加过拼音进User对象的))
     */
    private void filterData(String query) {
        if (TextUtils.isEmpty(query)) {
            //没有关键字

            //显示完整数据
            setData(datum);//这个时候的datum没有分组
        } else {
            //关键字要转成小写
            List<User> data = DataUtil.filterUser(datum, query.toLowerCase());

            //把过滤出来的数据 分组并设置到adapter中
            setData(data);//这个data是：List<User>
        }
    }

    /**
     * 手指按下和抬起会回调这里
     *
     * @param touched 是否按下了   如果手抬起来再调用一次onTouched，并且手抬起来后，touched为false
     *
     *                总结：手指按下和抬起都会调用
     */
    @Override
    public void onTouched(boolean touched) {
        LogUtil.d(TAG, "onTouched:" + touched);
        if (touched) {
            //按下了

            //显示索引
            tv_index.setVisibility(View.VISIBLE);
        } else {
            //没有按下

            //隐藏索引
            tv_index.setVisibility(View.GONE);
        }
    }

    /**
     * 索引字母改变时会回调这里
     *
     * @param indexChar 字母
     * @param index     索引
     * @param y         按下位置垂直距离
     *
     * 比如IndexBar中设置进去的字母数组为：[A,B,C]
     *     点击第一个A，索引index为0，通过索引0，去UserResult中Indexs中找到对应的索引
     *                  注意：这里是通过(索引index)去Indexs中找(对应索引)
     */
    @Override
    public void onLetterChanged(CharSequence indexChar, int index, float y) {
        LogUtil.d(TAG, "onLetterChanged:" + indexChar + "," + index + "," + y);
        //让列表滚动到相应的位置
        //参数1：滚动到指定索引的item 参数2：偏移，这里写0表示不偏移
        //注意：滚动位置的，用哪个布局管理器，就用哪个滚动
        layoutManager.scrollToPositionWithOffset(userResult.getIndexes()[index], 0);

        //显示索引(indexChar:参数中回调的)
        tv_index.setText(indexChar);
    }
}