package com.ixuea.courses.mymusic.activity;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.TextView;

import com.ixuea.courses.mymusic.R;
import com.ixuea.courses.mymusic.adapter.ImageSelectAdapter;
import com.ixuea.courses.mymusic.api.Api;
import com.ixuea.courses.mymusic.domain.BaseModel;
import com.ixuea.courses.mymusic.domain.Feed;
import com.ixuea.courses.mymusic.domain.event.OnFeedChangedEvent;
import com.ixuea.courses.mymusic.domain.response.DetailResponse;
import com.ixuea.courses.mymusic.listener.HttpObserver;
import com.ixuea.courses.mymusic.util.LogUtil;
import com.ixuea.courses.mymusic.util.ToastUtil;
import com.luck.picture.lib.PictureSelector;
import com.luck.picture.lib.config.PictureConfig;
import com.luck.picture.lib.config.PictureMimeType;
import com.luck.picture.lib.entity.LocalMedia;

import org.apache.commons.lang3.StringUtils;
import org.greenrobot.eventbus.EventBus;

import java.util.List;

import androidx.annotation.Nullable;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import butterknife.BindView;
import butterknife.OnClick;

/**
 * 发布动态界面
 */
public class PublishFeedActivity extends BaseTitleActivity implements TextWatcher {

    private static final String TAG = "PublishFeedActivity";

    /**
     * 输入框
     */
    @BindView(R.id.et_content)
    EditText et_content;
    /**
     * 图片列表控件
     */
    @BindView(R.id.rv)
    RecyclerView rv;

    /**
     * 当前位置
     */
    @BindView(R.id.tv_position)
    TextView tv_position;

