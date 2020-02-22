package com.ixuea.courses.mymusic.activity;

import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.github.florent37.glidepalette.BitmapPalette;
import com.github.florent37.glidepalette.GlidePalette;
import com.ixuea.courses.mymusic.R;
import com.ixuea.courses.mymusic.adapter.SongAdapter;
import com.ixuea.courses.mymusic.api.Api;
import com.ixuea.courses.mymusic.domain.Sheet;
import com.ixuea.courses.mymusic.domain.response.DetailResponse;
import com.ixuea.courses.mymusic.listener.HttpObserver;
import com.ixuea.courses.mymusic.util.Constant;
import com.ixuea.courses.mymusic.util.ImageUtil;
import com.ixuea.courses.mymusic.util.LogUtil;
import com.ixuea.courses.mymusic.util.ResourceUtil;
import com.ixuea.courses.mymusic.util.ToastUtil;

import org.apache.commons.lang3.StringUtils;

import androidx.annotation.Nullable;
import androidx.palette.graphics.Palette;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import butterknife.BindView;
import retrofit2.Response;

/**
 * 歌单详情界面
 */
public class SheetDetailActivity extends BaseTitleActivity implements View.OnClickListener {

    private static final String TAG = "SheetDetailActivity";

    /**
     * 列表控件
     */
    @BindView(R.id.rv)
    RecyclerView rv;

