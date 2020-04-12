package com.ixuea.courses.mymusic.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.FutureTarget;
import com.github.chrisbanes.photoview.PhotoView;
import com.ixuea.courses.mymusic.R;
import com.ixuea.courses.mymusic.util.Constant;
import com.ixuea.courses.mymusic.util.ImageUtil;
import com.ixuea.courses.mymusic.util.LogUtil;
import com.ixuea.courses.mymusic.util.ResourceUtil;
import com.ixuea.courses.mymusic.util.ToastUtil;

import java.io.File;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * 图片预览界面
 * 可以把该界面实现为项目中通用的图片预览界面
 */
public class ImagePreviewActivity extends BaseCommonActivity {
    private static final String TAG = "ImagePreviewActivity";
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

    /**
     * 保存图片按钮点击
     */
    @OnClick(R.id.bt_save)
    public void onSaveClick() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    //FutureTarget会阻塞
                    //所以需要在子线程调用
                    FutureTarget<File> target = Glide.with(getMainActivity())
                            .asFile()//返回一个文件对象
                            .load(ResourceUtil.resourceUri(url))
                            .submit();
                    //将文件拷贝到我们需要的位置
                    File file = target.get();

                    //TODO 将下载的文件保存到相册
                    //getAbsolutePath:获取绝对路径
                    LogUtil.d(TAG, "download image:" + file.getAbsolutePath());
                    //通过这种方法也可以切换到主线程
                    //但只能在activity中使用
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            //弹出提示
                            ToastUtil.successShortToast("下载完成");
                        }
                    });
                } catch (Exception e) {
                    e.printStackTrace();

                    //下载出错了
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            //弹出提示
                            ToastUtil.errorShortToast("下载失败了,请稍后再试");
                        }
                    });
                }
            }
        }).start();
    }
}
