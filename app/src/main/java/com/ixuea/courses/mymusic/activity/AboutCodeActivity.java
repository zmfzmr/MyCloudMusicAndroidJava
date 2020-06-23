package com.ixuea.courses.mymusic.activity;

import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.android.material.appbar.AppBarLayout;
import com.ixuea.courses.mymusic.R;
import com.ixuea.courses.mymusic.util.PackageUtil;

import org.jetbrains.annotations.NotNull;

import androidx.coordinatorlayout.widget.CoordinatorLayout;

/**
 * 关于我的云音乐（代码布局）
 * <p>
 * 本界面使用的是代码实现布局
 * 目的是演示纯代码实现界面
 * 两者没有好坏之分
 * 只有适不适合
 * 例如：界面里面的输入框要动态配置
 * <p>
 * 就适合代码实现布局
 * 如果界面基本上不变
 * 也没有什么复杂的效果
 * 就可视化布局
 * 如果有复杂的效果一般要自定义控件
 */
public class AboutCodeActivity extends BaseTitleActivity {
    /**
     * 内容容器
     */
    private LinearLayout contentContainer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //代码实现的话，这部分就注释掉
//        setContentView(R.layout.activity_about_code);

        //这里实现方法和可视化差不多
        //只是这些类是在代码中创建的

        //根布局
        CoordinatorLayout container = new CoordinatorLayout(getMainActivity());
        //设置背景
        container.setBackgroundColor(getResources().getColor(R.color.divider_color));

        //注意： 这里加载的是布局layout,用的是R.layout ,不是R.id
        //参数3： 附加到这个父容器里面
        //由于tool_bar这个布局有写参数设置，所以这里直接加载复用原来的布局就行了
        View appBarLayout = View.inflate(getMainActivity(), R.layout.tool_bar, container);

        //注意：父类到的toolbar有@BindView(这个ButterKnife我们isBindView关闭了)；我们查找的话，是用findViewById查找的，
        toolbar = appBarLayout.findViewById(R.id.toolbar);

        //---------------------内容容器
        //内容容器 （这个是成员变量）
        contentContainer = new LinearLayout(getMainActivity());
        //设置方向
        contentContainer.setOrientation(LinearLayout.VERTICAL);
        //内容水平居中
        contentContainer.setGravity(Gravity.CENTER_HORIZONTAL);

        //布局参数(也就是说LinearLayout的布局参数用的是父容器里面的布局参数(参数来自父容器))
        //宽高；位置都在这个类上面设置
        //他的类型为当前控件所在容器里面的LayoutParams
        CoordinatorLayout.LayoutParams contentContainerLayoutParams =
                new CoordinatorLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);

        //注意：这里传入的是一个对象(需要new的)
        contentContainerLayoutParams.setBehavior(new AppBarLayout.ScrollingViewBehavior());
        //将内容容器添加到根布局
        container.addView(contentContainer, contentContainerLayoutParams);


        //-----------logo
        ImageView logoImageView = new ImageView(getMainActivity());

        //设置显示的图片
        logoImageView.setImageResource(R.mipmap.ic_launcher);

        //logo宽
        //转为px
        //当然也可以使用我们定义的工具类
        int logoWidth = getResources().getDimensionPixelOffset(R.dimen.avatar_width);
        //logo布局参数 构造参数1 2：直接写宽度；如果是更父容器一样的，那么写ViewGroup.LayoutParams.MATCH_PARENT就行
        LinearLayout.LayoutParams logoLayoutParams = new LinearLayout.LayoutParams(logoWidth, logoWidth);
        int logoMarginTop = getResources().getDimensionPixelOffset(R.dimen.button_height);
        logoLayoutParams.topMargin = logoMarginTop;

        //父容器LinearLayout 添加view
        contentContainer.addView(logoImageView, logoLayoutParams);

        //-----------版本容器
        LinearLayout versionLinearLayout = getItemContainer();

//        //高度(50dp 换成px)
        int itemHeight = getResources().getDimensionPixelOffset(R.dimen.menu_item_height);

        //版本容器布局参数
        LinearLayout.LayoutParams versionLayoutParams = getItemContainerLayoutParams(itemHeight);

        //顶部偏移
        versionLayoutParams.topMargin = logoMarginTop;

        //添加到内容布局
        contentContainer.addView(versionLinearLayout, versionLayoutParams);

        //添加标题
        addItemTitle(versionLinearLayout, R.string.current_version);

        //版本的值
        TextView tvVersion = new TextView(getMainActivity());
        //设置文字
        setVersionData(tvVersion);

        //颜色
        tvVersion.setTextColor(getResources().getColor(R.color.light_grey));

        //版本值 布局参数
        LinearLayout.LayoutParams tvVersionLayoutParams =
                new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT,
                        ViewGroup.LayoutParams.WRAP_CONTENT);

        //版本值添加到版本容器
        versionLinearLayout.addView(tvVersion, tvVersionLayoutParams);

        //-----------新手指南 容器
