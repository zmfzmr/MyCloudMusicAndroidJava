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

import androidx.constraintlayout.widget.ConstraintLayout;
import butterknife.BindView;
import de.hdodenhof.circleimageview.CircleImageView;

/**
 * 音乐黑胶唱片界面
 */
public class MusicRecordFragment extends BaseCommonFragment {

    private static final String TAG = "MusicRecordFragment";
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
    private Song data;

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

    }

    /**
     * 开始旋转
     */
    private void startRecordRotate() {

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