    /**
     * 歌单Id
     */
    private String id;
    private Sheet data;//歌单对象数据
    private SongAdapter adapter;
    /**
     * 头部容器
     */
    private LinearLayout ll_header;//这里也可以命名为LinearLayout，也可以不命名
    private ImageView iv_banner;//封面图
    private TextView tv_title;//歌单详情头部标题（也就是歌单的名称）
    private ImageView iv_avatar;//头像
    private TextView tv_nickname;//头像
    private View ll_comment_container;//评论容器
    private TextView tv_comment_count;//评论数
    private Button bt_collection;//收藏按钮
    private View ll_play_all_container;//播放全部容器
    private TextView tv_count;//歌曲数

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sheet_detail);
    }

    @Override
    protected void initView() {
        super.initView();

        //尺寸固定
        rv.setHasFixedSize(true);

        //设置布局管理器(这里是线性布局管理器)
        LinearLayoutManager layoutManager = new LinearLayoutManager(getMainActivity());
        //设置管理器
        rv.setLayoutManager(layoutManager);

//        //创建分割线
//        DividerItemDecoration decoration = new DividerItemDecoration(getMainActivity(),RecyclerView.VERTICAL);
//
//        //添加到控件
//        //可以添加多个
//        rv.addItemDecoration(decoration);
    }

    /**
     * 创建头部
     */
    private View createHeaderView() {
        //从XML创建布局
        View view = getLayoutInflater().inflate(R.layout.header_sheet_detail, (ViewGroup) rv.getParent(), false);

        //头部容器
        ll_header = view.findViewById(R.id.ll_header);

        //封面图
        iv_banner = view.findViewById(R.id.iv_banner);

        //标题
        tv_title = view.findViewById(R.id.tv_title);

        //头像
        iv_avatar = view.findViewById(R.id.iv_avatar);

        //昵称
        tv_nickname = view.findViewById(R.id.tv_nickname);

        //评论容器
        ll_comment_container = view.findViewById(R.id.ll_comment_container);

        //评论数据
        tv_comment_count = view.findViewById(R.id.tv_comment_count);

        //收藏按钮
        bt_collection = view.findViewById(R.id.bt_collection);

        //播放全部容器
        ll_play_all_container = view.findViewById(R.id.ll_play_all_container);

        //歌曲数
        tv_count = view.findViewById(R.id.tv_count);

        //返回view
        return view;
    }

    @Override
    protected void initDatum() {
        super.initDatum();

        //获取传递的参数
        getIntent().getStringExtra(Constant.ID);

        //使用重构后的方法
        id = extraId();

        //创建适配器
        adapter = new SongAdapter(R.layout.item_sheet_detail);

        //添加头部（注意：在设置适配器前添加头部布局）
        adapter.addHeaderView(createHeaderView());

        //设置适配器
        rv.setAdapter(adapter);
        //这里RecyclerView在前面，请求网络数据在后

        //请求数据
        fetchData();
    }

    @Override
    protected void initListeners() {
        super.initListeners();
        //收藏按钮单击事件
        bt_collection.setOnClickListener(this);

        //评论点击事件
        ll_comment_container.setOnClickListener(this);
    }

    /**
     * //请求数据
     */
    private void fetchData() {
        Api.getInstance()
                .sheetDetail(id)
                .subscribe(new HttpObserver<DetailResponse<Sheet>>() {
                    @Override
                    public void onSucceeded(DetailResponse<Sheet> data) {
                        next(data.getData());
                    }
                });
    }

    /**
     * 显示数据
     *
     * @param data Sheet数据
     */
    private void next(Sheet data) {
        this.data = data;

        LogUtil.d(TAG, "next:" + data);
        if (data.getSongs() != null && data.getSongs().size() > 0) {
            //有音乐才设置
            //设置数据
            adapter.replaceData(data.getSongs());
        }

//        //显示封面
//        if (StringUtils.isBlank(data.getBanner())) {
//            //如果图片为空
//
//            //就用默认图片
//            iv_banner.setImageResource(R.drawable.placeholder);
//        } else {
//            //有图片
//            ImageUtil.show(getMainActivity(), iv_banner, data.getBanner());
//        }

//        //使用Palette获取封面颜色
//        if (StringUtils.isBlank(data.getBanner())) {
//            //图片为空
//
//            //使用默认图片
//            iv_banner.setImageResource(R.drawable.placeholder);
//        } else {
//            //有图
//
//            //这是一个典型的构建者模式
//            //由于这是项目课程
//            //所以这里不详细讲解设计模式
//            Glide.with(this)
//                    .asBitmap()
//                    //加载图片到自定义目标
//                    //为什么是自定义目标
//                    //是因为我们要获取Bitmap
//                    //然后获取Bitmap的一些颜色
//                    .load(ResourceUtil.resourceUri(data.getBanner()))
//                    .into(new CustomTarget<Bitmap>() {
//                        /**
//                         * 资源加载完成调用
//                         *
//                         * @param resource   Bitmap
//                         * @param transition Transition<? super Bitmap>
//                         */
//                        @Override
//                        public void onResourceReady(@NonNull Bitmap resource, @Nullable Transition<? super Bitmap> transition) {
//                            //显示封面图
//                            iv_banner.setImageBitmap(resource);
//
//                            //在Material Design(MD，材料设计，是Google的一门设计语言)的设计中
//                            //所谓的设计语言就是一些设计规范
//                            //目前Google已经应用到Android，Gmail等产品
//
//                            //推荐我们将应用的状态栏
//                            //标题栏的颜色和当前页面的内容融合
//                            //也就说当前页面显示一张红色的图片
//                            //那么最好状态栏，标题栏的颜色也和红色差不多
//                            //实现这种效果可以借助的Palette类。
//                            //Palette:可以翻译为调色板
//                            //功能是可以从图片中获取一些颜色
//                            //详细的可以学习《详解Material Design，http://www.ixuea.com/courses/9》课程
//                            Palette.from(resource)
//                                    //传入回调，这样就知道什么时候颜色资源生成了
//                                    .generate(new Palette.PaletteAsyncListener() {
//                                        /**
//                                         * 颜色计算完成了（也可能没找到颜色，这样也算计算完成了）
//                                         * @param palette Palette
//                                         *
//                                         * 这里是异步的操作
//                                         */
//                                        @Override
//                                        public void onGenerated(@Nullable Palette palette) {
//                                            //获取 有活力 的颜色
//                                            //翻译：Vibrant：充满活力的 Swatch：样本
//                                            Palette.Swatch swatch = palette.getVibrantSwatch();
//                                            //可能没有值所以要判断
//                                            if (swatch != null) {
//                                                //获取颜色的rgb
//                                                int rgb = swatch.getRgb();
//                                                //设置标题栏颜色
//                                                toolbar.setBackgroundColor(rgb);
//
//                                                //设置头部容器颜色
//                                                ll_header.setBackgroundColor(rgb);
//
//                                                //这些api只有高版本才有
//                                                //所以说要判断  LOLLIPOP  = 21
//                                                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
//                                                    Window window = getWindow();
//                                                    //设置状态栏颜色
//                                                    window.setStatusBarColor(rgb);
//                                                    //设置导航栏颜色
//                                                    window.setNavigationBarColor(rgb);
//                                                }
//                                            }
//
//                                        }
//                                    });
//                        }
//
//                        /**
//                         * 加载任务取消了
//                         * 可以在这里面释放我们定义的一些资源
//                         *
//                         * @param placeholder
//                         */
//                        @Override
//                        public void onLoadCleared(@Nullable Drawable placeholder) {
//
//                        }
//                    });
//        }

        //使用GlidePalette
        if (StringUtils.isBlank(data.getBanner())) {
            //如果图片为空

            //就用默认图片
            iv_banner.setImageResource(R.drawable.placeholder);
        } else {
            //有图

            //获取图片绝对路径
            String uri = ResourceUtil.resourceUri(data.getBanner());

            GlidePalette<Drawable> glidePalette = GlidePalette
                    //在把地址传到GlidePalette
                    .with(uri)
                    //VIBRANT颜色样板 （有活力的颜色）
                    .use(GlidePalette.Profile.VIBRANT)

                    //设置到控件背景
                    .intoBackground(toolbar, GlidePalette.Swatch.RGB)
                    .intoBackground(ll_header, GlidePalette.Swatch.RGB)
                    //设置回调
                    //用回调的目的是
                    //要设置状态栏和导航栏
                    .intoCallBack(new BitmapPalette.CallBack() {
                        @Override
                        public void onPaletteLoaded(@Nullable Palette palette) {
                            //获取 有活力 的颜色
                            //翻译：Vibrant：充满活力的 Swatch：样本
                            Palette.Swatch swatch = palette.getVibrantSwatch();
                            //可能没有值所以要判断
                            if (swatch != null) {
                                //获取颜色的rgb
                                int rgb = swatch.getRgb();

                                //这些api只有高版本才有
                                //所以说要判断  LOLLIPOP  = 21
                                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                                    Window window = getWindow();
                                    //设置状态栏颜色
                                    window.setStatusBarColor(rgb);
                                    //设置导航栏颜色
                                    window.setNavigationBarColor(rgb);
                                }
                            }
                        }
                    })
                    //淡入
                    //只有第一次效果很明显
                    //由于这里是项目课程
                    //所以就不在深入查看是为什么了
                    //如果大家感兴趣可以深入查看
                    //搞懂了也可以在群里分享给大家
                    .crossfade(true);

            //加载
            Glide.with(getMainActivity())
                    .load(uri)
                    //加载完成监听器
                    .listener(glidePalette)
                    //将图片设置到图片控件
                    .into(iv_banner);


        }

        //显示标题
        tv_title.setText(data.getTitle());

        //头像
        ImageUtil.showAvatar(getMainActivity(), iv_avatar, data.getUser().getAvatar());

        //昵称
        tv_nickname.setText(data.getUser().getNickname());

        //评论数
        tv_comment_count.setText(String.valueOf(data.getComments_count()));

        //音乐数
