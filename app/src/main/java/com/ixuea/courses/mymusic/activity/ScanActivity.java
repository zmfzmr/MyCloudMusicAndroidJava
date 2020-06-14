package com.ixuea.courses.mymusic.activity;

import android.os.Bundle;
import android.view.MotionEvent;
import android.view.SurfaceView;

import com.ixuea.courses.mymusic.R;
import com.ixuea.courses.mymusic.util.LogUtil;
import com.ixuea.courses.mymusic.util.ToastUtil;
import com.ixuea.courses.mymusic.util.UrlUtil;
import com.king.zxing.CaptureHelper;
import com.king.zxing.OnCaptureCallback;
import com.king.zxing.ViewfinderView;

import org.apache.commons.lang3.StringUtils;

import java.util.Map;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * 扫描二维码界面
 */
public class ScanActivity extends BaseTitleActivity implements OnCaptureCallback {
    private static final String TAG = "ScanActivity";
    /**
     * 扫描预览视图
     */
    @BindView(R.id.surfaceView)
    SurfaceView surfaceView;
    /**
     * 扫描框
     */
    @BindView(R.id.viewfinderView)
    ViewfinderView viewfinderView;
    private CaptureHelper captureHelper;//扫描工具类

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scan);
    }

    @Override
    protected void initView() {
        super.initView();
        //显示亮色状态栏
        lightStatusBar();
    }

    @Override
    protected void initDatum() {
        super.initDatum();

        //创建扫描工具类(里面是怎么扫描 预览的我们不用担心，只需要借助封装工具类即可)
        captureHelper = new CaptureHelper(this, surfaceView, viewfinderView);

        //社会组扫描结果回调
        captureHelper.setOnCaptureCallback(this);

        //设置支持连续扫描(如果数据格式不正确，就显示不支持，继续扫描)
        captureHelper.continuousScan(true);

        //执行创建
        captureHelper.onCreate();

    }

    /**
     * 界面显示了
     */
    @Override
    protected void onResume() {
        super.onResume();

        //开始扫描
        captureHelper.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
        //暂停扫描
        captureHelper.onPause();
    }

    /**
     * 这个这扫描(也就是拍照的这个功能)，在系统中是唯一的，
     * 如果不销毁的话，系统的那个手机拍照是无法运行的
     */
    @Override
    protected void onDestroy() {
        //销毁扫描工具类
        captureHelper.onDestroy();
        super.onDestroy();
    }

    /**
     * 我们也没有仔细了解过，推测是点击对焦功能(就是点击扫描框的里面 对焦图片)
     */
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        //将触摸事件发送到扫描工具类
        captureHelper.onTouchEvent(event);
        return super.onTouchEvent(event);
    }

    /**
     * 我的二维码点击
     */
    @OnClick(R.id.bt_code)
    public void onCodeClick() {
        //这里跟首页抽屉布局里面跳转是一样的
        startActivityExtraId(CodeActivity.class, sp.getUserId());
    }

    /**
     * 接收扫码结果回调
     *
     * @param result 扫码结果(这个结果：是CodeActivity生成二维码的时候已经存储用户信息(也就是网址),
     *               扫描的时候直接把这个网址扫描出来了)
     * @return 返回true表示拦截，将不自动执行后续逻辑，为false表示不拦截，默认不拦截
     */
    @Override
    public boolean onResultCallback(String result) {
        LogUtil.d(TAG, "onResultCallback: " + result);

        if (StringUtils.isNotBlank(result)) {
            //处理扫描结果
            handleScanString(result);
        } else {
            //显示不支持该格式
            showNotSupportFormat();
        }

        //拦截结果
        return true;
    }

    /**
     * 显示不支持该格式
     */
    private void showNotSupportFormat() {
        //先暂停
        captureHelper.onPause();

        //support：翻译 支持的意思
        ToastUtil.errorShortToast(R.string.error_not_support_qrcode_format);

        //延迟后启用扫描
        //目的是防止持续扫描不正确的二维码
        //可以根据需求调整

        //扫描框暂停，继续用的是 viewfinderView
        //注意：ViewfinderView:  是个扫描框
        viewfinderView.postDelayed(new Runnable() {
            /**
             * 这里是在主线程中进行的
             */
            @Override
            public void run() {
                //继续扫描用的是工具类captureHelper的onResume()
                captureHelper.onResume();
            }
        }, 800);
    }

    /**
     * 处理扫描结果
     */
    private void handleScanString(String data) {
        //从扫描的字符串中解析我们需要的参数
        //我们的二维码格式如下：
        //http://dev-my-cloud-music-api-rails.ixuea.com/v1/monitors/version?u=6

        //表示用我的云音乐软件扫描后
        //跳转到Id为6用户详情

        //其实就是我们应用的下载地址
        //我们要用到的参数放到了查询参数
        //生成网址的好处是
        //如果使用其他软件扫描的时候
        //会自动打开网址
        //大部分二维码扫描软件（QQ，微信）都会这样

        //这样就可以引导用户下载我们的应用
        //另外在真实项目中
        //会将上面的网址转短一点
        //因为二维码内容太多了
        //生成的二维码格子就会很小
        //如果显示的图片过小
        //就会非常难识别

        //另外不同的方式生成二维码
        //也可能不同

        //解析出网址中的查询参数
        Map query = UrlUtil.getUrlQuery(data);

        //获取用户id值 （因为这个是Map集合，可以通过key来获取值）
        String userId = (String) query.get("u");

        if (StringUtils.isNotBlank(userId)) {
            //有值
            processUserCode(userId);
        } else {
            //显示不支持该类型
            showNotSupportFormat();
        }

    }

    /**
     * 处理用户二维码
     * (其实就是把前面从网址上解除出来的的用户id获取出来，然后携带用户id跳转到用户详情)
     */
    private void processUserCode(String userId) {
        //关闭当前界面
        finish();

        //跳转到用户详情
        UserDetailActivity.startWithId(getMainActivity(), userId);

    }
}
