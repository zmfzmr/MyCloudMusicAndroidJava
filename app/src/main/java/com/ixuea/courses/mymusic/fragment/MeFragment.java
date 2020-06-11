package com.ixuea.courses.mymusic.fragment;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ExpandableListView;

import com.ixuea.courses.mymusic.R;
import com.ixuea.courses.mymusic.activity.DownloadActivity;
import com.ixuea.courses.mymusic.activity.LocalMusicActivity;
import com.ixuea.courses.mymusic.activity.SheetDetailActivity;
import com.ixuea.courses.mymusic.adapter.MeAdapter;
import com.ixuea.courses.mymusic.api.Api;
import com.ixuea.courses.mymusic.domain.Sheet;
import com.ixuea.courses.mymusic.domain.event.CreateSheetClickEvent;
import com.ixuea.courses.mymusic.domain.event.CreateSheetEvent;
import com.ixuea.courses.mymusic.domain.event.SheetChangedEvent;
import com.ixuea.courses.mymusic.domain.response.DetailResponse;
import com.ixuea.courses.mymusic.domain.response.ListResponse;
import com.ixuea.courses.mymusic.domain.ui.MeGroup;
import com.ixuea.courses.mymusic.listener.HttpObserver;
import com.ixuea.courses.mymusic.listener.ObserverAdapter;
import com.ixuea.courses.mymusic.util.HttpUtil;
import com.ixuea.courses.mymusic.util.LogUtil;
import com.ixuea.courses.mymusic.util.ToastUtil;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.List;

import butterknife.OnClick;
import io.reactivex.Observable;
import io.reactivex.functions.BiFunction;

/**
 * 首页-我的界面
 * <p>
 * 他可以实现列表展开功能，RecyclerView也可以实现折叠效果，
 * 只是要自己实现，或者使用第三方框架；顶部是通过头部实现的。
 */
public class MeFragment extends BaseCommonFragment implements ExpandableListView.OnChildClickListener {

    private static final String TAG = "MeFragment";
    /**
     * 列表控件
     */
//    @BindView(R.id.elv)//这个先注释了，因为我们isBindView置为了false，下面我们手动绑定view(findViewById找）
    ExpandableListView elv;
    private MeAdapter adapter;

    @Override
    protected void initViews() {
        super.initViews();

        //手动找一个控件
        //因为要添加header
        elv = findViewById(R.id.elv);
        //添加头部控件
        //参数3：表示不自动添加父容器（这里是ExpandableListView父容器(根布局)），我们自己手动添加headerView到父布局里面
        View headerView = LayoutInflater.from(getMainActivity()).inflate(R.layout.header_me, elv, false);

        elv.addHeaderView(headerView);
        //手动绑定控件
        //目的是添加了header后
        //在绑定
        //就可以查找到头部布局中的控件(头部成为了ExpandableListView里面的一部分)

        // 里面逻辑是：(ButterKnife.bind(this, getView());)
        bindView();
    }

    /**
     * @return false:表示父类就不调用bindView方法(父类里面的方法)，我们这里手动调用
     */
    @Override
    protected boolean isBindView() {
        return false;
    }

    @Override
    protected void initDatum() {
        super.initDatum();

        //注册
        EventBus.getDefault().register(this);

        //创建适配器
        adapter = new MeAdapter(getMainActivity());

        //设置适配器
        elv.setAdapter(adapter);
        fetchData();

    }

    @SuppressLint("CheckResult")
    private void fetchData() {
        //数据类表
        ArrayList<MeGroup> datum = new ArrayList<>();

        //创建歌单请求
        Observable<ListResponse<Sheet>> createSheetApi = Api.getInstance()
                .createSheets(sp.getUserId());
        //收藏到的歌单请求
        Observable<ListResponse<Sheet>> collectSheetsApi = Api.getInstance()
                .collectSheets(sp.getUserId());

        //普通的方式
        //请求是线性(一个人干活，干完了另外一个人干)
        //(外请求：获取用户创建的歌单 内请求：获取用户收藏的歌单)
//        createSheetApi.subscribe(new HttpObserver<ListResponse<Sheet>>() {
//            @Override
//            public void onSucceeded(ListResponse<Sheet> data) {
//                //添加数据
//                //这里MeGroup 参数3：是否显示右侧按钮 这是是创建的歌单，就显示右侧按钮
//                datum.add(new MeGroup("创建的歌单", data.getData(), true));
//
//                collectSheetsApi.subscribe(new HttpObserver<ListResponse<Sheet>>() {
//                    @Override
//                    public void onSucceeded(ListResponse<Sheet> data) {
//                        //添加数据(这里是收藏歌单，所以MeGroup 参数3：表示不显示右侧按钮)
//                        //这里传入了false，表示不显示右侧按钮
//                        datum.add(new MeGroup("收藏的歌单", data.getData(), false));
//
//                        //设置数据到适配器
//                        LogUtil.d(TAG, "get sheets success: " + datum.size());
//
//                        adapter.setDatum(datum);
//
//                        //展开所有组(显示完数据后才展开所有的组)
//                        expandedAll();
//                    }
//                });
//            }
//        });


        //改成并发模式
        //使用RxAndroid(RxJava 的封装 更适合android项目)
        //将两个请求实现为并发
        //两个请求都请求成功后回调

        //第三个对象：泛型参数1:也就是zip(第一个createSheetApi返回来的模型对象) 依次类推 泛型参数2也是一样的
        //           泛型参数3：你想要转换的格式类型(意思说：把网络请求到的数据转换成这个集合对象的类型)
        Observable.zip(createSheetApi, collectSheetsApi,
                new BiFunction<ListResponse<Sheet>, ListResponse<Sheet>, List<MeGroup>>() {
                    /**
                     * 所有任务都执行成功了回调
                     *
                     * @param createSheets
                     * @param collectSheets
                     * @return
                     * @throws Exception
                     */
                    @Override
                    public List<MeGroup> apply(ListResponse<Sheet> createSheets,
                                               ListResponse<Sheet> collectSheets) throws Exception {
                        //请求成功了

                        //只要有一个请求失败了
                        //就直接走到onError

                        //将数据放到列表中

                        //也可以包装为对象
                        //或者使用操作符
                        //添加数据
                        List<MeGroup> datum = new ArrayList<>();
                        //添加创建的歌单和收藏的歌单 true:右边的创建歌单按钮
                        datum.add(new MeGroup("创建的歌单", createSheets.getData(), true));
                        datum.add(new MeGroup("收藏的歌单", collectSheets.getData(), false));
                        return datum;
                    }
                }).subscribeWith(new ObserverAdapter<List<MeGroup>>() {
            //注意： 这里用的是 subscribeWith

            //调用zip方法不会请求网络数据，只有调用这个subscribeWith后才会去请求网络数据
            //请求成功后会回调zip 方法里面的apply方法(这个方法返回的数据又回调到这里的onNext中)

            /**
             * 成功调用
             */
            @Override
            public void onNext(List<MeGroup> results) {
                super.onNext(results);

                //设置适配器数据
                adapter.setDatum(results);
                //展开所有组
                expandedAll();
            }

            /**
             * 失败调用
             * <p>
             * 因为我们用的是ObserverAdapter(避免重写多个方法)，不是用HttpObserver(这个类处理了错误)
             * <p>
             * 我们这ObserverAdapter没有处理错误，所以我们在onError中处理下错误
             *
             * @param e Throwable对象
             */
            @Override
            public void onError(Throwable e) {
                super.onError(e);
                //参数1：数据模型对象  因为我们前面的把数据模型对象转成List<MeGroup>了，所以这里写null
                HttpUtil.handlerRequest(null,e);
            }
        });
    }

