package com.ixuea.courses.mymusic.activity;

import android.content.Context;
import android.os.Bundle;
import android.view.View;

import com.ixuea.courses.mymusic.R;
import com.ixuea.courses.mymusic.adapter.DownloadAdapter;

import net.lucode.hackware.magicindicator.MagicIndicator;
import net.lucode.hackware.magicindicator.ViewPagerHelper;
import net.lucode.hackware.magicindicator.buildins.commonnavigator.CommonNavigator;
import net.lucode.hackware.magicindicator.buildins.commonnavigator.abs.CommonNavigatorAdapter;
import net.lucode.hackware.magicindicator.buildins.commonnavigator.abs.IPagerIndicator;
import net.lucode.hackware.magicindicator.buildins.commonnavigator.abs.IPagerTitleView;
import net.lucode.hackware.magicindicator.buildins.commonnavigator.indicators.LinePagerIndicator;
import net.lucode.hackware.magicindicator.buildins.commonnavigator.titles.SimplePagerTitleView;

import java.util.ArrayList;

import androidx.viewpager.widget.ViewPager;
import butterknife.BindView;

/**
 * 下载管理界面
 */
public class DownloadActivity extends BaseTitleActivity {
    /**
     * 指示器
     */
    @BindView(R.id.mi)
    MagicIndicator mi;

    /**
     * ViewPager 左右滚动控件
     */
    @BindView(R.id.vp)
    ViewPager vp;
    private DownloadAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_download);
    }

    @Override
    protected void initDatum() {
        super.initDatum();

        //创建适配器
        adapter = new DownloadAdapter(getMainActivity(), getSupportFragmentManager());

        //设置适配器
        vp.setAdapter(adapter);

        //创建列表
        ArrayList<Integer> datum = new ArrayList<>();
        datum.add(0);
        datum.add(1);
        //设置数据
        adapter.setDatum(datum);

        //思路： MagicIndicator设置CommonNavigator(设置适配器)),然后借助ViewPagerHelper绑定 mi和vp
        //记得设置 commonNavigator.setAdjustMode(true);
        //创建通用指示器(其实就是通用导航器)
        CommonNavigator commonNavigator = new CommonNavigator(getMainActivity());
        //给通用导航器 设置适配器
        commonNavigator.setAdapter(new CommonNavigatorAdapter() {
            /**
             * 指示器数量(和ViewPager 适配器的数量 一样)
             */
            @Override
            public int getCount() {
                return adapter.getCount();
            }

            /**
             * 返回当前位置的标题
             */
            @Override
            public IPagerTitleView getTitleView(Context context, int index) {
                //创建简单的文本控件
                SimplePagerTitleView titleView = new SimplePagerTitleView(context);

                //注意：是调用adapter 里面重写的getPageTitle
                titleView.setText(adapter.getPageTitle(index));
                //指示器标题 正常的颜色(默认颜色)
                titleView.setNormalColor(R.color.tab_normal);
                //标题选择颜色是白色
                titleView.setSelectedColor(R.color.white);

                titleView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        //让ViewPager跳转到指定位置
                        //getTitleView 方法参数里面的 index
                        vp.setCurrentItem(index);
                    }
                });
                //返回标题文本对象
                return titleView;
            }

            /**
             * 返回指示器
             * 就是下面那条线
             */
            @Override
            public IPagerIndicator getIndicator(Context context) {
                //创建一条线(翻译：线 页面指示器)
                LinePagerIndicator indicator = new LinePagerIndicator(context);
                //线的宽度和内容一样(也就是线宽 和 文本内容标题 一样大)
                indicator.setMode(LinePagerIndicator.MODE_WRAP_CONTENT);
                //高亮颜色
                indicator.setColors(getResources().getColor(R.color.white));

                return indicator;//记得返回LinePagerIndicator对象

                //返回null表示不显示指示器
                //return null;
            }
        });
        //如何位置显示不下指示器
        //是否自动调整  true: 表示自动调整
        commonNavigator.setAdjustMode(true);

        //mi设置导航器
        mi.setNavigator(commonNavigator);

        //将ViewPager和指示器关联
        //ViewPagerHelper: 是我们添加的net.lucode.hackware.magicindicator 依赖里面的类
        ViewPagerHelper.bind(mi, vp);
    }
}
