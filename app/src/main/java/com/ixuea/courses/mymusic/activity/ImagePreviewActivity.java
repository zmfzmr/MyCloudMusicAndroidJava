package com.ixuea.courses.mymusic.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

import com.github.chrisbanes.photoview.PhotoView;
import com.ixuea.courses.mymusic.R;
import com.ixuea.courses.mymusic.util.Constant;
import com.ixuea.courses.mymusic.util.ImageUtil;

import butterknife.BindView;

/**
 * 图片预览界面
 * 可以把该界面实现为项目中通用的图片预览界面
 */
public class ImagePreviewActivity extends BaseCommonActivity {
    /**
     * 找到添加的依赖 PhotoView控件
     */
    @BindView(R.id.pv)
    PhotoView pv;
    private String id;//这个是传递过来的Song id，目前还没有用到
    private String url;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_image_preview);
    }

    @Override
    protected void initView() {
        super.initView();

        //亮色状态栏
        lightStatusBar();
    }

    //亮色状态栏

    @Override
    protected void initDatum() {
        super.initDatum();

        //获取id
        id = extraId();
        //获取图片地址
        url = extraString(Constant.URL);
        //显示图片
        ImageUtil.show(getMainActivity(), pv, url);

    }

    /**
     * 启动界面
     *
     * @param activity Activity
     * @param id       数据id
     * @param uri      图片地址
     */
    public static void start(Activity activity, String id, String uri) {
        //创建intent
        Intent intent = new Intent(activity, ImagePreviewActivity.class);

        //传递id
        intent.putExtra(Constant.ID, id);

        //传递网址
        intent.putExtra(Constant.URL, uri);

        //启动界面
        activity.startActivity(intent);
    }
}
