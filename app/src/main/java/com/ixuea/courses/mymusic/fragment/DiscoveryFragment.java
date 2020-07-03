package com.ixuea.courses.mymusic.fragment;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.ixuea.courses.mymusic.R;
import com.ixuea.courses.mymusic.activity.BaseMusicPlayerActivity;
import com.ixuea.courses.mymusic.activity.SheetDetailActivity;
import com.ixuea.courses.mymusic.activity.WebViewActivity;
import com.ixuea.courses.mymusic.adapter.DiscoveryAdapter;
import com.ixuea.courses.mymusic.api.Api;
import com.ixuea.courses.mymusic.domain.Ad;
import com.ixuea.courses.mymusic.domain.BaseMultiItemEntity;
import com.ixuea.courses.mymusic.domain.Sheet;
import com.ixuea.courses.mymusic.domain.Song;
import com.ixuea.courses.mymusic.domain.Title;
import com.ixuea.courses.mymusic.domain.response.ListResponse;
import com.ixuea.courses.mymusic.domain.test.Person;
import com.ixuea.courses.mymusic.listener.HttpObserver;
import com.ixuea.courses.mymusic.manager.ListManager;
import com.ixuea.courses.mymusic.service.MusicPlayerService;
import com.ixuea.courses.mymusic.util.AndroidBase64Util;
import com.ixuea.courses.mymusic.util.ImageUtil;
import com.ixuea.courses.mymusic.util.LogUtil;
import com.youth.banner.Banner;
import com.youth.banner.listener.OnBannerListener;
import com.youth.banner.loader.ImageLoader;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import butterknife.BindView;
import io.reactivex.Observable;
import io.realm.Realm;
import io.realm.RealmResults;

/**
 * 首页-发现界面
 */
public class DiscoveryFragment extends BaseCommonFragment implements OnBannerListener {

    private static final String TAG = "DiscoveryFragment";
    /**
     * 列表控件
     */

    @BindView(R.id.rv)
    RecyclerView rv;
    private GridLayoutManager layoutManager;//Grid布局管理器
    private DiscoveryAdapter adapter;//发现界面适配器（RecyclerView多类型适配器）
    private Banner banner;//轮播图组件
    private List<Ad> bannerData;//轮播图数据

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
        //添加头部
        adapter.setHeaderView(createHeaderView());

        //请求数据
        fetchData();

        //请求轮播图数据
        fetchBannerData();

        //测试数据库使用
//        testRealm();

        //测试有没有生成多用户文件（每个用户单独查看自己的内容）
//        orm.saveSong(new Song());

        //测试LiteOrm数据库使用

        //创建数据库实  参数2： 数据库名称
        //创建到的数据默认保存/data/data/包名/databases 里面
//        LiteOrm orm = LiteOrm.newSingleInstance(getMainActivity(), "ixuea_android.db");
//
//        //创建对象(也就是表)
//        SearchHistory searchHistory = new SearchHistory();
//        //赋值
//        searchHistory.setContent("我们是爱学啊");
//        searchHistory.setCreated_at(System.currentTimeMillis());
//
//        //保存数据 这个orm 是LiteOrm对象
//        orm.save(searchHistory);
//
//        //查询所欲数据
//        ArrayList<SearchHistory> datum = orm.query(SearchHistory.class);
//        //打印数量
//        LogUtil.d(TAG, "search history count: " + datum.size());

        //测试Android Base64编码
        //编码相等
        if ("aXh1ZWFlZHU=".equals(AndroidBase64Util.encodeString2string("ixueaedu"))) {
            //测试通过
            LogUtil.d(TAG, "test encode1 pass");
        } else {
            LogUtil.d(TAG, "test encode1 error");
        }

        //编码不相等
        if ("ixueaedu".equals(AndroidBase64Util.encodeString2string("ixueaedu"))) {
            LogUtil.d(TAG, "test encode2 error");
        } else {
            //编码不相等的时候才是通过的
            LogUtil.d(TAG, "test encode2 pass");
        }

