package com.ixuea.courses.mymusic.adapter;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.ixuea.courses.mymusic.R;
import com.ixuea.courses.mymusic.domain.Song;
import com.ixuea.courses.mymusic.domain.event.OnStartRecordEvent;
import com.ixuea.courses.mymusic.domain.event.OnStopRecordEvent;
import com.ixuea.courses.mymusic.fragment.BaseCommonFragment;
import com.ixuea.courses.mymusic.util.Constant;
import com.ixuea.courses.mymusic.util.ImageUtil;
import com.ixuea.courses.mymusic.util.LogUtil;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.Timer;
import java.util.TimerTask;

import androidx.constraintlayout.widget.ConstraintLayout;
import butterknife.BindView;
import de.hdodenhof.circleimageview.CircleImageView;

/**
 * 音乐黑胶唱片界面
 */
public class MusicRecordFragment extends BaseCommonFragment {

    private static final String TAG = "MusicRecordFragment";
    /**
     * 每16毫秒旋转的角度
     * 16毫秒是通过
     * 每秒60帧计算出来的
     * 也就是1000 / 60 = 16
     * 也就是说绘制一帧要在16毫秒中完成
     * 不然就能感觉卡顿
     * 用秒表侧转一圈的时间
     * <p>
     * 假设：转一圈30s秒 360度  360/30 = 12  每秒转12度
     * 1s转12度 换成毫秒  = 12 / 1000,这里是16毫秒，再乘以16，所以是12 /1000 *16 = 0.192
     * <p>
     * 这里用的0.2304F 秒转一圈360
     */
    private static final float ROTATION_PER = 0.2304F;//float 类型
    /**
     * 黑胶唱片容器
     */
    @BindView(R.id.cl_content)
    ConstraintLayout cl_content;

    /**
     * 歌曲封面
     */
    @BindView(R.id.iv_banner)
    CircleImageView iv_banner;
    private Song data;//唱片fragment对应的Song对象
    private TimerTask timerTask;//定时器任务
    private Timer timer;
    private float recordRotation;//旋转的角度 注意这里：是float类型

    @Override
    protected void initDatum() {
        super.initDatum();
        //出入的是this（就是判断有没有注册）
        if (!EventBus.getDefault().isRegistered(this)) {
            //注册发布订阅框架
            EventBus.getDefault().register(this);
        }


        //获取传递的数据
        data = (Song) extraData();//当前ViewPager的item 中fragment中传递过来的音乐对象（在fragment的构造方法中传递过来的）

        //显示封面
        ImageUtil.show(getMainActivity(), iv_banner, data.getBanner());
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onStartRecordEvent(OnStartRecordEvent event) {
        //由于Fragment放入ViewPager中
        //他的生命周期就改变了
        //所以不能通过onResume这样的方法判断
        //当前Fragment是否显示
        //所有这里解决方法是
        //通过事件传递当前音乐
        //如果当前音乐匹配
        //当前Fragment就是操作当前fragment
        //如果不是就忽略

        //event.getData():是当前播放的Song对象  data：ViewPager 中item fragment中传入的Song对象（getData(position)）
        //MusicPlayerAdapter:中的getItem 返回当前位置的item（也就是当前fragment）
        //简写：当前播放的音乐Song对象，等于==  当前fragment中(唱片中)的Song对象
        if (event.getData() == data) {
            LogUtil.d(TAG, "onStartRecordEvent: " + data.getTitle());

            startRecordRotate();//开始旋转
        }

    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onStopRecordEvent(OnStopRecordEvent event) {

        if (event.getData() == data) {
            LogUtil.d(TAG, "onStopRecordEvent: " + data.getTitle());

            stopRecordRotate();
        }
    }

    /**
     * 停止转动
     */
    private void stopRecordRotate() {
        //停止定时器任务
        if (timerTask != null) {
            timerTask.cancel();
            timerTask = null;
        }

        //停止定时器
        if (timer != null) {
            timer.cancel();
            timer = null;
        }
    }

    /**
     * 开始旋转
     */
    private void startRecordRotate() {
        if (timerTask != null) {
            //已经启动了
            return;
        }
        //创建定时器任务
        timerTask = new TimerTask() {
            @Override
            public void run() {
                //data:唱片fragment对应的Song对象
                LogUtil.d(TAG, "startRecordRotate: " + data.getTitle());

                //如果旋转的角度大于等于360
                if (recordRotation >= 360) {
                    //就设置为0
                    recordRotation = 0;
                }

                //每次加旋转的偏移
                recordRotation += ROTATION_PER;

                //旋转（ConstraintLayout约束布局中设置  setRotation是父类View中的，所以说每个View子类都有）
                cl_content.setRotation(recordRotation);
            }
        };
        //创建定时器
        timer = new Timer();
        //创建定时器
        timer.schedule(timerTask,0,16);
    }

    /**
     * 返回要显示的控件
     */
    @Override
    protected View getLayoutView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_record_music, container, false);
    }

    /**
     * 创建方法
     */
    public static MusicRecordFragment newInstance(Song data) {

        Bundle args = new Bundle();

        //传递数据
        args.putSerializable(Constant.DATA, data);

        MusicRecordFragment fragment = new MusicRecordFragment();
        fragment.setArguments(args);
        return fragment;
    }

    /**
     * 界面销毁时
     */
    @Override
    public void onDestroy() {
        if (EventBus.getDefault().isRegistered(this)) {
            //取消注册
            EventBus.getDefault().unregister(this);
        }
        super.onDestroy();//在调用父类方法之前（之前或者之后，好像影响不大）

    }
}