//        LinearLayout guideLinearLayout = getItemContainer();
//
//        //新手指南容器 布局参数
//        LinearLayout.LayoutParams guideLayoutParams = getItemContainerLayoutParams(itemHeight);
//
//        //中等距离偏移
//        int middlePadding = getResources().getDimensionPixelOffset(R.dimen.padding_middle);
//
//        //顶部偏移
//        guideLayoutParams.topMargin = middlePadding;
//
//        //添加到内容布局
//        contentContainer.addView(guideLinearLayout,guideLayoutParams);
//
//        //添加标题(我的云音乐新手指南)
//        addItemTitle(versionLinearLayout,R.string.my_cloud_music_guide);

        //中等距离偏移 (这个也就是距离顶部的值)
        int middlePadding = getResources().getDimensionPixelOffset(R.dimen.padding_middle);
        int smallPadding = getResources().getDimensionPixelOffset(R.dimen.divider_small);

        //添加新手指南item  (这个不需要点击事件)
        addItem(R.string.my_cloud_music_guide, middlePadding, null);

        //关于爱学啊
        addItem(R.string.about_ixuea, smallPadding, v -> {
            WebViewActivity.start(getMainActivity(), "爱学啊", "http://wwww/ixuea.com");
        });

        //这里传入到的是一个View
        setContentView(container);
    }

    /**
     * 添加item
     *
     * @param titleResourceId 标题字符串id
     * @param onClickListener
     */
    private void addItem(int titleResourceId, int marginTop, View.OnClickListener onClickListener) {
        LinearLayout linearLayout = getItemContainer();

        //点击事件(item容器点击事件)
        if (onClickListener != null) {
            linearLayout.setOnClickListener(onClickListener);
        }

        //高度(50dp 换成px)
        int itemHeight = getResources().getDimensionPixelOffset(R.dimen.menu_item_height);

        //新手指南容器 布局参数
        LinearLayout.LayoutParams guideLayoutParams = getItemContainerLayoutParams(itemHeight);

//        //中等距离偏移 (这个值的拿到外面来)
//        int middlePadding = getResources().getDimensionPixelOffset(R.dimen.padding_middle);

        //顶部偏移 (这个值由外面传入)
        guideLayoutParams.topMargin = marginTop;

        //添加到内容布局
        contentContainer.addView(linearLayout, guideLayoutParams);

        //添加标题(我的云音乐新手指南)
        addItemTitle(linearLayout, titleResourceId);

        //图标
        ImageView icon = new ImageView(getMainActivity());

        //设置图标
        icon.setImageResource(R.drawable.ic_arrow_right);

        //布局参数
        LinearLayout.LayoutParams iconLayoutParams =
                new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT,
                        ViewGroup.LayoutParams.WRAP_CONTENT);
        //添加到item容器
        linearLayout.addView(icon, iconLayoutParams);

    }

    /**
     * 添加标题容器
     *
     * @param linearLayout
     * @param titleResourceId 标题资源id
     */
    private void addItemTitle(LinearLayout linearLayout, int titleResourceId) {
        //版本标题
        TextView tvVersionTitle = new TextView(getMainActivity());

        //标题 (当前版本)
        tvVersionTitle.setText(titleResourceId);

        //版本标题颜色
        tvVersionTitle.setTextColor(getResources().getColor(R.color.text));

        //版本标题布局参数  宽度为0，因为要TextView 使用权重
        LinearLayout.LayoutParams tvVersionTitleLayoutParams =
                new LinearLayout.LayoutParams(0, ViewGroup.LayoutParams.WRAP_CONTENT);

        //权重
        tvVersionTitleLayoutParams.weight = 1;

        //添加版本标题到-->版本容器(这个是水平方向)
        linearLayout.addView(tvVersionTitle, tvVersionTitleLayoutParams);
    }

    /**
     * 获取item 容器布局参数
     *
     * @param itemHeight
     * @return
     */
    @NotNull
    private LinearLayout.LayoutParams getItemContainerLayoutParams(int itemHeight) {
        //版本容器布局参数(父容器是 LinearLayout)
        return new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, itemHeight);
    }

    /**
     * 获取item容器
     *
     * @return
     * @ NotNull 方法前面加这个，表示返回值，不能为null
     */
    @NotNull
    private LinearLayout getItemContainer() {
        LinearLayout versionLinearLayout = new LinearLayout(getMainActivity());

//        //设置水平 默认就是水平的 可以不用写
//        versionLinearLayout.setOrientation(LinearLayout.HORIZONTAL);

        //垂直居中
        versionLinearLayout.setGravity(Gravity.CENTER_VERTICAL);

        //白色背景
        versionLinearLayout.setBackgroundColor(getResources().getColor(R.color.white));

        //padding
        int paddingLeft = getResources().getDimensionPixelOffset(R.dimen.padding_large);
        //padding 是设置到本容器里面 (而margin是子空间设置的(子控件相对外面的距离))
        versionLinearLayout.setPadding(paddingLeft, 0, paddingLeft, 0);
        return versionLinearLayout;
    }

    /**
     * 设置版本数据
     *
     * @param tv TextView
     */
    private void setVersionData(TextView tv) {
        //版本name : 比如： 2.0.0
        String versionName = PackageUtil.getViersionName(getMainActivity());
        //版本代码号：比如200
        long versionCode = PackageUtil.getVersionCode(getMainActivity());
        //设置版本号到控件上
        tv.setText(getResources().getString(R.string.version_value,
                versionName, versionCode));
    }

    /**
     * 不自动注入view
     * <p>
     * 因为代码还没有准备好(也就是控件还没有初始化)，注入view，就会报错
     */
    @Override
    protected boolean isBindView() {
        return false;
    }
}
