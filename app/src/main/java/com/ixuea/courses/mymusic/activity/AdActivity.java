package com.ixuea.courses.mymusic.activity;

import android.os.Bundle;
import android.os.CountDownTimer;
import android.widget.Button;

import com.ixuea.courses.mymusic.R;
import com.ixuea.courses.mymusic.util.LogUtil;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * 广告界面
 */
public class AdActivity extends BaseCommonActivity {

    private static final String TAG = "AdActivity";

    /**
     * 跳过广告按钮
     */
    @BindView(R.id.bt_skip_ad)
    Button bt_skip_ad;
    private CountDownTimer countDownTimer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ad);
    }


    @Override
    protected void initView() {
        super.initView();

        //全屏
        fullScreen();
    }

    /**
     * 界面以及显示出来了调用
     * 从后台切换到前台还会调用
     */
    @Override
    protected void onResume() {
        super.onResume();

        //倒计时的总时间,间隔
        //单位为毫秒

        //创建倒计时
        countDownTimer = new CountDownTimer(5000, 1000) {

            /**
             * 每次间隔调用
             */
            @Override
            public void onTick(long millisUntilFinished) {
                //可能会倒回到0，所以这里加1
                bt_skip_ad.setText(getString(R.string.count_second, millisUntilFinished / 1000 + 1));
            }

            /**
             * 倒计时完成
             */
            @Override
            public void onFinish() {
                //执行下一步方法
                next();
            }
        };

        //启动定时器
        countDownTimer.start();
    }

    /**
     * 进入首页
     */
    private void next() {
//        startActivityAfterFinishThis(MainActivity.class);

        //显示网页
        WebViewActivity.start(getMainActivity(), "活动详情", "http://www.ixuea.com");

    }

    /**
     * 广告点击了
     */
    @OnClick(R.id.bt_ad)
    public void onAdClick() {
        LogUtil.d(TAG, "onAdClick");
    }

    /**
     * 跳过广告按钮
     */
    @OnClick(R.id.bt_skip_ad)
    public void onSkipClick() {
        LogUtil.d(TAG, "onSkipClick");
        //取消倒计时并跳转到首页
        cancelCountDown();//记得取消倒计时，否则会出现内存泄漏
        next();
    }

    /**
     * 界面销毁了调用
     */
    @Override
    protected void onDestroy() {
        cancelCountDown();
        super.onDestroy();
    }

    /**
     * 取消倒计时
     */
    private void cancelCountDown() {
        if (countDownTimer != null) {
            countDownTimer.cancel();
        }
    }
}
