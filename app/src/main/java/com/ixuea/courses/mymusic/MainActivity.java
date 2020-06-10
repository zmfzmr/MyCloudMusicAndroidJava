package com.ixuea.courses.mymusic;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.ixuea.courses.mymusic.activity.BaseMusicPlayerActivity;
import com.ixuea.courses.mymusic.activity.SettingActivity;
import com.ixuea.courses.mymusic.activity.UserActivity;
import com.ixuea.courses.mymusic.activity.UserDetailActivity;
import com.ixuea.courses.mymusic.activity.WebViewActivity;
import com.ixuea.courses.mymusic.adapter.MainAdapter;
import com.ixuea.courses.mymusic.api.Api;
import com.ixuea.courses.mymusic.domain.User;
import com.ixuea.courses.mymusic.domain.response.DetailResponse;
import com.ixuea.courses.mymusic.listener.HttpObserver;
import com.ixuea.courses.mymusic.util.Constant;
import com.ixuea.courses.mymusic.util.ImageUtil;
import com.ixuea.courses.mymusic.util.LogUtil;

import net.lucode.hackware.magicindicator.MagicIndicator;
import net.lucode.hackware.magicindicator.ViewPagerHelper;
import net.lucode.hackware.magicindicator.buildins.commonnavigator.CommonNavigator;
import net.lucode.hackware.magicindicator.buildins.commonnavigator.abs.CommonNavigatorAdapter;
import net.lucode.hackware.magicindicator.buildins.commonnavigator.abs.IPagerIndicator;
import net.lucode.hackware.magicindicator.buildins.commonnavigator.abs.IPagerTitleView;
import net.lucode.hackware.magicindicator.buildins.commonnavigator.indicators.LinePagerIndicator;
import net.lucode.hackware.magicindicator.buildins.commonnavigator.titles.SimplePagerTitleView;

import java.util.ArrayList;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.viewpager.widget.ViewPager;
import butterknife.BindView;
import butterknife.OnClick;

public class MainActivity extends BaseMusicPlayerActivity {

    private static final String TAG = "MainActivity";
    /**
     * 侧滑控件
     */
    @BindView(R.id.dl)
    DrawerLayout dl;
    /**
     * 头像
     */
    @BindView(R.id.iv_avatar)
    ImageView iv_avatar;

    /**
     * 昵称
     */
    @BindView(R.id.tv_nickname)
    TextView tv_nickname;

    /**
     * 描述
     */
    @BindView(R.id.tv_description)
    TextView tv_description;

    /**
     * 滚动视图
     * ViewPager
     */
    @BindView(R.id.vp)
    ViewPager vp;

    /**
     * MagicIndicator (ViewPager指示器)
     */
    @BindView(R.id.mi)
    MagicIndicator mi;
    private MainAdapter adapter;//适配器

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        LogUtil.d(TAG, "onCreate");

        //处理动作
        processIntent(getIntent());
    }

    @Override
    protected void initView() {
        super.initView();

        //侧滑配置 3 4参数 可以认为是2个字符串，目前还用不到
        //这里的toobar是父类里面的
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, dl, toolbar,
                R.string.navigation_drawer_open, R.string.navigation_drawer_close);
//
//        //添加监听器
////        dl.setDrawerListener(toggle);//这个过时了
        dl.addDrawerListener(toggle);//用这个最新的