        //解码相等测试(也就是把aXh1ZWFlZHU= 解码出来的结果是等于ixueaedu=  的 )
        if ("ixueaedu".equals(AndroidBase64Util.decodeString2string("aXh1ZWFlZHU="))) {
            LogUtil.d(TAG, "test decode1 pass");
        } else {
            LogUtil.d(TAG, "test decode1 error");
        }

        //解码不相等测试
        if ("ixueaedu".equals(AndroidBase64Util.decodeString2string("ixueaedu"))) {
            //解码处理不可能一样的，这个是错误的
            LogUtil.d(TAG, "test decode2 error");
        } else {
            //这个不相等的时候 才是通过的
            LogUtil.d(TAG, "test decode2 pass");
        }

    }

    private void testRealm() {
        //获取数据库实例
        //内部相当于打开了数据库
        Realm realm = Realm.getDefaultInstance();
        //开启事务
        realm.beginTransaction();//意思说所有的操作都在这个中进行

        //保存人（其实就是创建了一个对象，这时还没有值，下面才是赋值）
        Person person = realm.createObject(Person.class);
        //赋值
        person.setName("张三");
        person.setAge(17);
        //提交事务
        realm.commitTransaction();

        RealmResults<Person> results = realm.where(Person.class).findAll();
        LogUtil.d(TAG, "person count :" + results.size());
        realm.close();//记得 ：关闭数据库
    }

    @Override
    protected void initListeners() {
        super.initListeners();

        //设置item点击事件（DiscoveryAdapter内部实现了点击事件，如果是RecyclerView 需要自己取实现点击事件）
        adapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            /**
             * 点击Item回调
             * @param adapter BaseQuickAdapter
             * @param view View
             * @param position 索引
             */
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                //获取点击的数据（获取当前用户点击的是哪一个对象）
                Object data = adapter.getItem(position);

                //判断类型
                if (data instanceof Sheet) {
                    //歌单
                    Sheet sheet = (Sheet) data;

//                    //创建Intent
//                    //Android中大部分组件的操作都是通过他完成的
//                    Intent intent = new Intent(getMainActivity(), SheetDetailActivity.class);
//
//                    //传递Id
//                    //这样详情界面才知道点击的是那个歌单
//                    intent.putExtra(Constant.ID, sheet.getId());
//
//                    //启动Intent里面的activity
//                    startActivity(intent);

                    //使用重构后的方法
                    startActivityExtraId(SheetDetailActivity.class, sheet.getId());
                } else if (data instanceof Song) {
                    //单曲

                    //创建一个列表
                    List<Song> datum = new ArrayList<>();
                    Song song = (Song) data;
                    datum.add(song);
                    //获取播放列表管理器
                    ListManager listManager = MusicPlayerService.getListManager(getMainActivity());
                    //设置到播放列表
                    listManager.setDatum(datum);
                    //播放这首音乐
                    listManager.play(song);

                    //进入播放界面(向上转型（因为父类没有子类的方法）)
                    ((BaseMusicPlayerActivity) getMainActivity()).startMusicPlayerActivity();

                }
            }
        });
    }

    /**
     * //请求轮播图数据
     */
    private void fetchBannerData() {
        Api.getInstance()
                .ads()
                .subscribe(new HttpObserver<ListResponse<Ad>>() {
                    @Override
                    public void onSucceeded(ListResponse<Ad> data) {
                        //可能有很多的逻辑要处理，所以定义一个方法
                        showBanner(data.getData());
                    }
                });
    }

    /**
     * 显示banner
     *
     * @param data List<Ad>
     */
    private void showBanner(List<Ad> data) {
        LogUtil.d(TAG, "showBanner:" + data.size());
        this.bannerData = data;
        //设置到轮播图组件
        banner.setImages(data);

        //显示数据
        banner.start();
        //第一次也要滚动
        startBannerScroll();
    }

    /**
     * //第一次也要滚动
     */
    private void startBannerScroll() {
        //轮播图自动播放（自动滚动）
        banner.startAutoPlay();
    }

    /**
     * 当界面显示了执行
     */
    @Override
    public void onResume() {
        super.onResume();
        //开始轮播图滚动
        if (bannerData != null) {//有数据才滚动
            //开始轮播图滚动
            banner.startAutoPlay();
        }
    }

    /**
     * 当界面看不见了执行
     * <p>
     * 包括开启新界面，弹窗，后台
     */
    @Override
    public void onPause() {
        super.onPause();
        //结束轮播图滚动
        banner.stopAutoPlay();
    }

    /**
     * 创建头部布局
     * <p>
     * 就是把一个xml转换成view，然后在上面adapter.setHeaderView() 添加进去
     *
     * @return View
     */
    private View createHeaderView() {
        //参数2：false，我们自己在前面的adapter.setHeaderView(createHeaderView());已经添加了
        //如果是true，则让它帮我们添加，那么这样的话，就是把一个控件添加到2个父类中，那么就会报错
        //从XML创建View
        View view = getLayoutInflater().inflate(R.layout.header_discovery, (ViewGroup) rv.getParent(), false);

        //查找轮播图组件
        banner = view.findViewById(R.id.banner);

        //设置点击监听器
        banner.setOnBannerListener(this);

        //设置图片加载器
        banner.setImageLoader(new GlideImageLoader());

        //找到日期文本控件
        //注意这里是用的findViewById（如果用的是@BindView获取的话，获取到的是本类加载的那个fragment_discovery）
        //而我们需要加载的是头布局，所以用头布局的view来获取
        TextView tv_day = view.findViewById(R.id.tv_day);

        //设置日期
        //由于项目中没有其他位置使用到
        //所以可以不用重构

        //获取日历对象
        Calendar calendar = Calendar.getInstance();
        //获取当前月的天
        int day = calendar.get(Calendar.DAY_OF_MONTH);
        //设置到控件上，记得转换
        tv_day.setText(String.valueOf(day));

        //返回控件
        return view;
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

        //单曲Api
        Observable<ListResponse<Song>> songs = Api.getInstance().songs();

        //为降低课程难度
        //这里先不使用RxJava来合并请求
        //后面会在我的界面讲解

        //请求歌单数据
        sheets.subscribe(new HttpObserver<ListResponse<Sheet>>() {
            @Override
            public void onSucceeded(ListResponse<Sheet> data) {
                //添加歌单数据
                datum.addAll(data.getData());

                //请求单曲数据
                songs.subscribe(new HttpObserver<ListResponse<Song>>() {
                    @Override
                    public void onSucceeded(ListResponse<Song> data) {
                        //添加标题
                        datum.add(new Title("推荐单曲"));

                        //添加单曲数据
                        //data.getData():获取到的是Song的集合（含有多个song）
                        datum.addAll(data.getData());
                        //设置数据到适配器（替换数据）
                        adapter.replaceData(datum);//记得写上这个
                    }
                });
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

    /**
     * 轮播图点击回调
     *
     * @param position 索引
     */
    @Override
    public void OnBannerClick(int position) {
        //获取点击的广告对象
        Ad ad = bannerData.get(position);
        //获取通用的WebView界面显示
        WebViewActivity.start(getMainActivity(), "活动详情", ad.getUri());
    }

    /**
     * Banner框架显示图片的实现类
     * <p>
     * 如果有其他位置用到
     * 可以放到单独的文件中
     */
    class GlideImageLoader extends ImageLoader {
        /**
         * 加载图片的方法
         * @param context Context
         * @param path 就是给Banner添加的对象（比如点击集合List<ad> 就是里面的ad对象）
         * @param imageView 图片控件
         */
        @Override
        public void displayImage(Context context, Object path, ImageView imageView) {
            //将对象转为广告对象
            Ad banner = (Ad) path;
            //使用工具类方法显示图片
            ImageUtil.show(getMainActivity(), imageView, banner.getBanner());
        }
    }

}