    /**
     * 字数统计
     */
    @BindView(R.id.tv_count)
    TextView tv_count;
    private String content;//输入的内容
    private ImageSelectAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_publish_feed);
    }

    @Override
    protected void initView() {
        super.initView();

        //尺寸固定
        rv.setHasFixedSize(true);

        //禁止嵌套滚动
        rv.setNestedScrollingEnabled(false);

        GridLayoutManager layoutManager = new GridLayoutManager(getMainActivity(), 4);
        rv.setLayoutManager(layoutManager);
    }

    @Override
    protected void initDatum() {
        super.initDatum();

        //也可以是在用户选择了图片之后初始化这个适配器（这样性能更好一点），否则用户永远不选择图片，有点耗费性能
        //那样的处理逻辑多点，我们这里直接在initDatum处理了

        //创建适配器
        adapter = new ImageSelectAdapter(R.layout.item_image_select);
        rv.setAdapter(adapter);
    }

    @Override
    protected void initListeners() {
        super.initListeners();

        //添加输入框监听器
        et_content.addTextChangedListener(this);

        //点击点击事件   注意： 这里是：有个child 的
        adapter.setOnItemChildClickListener((adapter, view, position) -> {
            adapter.remove(position);
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.publish, menu);
        return true;
    }

    /**
     * 按钮点击了
     */
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.action_publish) {
            //发布按钮
            onSendMessageClick();

            return true;//返回true 表示处理了事件
        }
        return super.onOptionsItemSelected(item);
    }

    /**
     * 发布按钮点击
     */
    private void onSendMessageClick() {
        LogUtil.d(TAG, "onSendMessageClick");

        //获取输入的内容
        content = et_content.getText().toString().trim();

        //判断是否输入了
        if (StringUtils.isBlank(content)) {
            //提示: 请输入你想说的
            ToastUtil.errorShortToast(R.string.hint_feed);
            return;
        }

        //判断长度
        //至于为什么是140
        //市面上大部分软件都是这样
        //大家感兴趣可以搜索下   因为EditText限制 最大字符 maxLength = 140，所以内容不可能大于140的，
        // 所以下面的不用写了
//        if (content.length() > 140) {
//            //提示 内容不能超过140字符
//            ToastUtil.errorShortToast(R.string.error_content_length);
//            return;
//        }

        saveFeed();
    }

    /**
     * 保存动态
     */
    private void saveFeed() {
        Feed feed = new Feed();

        //设置内容
        //因为Feed里面有个content(String类型) 我们直接把内容传递到这个字符串里面
        //网络请求的时候以转换成json传递
        feed.setContent(content);

        //调用接口保存
        Api.getInstance()
                .createFeed(feed)
                .subscribe(new HttpObserver<DetailResponse<BaseModel>>() {
                    @Override
                    public void onSucceeded(DetailResponse<BaseModel> data) {
                        //发布通知
                        EventBus.getDefault().post(new OnFeedChangedEvent());

                        //关闭界面
                        finish();
                    }
                });
    }

    /**
     * 选择图片快捷按钮点击
     */
    @OnClick(R.id.ib_select_image)
    public void onSelectImageClick() {
        LogUtil.d(TAG, "onSelectImageClick");

        //选择图片
        selectImage();
    }

    /**
     * 选择图片
     */
    private void selectImage() {
        //进入相册
        //以下是例子
        //用不到的api可以不写
        PictureSelector.create(this)
                .openGallery(PictureMimeType.ofImage())//全部.PictureMimeType.ofAll()、图片.ofImage()、视频.ofVideo()、音频.ofAudio()
                //.theme()//主题样式(不设置为默认样式) 也可参考demo values/styles下 例如：R.style.picture.white.style
                .maxSelectNum(9)// 最大图片选择数量 int
                .minSelectNum(1)// 最小选择数量 int
                .imageSpanCount(3)// 每行显示个数 int
                .selectionMode(PictureConfig.MULTIPLE)// 多选 or 单选 PictureConfig.MULTIPLE or PictureConfig.SINGLE
                .previewImage(true)// 是否可预览图片 true or false
                //.previewVideo()// 是否可预览视频 true or false
                //.enablePreviewAudio() // 是否可播放音频 true or false
                .isCamera(true)// 是否显示拍照按钮 true or false
                .imageFormat(PictureMimeType.JPEG)// 拍照保存图片格式后缀,默认jpeg
                .isZoomAnim(true)// 图片列表点击 缩放效果 默认true
                .sizeMultiplier(0.5f)// glide 加载图片大小 0~1之间 如设置 .glideOverride()无效
                //.setOutputCameraPath("/CustomPath")// 自定义拍照保存路径,可不填
                //.enableCrop()// 是否裁剪 true or false
                .compress(true)// 是否压缩 true or false
                //.glideOverride()// int glide 加载宽高，越小图片列表越流畅，但会影响列表图片浏览的清晰度
                //.withAspectRatio()// int 裁剪比例 如16:9 3:2 3:4 1:1 可自定义
                //.hideBottomControls()// 是否显示uCrop工具栏，默认不显示 true or false
                //.isGif()// 是否显示gif图片 true or false
                //.compressSavePath(getPath())//压缩图片保存地址
                //.freeStyleCropEnabled()// 裁剪框是否可拖拽 true or false
                //.circleDimmedLayer()// 是否圆形裁剪 true or false
                //.showCropFrame()// 是否显示裁剪矩形边框 圆形裁剪时建议设为false   true or false
                //.showCropGrid()// 是否显示裁剪矩形网格 圆形裁剪时建议设为false    true or false
                //.openClickSound()// 是否开启点击声音 true or false
                //.selectionMedia(selectedImage)// 是否传入已选图片 List<LocalMedia> list
                .previewEggs(true)// 预览图片时 是否增强左右滑动图片体验(图片滑动一半即可看到上一张是否选中) true or false
                //.cropCompressQuality()// 裁剪压缩质量 默认90 int
                //.minimumCompressSize(100)// 小于100kb的图片不压缩
                //.synOrAsy(true)//同步true或异步false 压缩 默认同步
                //.cropWH()// 裁剪宽高比，设置如果大于图片本身宽高则无效 int
                //.rotateEnabled() // 裁剪是否可旋转图片 true or false
                //.scaleEnabled()// 裁剪是否可放大缩小图片 true or false
                //.videoQuality()// 视频陆制质量 0 or 1 int
                //.videoMaxSecond(15)// 显示多少秒以内的视频or音频也可适用 int
                //.videoMinSecond(10)// 显示多少秒以内的视频or音频也可适用 int
                //.recordVideoSecond()//视频秒数陆制 默认60s int
                //.isDragFrame(false)// 是否可拖动裁剪框(固定)
                .forResult(PictureConfig.CHOOSE_REQUEST);//结果回调onActivityResult code
    }


    //输入框监听器

    /**
     * 文本改变前
     *
     * @param s
     * @param start
     * @param count
     * @param after
     */
    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

    }

    /**
     * 文本改变了
     *
     * @param s
     * @param start
     * @param before
     * @param count
     */
    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {

    }

    /**
     * 文本改变后
     *
     * @param s
     */
    @Override
    public void afterTextChanged(Editable s) {
        String info = getResources().getString(R.string.feed_count, s.toString().length());
        tv_count.setText(info);

    }
    //end 输入框监听器


    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        //注意：这里是先判断结果码
        if (resultCode == RESULT_OK) {
            //请求成功

            //也可以不判断，因为我们这里只有一个这个回调 使用这个方法
            switch (requestCode) {
                case PictureConfig.CHOOSE_REQUEST:
                    //选择了媒体回调

                    //获取选择的资源  data:  方法参数里面的Intent
                    List<LocalMedia> datum = PictureSelector.obtainMultipleResult(data);
                    // 例如 LocalMedia 里面返回三种path
                    // 1.media.getPath(); 为原图path
                    // 2.media.getCutPath();为裁剪后path，需判断media.isCut();是否为true  注意：音视频除外
                    // 3.media.getCompressPath();为压缩后path，需判断media.isCompressed();是否为true  注意：音视频除外
                    // 如果裁剪并压缩了，以取压缩路径为准，因为是先裁剪后压缩的
                    LogUtil.d(TAG, "onActivityResult medias:" + datum.size());

                    //设置数据
                    setData(datum);
                    break;
            }
        }
    }

    /**
     * 设置数据
     */
    private void setData(List<LocalMedia> datum) {
        adapter.replaceData(datum);
    }
}