//
        //同步状态(ActionBarDrawerToggle 相当于个监听器，这个监听检测Drawlayout 关闭 和打开状态，然后同步样式)
        //本来是返回箭头的，同步状态后，变为了另外一个(三)的图标；往右划的时候图标也会变化
        toggle.syncState();

        //缓存页面数量
        //默认是缓存一个
        vp.setOffscreenPageLimit(4);

        //创建Adapter
        adapter = new MainAdapter(getMainActivity(), getSupportFragmentManager());
        vp.setAdapter(adapter);

        //创建占位数据
        ArrayList<Integer> datum = new ArrayList<>();
        //4个数据表示有4个页面
        datum.add(0);
        datum.add(1);
        datum.add(2);
        datum.add(3);

        adapter.setDatum(datum);//设置数据

        //将指示器和ViewPager关联起来
        //创建通用的指示器
        CommonNavigator commonNavigator = new CommonNavigator(getMainActivity());

        /**
         * 设置适配器
         */
        commonNavigator.setAdapter(new CommonNavigatorAdapter() {
            /**
             * 指示器数量
             *
             * @return 就是指示器数量（就是有对应多少个标题）
             */
            @Override
            public int getCount() {
                return datum.size();
            }

            /**
             * 获取当前位置的标题
             *
             * @param context Context
             * @param index   index
             * @return 标题
             */
            @Override
            public IPagerTitleView getTitleView(Context context, int index) {
                //创建简单的文本控件
                SimplePagerTitleView titleView = new SimplePagerTitleView(context);

                //默认选中颜色
                titleView.setNormalColor(getResources().getColor(R.color.tab_normal));
                //选中的颜色
                titleView.setSelectedColor(getResources().getColor(R.color.white));

                //根据坐标获取
                //显示的文本
                //getPageTitle:这个方法记得要重写，否则无法无法显示标题
                titleView.setText(adapter.getPageTitle(index));

                titleView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        //点击相关的指示器
                        //让ViewPager跳转到当前位置
                        vp.setCurrentItem(index);
                    }
                });
                return titleView;
            }

            /**
             * 返回指示器线
             * <p>
             * 就是下面那条线
             *
             * @param context Context
             * @return IPagerIndicator
             */
            @Override
            public IPagerIndicator getIndicator(Context context) {
                LinePagerIndicator indicator = new LinePagerIndicator(context);
                //线的宽度和内容一样
                indicator.setMode(LinePagerIndicator.MODE_WRAP_CONTENT);
                //高亮颜色
                indicator.setColors(Color.WHITE);
//                return indicator;

                //返回null表示不显示指示器
                return null;
            }
        });

        //如果位置显示不下指示器时
        //是否自动调整
        commonNavigator.setAdjustMode(true);
        //设置导航器
        mi.setNavigator(commonNavigator);
        //让指示器和ViewPager关联
        ViewPagerHelper.bind(mi, vp);
    }


    @Override
    protected void initDatum() {
        super.initDatum();

        //测试显示资源目录图片
//        iv_avatar.setImageResource(R.drawable.placeholder);

        //显示资源目录图片
        //就是应用中drawable和mipmap目录
//        Glide.with(this)
//                .load(R.drawable.placeholder)
//                .into(iv_avatar);

//        //测试网络图片
//        Glide.with(this)
//                .load("http://dev-courses-misuc.ixuea.com/1da1c001e89c4b8780ac8f9780ef881f.jpg")
//                .into(iv_avatar);

        //其他配置
//        Glide.with(this)
//                .load("http://dev-courses-misuc.ixuea.com/1da1c001e89c4b8780ac8f9780ef881f.jpg")
//                //从中心裁剪
//                .centerCrop()
//                //占位图
//                //就是当前真实的图片没有显示出来前
//                //显示的图片
//                .placeholder(R.drawable.placeholder)
//                .into(iv_avatar);

        //默认选中第二个界面
        //设置监听器在选择就会调用监听器(因为将监听器和指示器绑定了，所以滑动到第二个页面，上面的标题就会跟着跳转)
        vp.setCurrentItem(1);

        //获取用户信息
        //当然可以在用户要显示侧滑的时候
        //才获取用户信息
        //这样可以减少请求

        //获取用户信息
        fetchUserData();
    }

    @Override
    protected void initListeners() {
        super.initListeners();

        //添加侧滑监听器
        //注意： 这里跟前面的那个是不一样的（前面的是：dl.addDrawerListener(toggle)
        // 前的监听器是ActionBarDrawerToggle ，而这里的是DrawerLayout.DrawerListener）
        dl.addDrawerListener(new DrawerLayout.DrawerListener() {
            @Override
            public void onDrawerSlide(@NonNull View drawerView, float slideOffset) {

            }

            /**
             * 侧滑打开了
             */
            @Override
            public void onDrawerOpened(@NonNull View drawerView) {
                LogUtil.d(TAG, "onDrawerOpened");
                //侧滑打开了，这样可以节省资源
                fetchUserData();
            }

            @Override
            public void onDrawerClosed(@NonNull View drawerView) {

            }

            @Override
            public void onDrawerStateChanged(int newState) {

            }
        });
    }

    /**
     * 之所以在在onResume设置的原因
     *
     * 防止用户授权 偷偷的在后台关闭权限；这样一回到应用中马上就能判断出来有没有权限
     */
    @Override
    protected void onResume() {
        super.onResume();

        //只有6.0以上版本才需要请求
        //低版本默认就该权限
        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.M) {
            //检查是否有悬浮权限
            requestDrawOverlays();
        }
    }

    /**
     * 请求悬浮权限
     *
     * @return
     */
    @RequiresApi(api = Build.VERSION_CODES.M)
    private boolean requestDrawOverlays() {
        if (!Settings.canDrawOverlays(getMainActivity())) {
            //如果没有显示浮窗的权限
            //就跳转到设置界面

            //真实项目中
            //应该在使用的位置在请求
            Intent intent = new Intent(Settings.ACTION_MANAGE_OVERLAY_PERMISSION, Uri.parse("package:" + getMainActivity().getPackageName()));
            //开启界面返回值
            startActivityForResult(intent, Constant.REQUEST_OVERLAY_PERMISSION);

            return false;
        }
        //返回true 表示有权限(没有进入if里面 说明已经授权了)
        return true;
    }

    /**
     * 使用startActivityForResult启动界面后的回调
     * 从第二个页面启动回来的回调方法
     */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case Constant.REQUEST_OVERLAY_PERMISSION:
                if (Build.VERSION.SDK_INT > Build.VERSION_CODES.M) {
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                        if (requestDrawOverlays()) {
                            //授权成功
                            LogUtil.d(TAG, "onActivityResult DrawOverlays success");
                        } else {
                            //授权失败
                            LogUtil.d(TAG, "onActivityResult DrawOverlays failed");
                        }
                    }
                }
                break;
        }
    }

    /**
     * 请求数据
     * sp.getUserId():因为登录成功后保存了用户的id，所以通过地址和id连接起来可以访问改用户的详细信息
     */
    private void fetchUserData() {
        Api.getInstance().userDetail(sp.getUserId())
                .subscribe(new HttpObserver<DetailResponse<User>>() {
                    @Override
                    public void onSucceeded(DetailResponse<User> data) {
                        next(data.getData());
                    }
                });
    }

    /**
     * 显示数据
     *
     * @param data User
     */
    private void next(User data) {
        // 显示头像
        //1.上下文  2，显示图片控件 3，显示头像地址（包含绝对路径（完整网址）和相对路径（就是地址后面的，比如11123.png））
        ImageUtil.showAvatar(getMainActivity(), iv_avatar, data.getAvatar());

        //显示昵称
        tv_nickname.setText(data.getNickname());

        //显示描述
        tv_description.setText(data.getDescriptionFormat());
    }

    /**
     * 界面已经显示了
     * 不需要再次创建新界面的时候调用
     *
     * @param intent Intent
     */
    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);

        LogUtil.d(TAG, "onNewIntent");
        processIntent(intent);
    }

    /**
     * 处理动作
     *
     * @param intent Intent
     */
    private void processIntent(Intent intent) {
        if (Constant.ACTION_AD.equals(intent.getAction())) {
            //广告点击

            //显示广告界面
            WebViewActivity.start(getMainActivity(), "活动标题", intent.getStringExtra(Constant.URL));
        } else if (Constant.ACTION_MUSIC_PLAY_CLICK.equals(intent.getAction())) {
            //音乐点击

            //进入音乐播放界面
            startMusicPlayerActivity();
        }
    }

    /**
     * 用户点击了（这里是外层的ll_user 设置点击事件）
     */
    @OnClick(R.id.ll_user)
    public void onUserClick() {
        LogUtil.d(TAG, "onUserClick:");
        //这里是用父类到的startActivityExtraId方法(当然也可以用UserDetailActivity里面的startWithId方法)
        startActivityExtraId(UserDetailActivity.class, sp.getUserId());
        ///关闭抽屉
        cloneDrawer();
    }

    /**
     * 好友点击
     */
    @OnClick(R.id.ll_friend)
    public void onFriendClick() {
        LogUtil.d(TAG, "onFriendClick:");
        UserActivity.start(getMainActivity(), sp.getUserId(), UserActivity.FRIEND);
        //关闭抽屉
        cloneDrawer();
    }

    /**
     * 粉丝点击
     */
    @OnClick(R.id.ll_fans)
    public void onFansClick() {
        UserActivity.start(getMainActivity(), sp.getUserId(), UserActivity.FANS);
        cloneDrawer();
    }

    /**
     * 设置点击了
     */
    @OnClick(R.id.ll_setting)
    public void onSettingClick() {
        startActivity(SettingActivity.class);
        cloneDrawer();
    }

    /**
     * 关闭抽屉
     */
    private void cloneDrawer() {
        //START 就是我们显示抽屉的方向
        dl.closeDrawer(GravityCompat.START);
    }
}