    /**
     * 展开所有组
     */
    private void expandedAll() {
        //adapter.getGroupCount():组的个数
        for (int i = 0; i < adapter.getGroupCount(); i++) {
            elv.expandGroup(i);
        }
    }

    @Override
    protected void initListeners() {
        super.initListeners();
        //设置子item点击监听器
        //注意：是有个child的
        elv.setOnChildClickListener(this);
    }

    /**
     * 构造方法
     * 固定写法
     *
     * @return MeFragment本身
     */
    public static MeFragment newInstance() {

        Bundle args = new Bundle();

        MeFragment fragment = new MeFragment();
        fragment.setArguments(args);
        return fragment;
    }

    /**
     * 返回布局文件
     *
     * @param inflater           LayoutInflater
     * @param container          ViewGroup
     * @param savedInstanceState Bundle
     * @return
     */
    @Override
    protected View getLayoutView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_me, null);
    }

    /**
     * 子元素点击回调
     *
     * @param parent
     * @param v
     * @param groupPosition
     * @param childPosition
     * @param id
     * @return true:我们处理了事件
     */
    @Override
    public boolean onChildClick(ExpandableListView parent, View v, int groupPosition, int childPosition, long id) {
        //获取点击的数据
        Sheet data = adapter.getChildData(groupPosition, childPosition);

        //跳转到详情(这里是携带一个歌单id获取的)
        startActivityExtraId(SheetDetailActivity.class, data.getId());
        return true;//记得返回true
    }

    /**
     * 点击了歌单创建按钮事件
     *
     * 总路线：item(适配器)-->MeFragment->显示CreateSheetDialogFragment(在回调onCreateSheetClickEvent中)
     */
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onCreateSheetClickEvent(CreateSheetClickEvent event) {
        LogUtil.d(TAG, "onCreateSheetClickEvent");
        //如果在Fragment中显示fragment
        //要使用getChildFragmentManager方法
        CreateSheetDialogFragment.show(getChildFragmentManager());
    }

    /**
     * 创建歌单事件（点击了对话框中的确认按钮 回来的事件回调）
     *
     * @param event
     */
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onCreateSheetEvent(CreateSheetEvent event) {
        LogUtil.d(TAG, "onCreateSheetEvent: " + event.getData());

        Sheet data = new Sheet();

        //这里不要传用户id
        //不然这就是一个漏洞
        //就可以给任何人创建歌单
        //而是服务端根据token获取用户信息(因为该用户登录成功后，会有一个token值，服务器根据token判断是哪个用户创建的歌单)
        data.setTitle(event.getData());

        Api.getInstance()
                .createSheet(data)
                .subscribe(new HttpObserver<DetailResponse<Sheet>>() {
                    @Override
                    public void onSucceeded(DetailResponse<Sheet> data) {
                        ToastUtil.successShortToast(R.string.success_create_sheet);

                        //重写加载数据
                        fetchData();
                    }
                });
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onSheetChangedEvent(SheetChangedEvent event) {
        //重新加载数据
        fetchData();
    }

    /**
     * 界面销毁时调用
     */
    @Override
    public void onDestroy() {
        //解除注册
        EventBus.getDefault().unregister(this);
        super.onDestroy();
    }

    /**
     * 本地音乐点击
     */
    @OnClick(R.id.ll_local_music)
    public void onLocalMusicClick() {
        LogUtil.d(TAG, "onLocalMusicClick: ");

        startActivity(LocalMusicActivity.class);
    }

    /**
     * 下载管理
     */
    @OnClick(R.id.ll_download_manager)
    public void onDownloadManagerClick() {
        LogUtil.d(TAG, "onDownloadManagerClick: ");
        //跳转到 下载管理 界面
        startActivity(DownloadActivity.class);
    }
}