//        tv_count.setText(getResources().getString(R.string.music_count, data.getSongs().size()));
        //因为有些歌单没有值，会引起空指针异常，所以先用下面这个
        tv_count.setText(getResources().getString(R.string.music_count, data.getSongs_count()));

        //显示收藏状态
        showCollectionStatus();
    }

    /**
     * 显示收藏状态
     */
    private void showCollectionStatus() {
        if (data.isCollection()) {
            //收藏了

            //将按钮文字改为取消
            bt_collection.setText(getResources()
                    .getString(R.string.cancel_collection, data.getCollections_count()));
            //弱化取消收藏按钮
            //因为我们的本质是想让用户收藏歌单
            //所以去掉背景
            bt_collection.setBackground(null);

            //设置文字颜色为灰色
            //注意：这里是getColor，而不是getSting
            bt_collection.setTextColor(getResources().getColor(R.color.light_grey));
        } else {
            //将按钮文字改为收藏
            bt_collection.setText(getResources()
                    .getString(R.string.collection, data.getCollections_count()));

            //设置按钮颜色为主色调 2种方式
//            bt_collection.setBackground(getResources().getDrawable(R.drawable.selector_color_primary));
            bt_collection.setBackgroundResource(R.drawable.selector_color_primary);

            //设置文字颜色为白色
            bt_collection.setTextColor(getResources().getColor(R.color.white));
        }
    }

    /**
     * 按钮点击回调方法
     *
     * @param v View
     */
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.bt_collection:
                //收藏歌单按钮点击了
                //翻译：process：过程或者处理
                processCollectionClick();
                break;
            case R.id.ll_comment_container:
//                //评论容器点击了
//                Intent intent = new Intent(getMainActivity(), CommentActivity.class);
//
//                //添加歌单id
//                intent.putExtra(Constant.SHEET_ID, data.getId());
//
//                //启动界面
//                startActivity(intent);
                //使用重构的方法
                CommentActivity.start(getMainActivity(), data.getId());

                break;
            default:
                break;
        }
    }

    /**
     * 处理收藏和取消收藏逻辑
     *
     * 这里的思路是：先请求，然后(本地)及时刷新收藏状态，
     * 然后再次进入歌单详情的时候，数据也是正确的啦
     * （因为第一次使用了网络，再次进入个歌单详情的时候重新显示了状态）
     *
     * 其实这里使用fetchData();和本地2种方式都可以
     * 用fetchData的话，需要加载更多的资源（需要请求网络资源）
     * 实际使用的话，2中都行
     */
    private void processCollectionClick() {
        LogUtil.d(TAG, "processCollectionClick");

        if (data.isCollection()) {
            //已经收藏了

            //取消收藏
            Api.getInstance()
                    .deleteCollect(data.getId())
                    .subscribe(new HttpObserver<Response<Void>>() {
                        @Override
                        public void onSucceeded(Response<Void> d) {
                            //弹出提示
                            ToastUtil.successShortToast(R.string.cancel_success);
                            //重新加载数据
                            //目的是显示新的收藏状态
//                            fetchData();

                            //取消收藏成功 (collection_id 为null，则说明取消收藏了)
                            data.setCollection_id(null);

                            //收藏数-1
//                            SheetDetailActivity.this.data.setCollections_count(data.getCollections_count() - 1);
                            data.setCollections_count(data.getCollections_count() - 1);

                            //显示收藏状态
                            showCollectionStatus();
                        }
                    });

        } else {
            //没有收藏

            //收藏
            Api.getInstance()
                    .collect(data.getId())
                    .subscribe(new HttpObserver<Response<Void>>() {
                        @Override
                        public void onSucceeded(Response<Void> d) {
                            //弹出提示
                            ToastUtil.successShortToast(R.string.collection_success);
                            //重新加载数据
                            //目的是显示新的收藏状态
//                            fetchData();

                            //收藏状态变更后
                            //可以重新调用歌单详情界面接口
                            //获取收藏状态
                            //但对于收藏来说
                            //收藏数可能没那么重要
                            //所以不用及时刷新

                            //（我们这里做的是本地及时更新）
                            data.setCollection_id(1);//只要collection_id有值，那么isCollection就为true，说明收藏了，
                            //收藏数+1
//                            SheetDetailActivity.this.data.setCollections_count(data.getCollections_count() + 1);
                            data.setCollections_count(data.getCollections_count() + 1);
                            //显示收藏状态
                            showCollectionStatus();

                        }
                    });
        }
    }
}