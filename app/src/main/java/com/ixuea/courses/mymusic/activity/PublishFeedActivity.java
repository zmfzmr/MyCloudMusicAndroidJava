package com.ixuea.courses.mymusic.activity;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.alibaba.sdk.android.oss.OSSClient;
import com.alibaba.sdk.android.oss.model.PutObjectRequest;
import com.alibaba.sdk.android.oss.model.PutObjectResult;
import com.amap.api.location.AMapLocation;
import com.amap.api.location.AMapLocationClient;
import com.amap.api.location.AMapLocationClientOption;
import com.amap.api.location.AMapLocationListener;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.google.common.collect.Lists;
import com.ixuea.courses.mymusic.R;
import com.ixuea.courses.mymusic.adapter.ImageSelectAdapter;
import com.ixuea.courses.mymusic.api.Api;
import com.ixuea.courses.mymusic.domain.BaseModel;
import com.ixuea.courses.mymusic.domain.Feed;
import com.ixuea.courses.mymusic.domain.Resource;
import com.ixuea.courses.mymusic.domain.event.OnFeedChangedEvent;
import com.ixuea.courses.mymusic.domain.response.DetailResponse;
import com.ixuea.courses.mymusic.listener.HttpObserver;
import com.ixuea.courses.mymusic.util.Constant;
import com.ixuea.courses.mymusic.util.LoadingUtil;
import com.ixuea.courses.mymusic.util.LogUtil;
import com.ixuea.courses.mymusic.util.OSSUtil;
import com.ixuea.courses.mymusic.util.ToastUtil;
import com.ixuea.courses.mymusic.util.UUIDUtil;
import com.luck.picture.lib.PictureSelector;
import com.luck.picture.lib.config.PictureConfig;
import com.luck.picture.lib.config.PictureMimeType;
import com.luck.picture.lib.entity.LocalMedia;

import org.apache.commons.lang3.StringUtils;
import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;
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
    private AMapLocationClient locationClient;//定位客户端

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

        //设置数据(一开始设置个空数据, 所以就显示各+号图片(走 方法setData里面的 if方法))
        setData(new ArrayList<>());

        //在真机测试
        //当然也可以在有模拟定位的模拟器上测试
        //只是比较麻烦
        //所以我们在真机测试

        //初始化定位客户端   注： 这里最好填应用的上下文
        locationClient = new AMapLocationClient(getApplicationContext());
        //设置定位回调监听
        locationClient.setLocationListener(new AMapLocationListener() {
            /**
             * 定位改变后，会调用这个方法(也就是定位成功或者失败后，都会回调这个方法)
             *
             * @param amapLocation
             */
            @Override
            public void onLocationChanged(AMapLocation amapLocation) {
                if (amapLocation != null && amapLocation.getErrorCode() == 0) {
                    //可在其中解析amapLocation获取相应内容

                    //定位成功后
                    //停止定位
                    //因为我们这里只用一次
                    locationClient.stopLocation();

                    //省
                    String province = amapLocation.getProvince();//省

                    //市
                    String city = amapLocation.getCity();

                    //显示当前位置
                    String position = getResources().getString(R.string.current_position, province, city);
                    tv_position.setText(position);


                } else {
                    //走到这里，amapLocation(也就是定位对象可能为null)，所以判断下
                    if (amapLocation != null) {
                        //定位失败时，
                        // 可通过ErrCode（错误码）信息来确定失败的原因，
                        // errInfo是错误信息，
                        // 详见错误码表。
                        LogUtil.d(TAG, "location Error, ErrCode:"
                                + amapLocation.getErrorCode() + ", errInfo:"
                                + amapLocation.getErrorInfo());
                    } else {
                        //未知错误
                    }
                }
            }
        });

        //初始化AMapLocationClientOption对象
        AMapLocationClientOption option = new AMapLocationClientOption();

        //获取一次定位结果： true ：表示一次定位  后面如果用户跑到另外一个地方的话，可能需要多次处理
        //该方法默认为false。
        option.setOnceLocation(true);

        //获取最近3s内精度最高的一次定位结果：
        //反之不会
        //默认为false： false 就是获取最后一次的结果

        //设置定位参数（跟rv.setAdapter() 一样的道理）
        locationClient.setLocationOption(option);

        //开始定位
        locationClient.startLocation();

    }

    @Override
    protected void initListeners() {
        super.initListeners();

        //添加输入框监听器
        et_content.addTextChangedListener(this);

        //设置item点击事件
        adapter.setOnItemClickListener((adapter, view, position) -> {
            Object data = adapter.getItem(position);
            if (data instanceof Integer) {
                //选择图片
                selectImage();
            }
        });

        //点击点击事件   注意： 这里是：有个child 的
        adapter.setOnItemChildClickListener(new BaseQuickAdapter.OnItemChildClickListener() {
            @Override
            public void onItemChildClick(BaseQuickAdapter baseQuickAdapter, View view, int position) {
                baseQuickAdapter.remove(position);

                //下面是点击删除按钮后，保留后面的 + 号图片
                //这个目前先这样实现，可能会有点问题(先注释)
//                int resourceId = R.drawable.ic_add_grey;
//                if (adapter.getItemCount() < 9 && !adapter.getData().contains(resourceId)) {
//                    adapter.addData(resourceId);
//                }
            }
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

        //获取选中的图片
        List<LocalMedia> selectedImage = getSelectedImage();
        if (selectedImage.size() > 0) {
            //有图片

            //先上传图片  upload 上传
            uploadImages(selectedImage);
        } else {
            //没有图片

            //直接发布动态(没有图片的时候，直接传递个null就行了)
            saveFeed(null);
        }
    }

    /**
     * 上传图片
     *
     * @param datum
     */
    private void uploadImages(List<LocalMedia> datum) {


        //上传图片异步任务 异步任务并不只有AsyncTask
        //这里参数1 就是execute(datum) 传入的datum(当然这里可以写Void 因为我们 uploadImages本方法中已经传入进来了)
        // 参数2：进度，暂时用不到，先写出来 3：返回的结果类型
        new AsyncTask<List<LocalMedia>, Integer, List<Resource>>() {

            /**
             * 异步任务指定前调用
             * 主线程中调用
             */
            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                LoadingUtil.showLoading(getMainActivity(), "上传图片中...");
            }

            /**
             * 在子线程中执行
             * @param params
             * @return
             */
            @Override
            protected List<Resource> doInBackground(List<LocalMedia>... params) {
                //可能上传失败，所try catch 包裹
                try {
                    //获取oss客户端 （注： 由于我们上传的是阿里云，用是阿里云的SDK；
                    // 我们用的Retrofit也可以上传图片到自己的服务器，我们这里用不到，也不讲解它）
                    OSSClient oss = OSSUtil.getInstance(getMainActivity());

                    List<LocalMedia> images = params[0];//取出数据(excute() 传入的数据)

                    //创建结果数组
                    ArrayList<Resource> results = new ArrayList<>();

                    //循环每一张图片
                    LocalMedia localMedia = null;
                    for (int i = 0; i < images.size(); i++) {
                        localMedia = images.get(i);

                        //发布进度 publishProgress:这个方法是AsyncTask 的
                        //i 表示正在上传第几张图片
                        publishProgress(i);

                        //上传
                        //OSS如果没有特殊需求建议不要分目录
                        //如果一定要分不要让目陆名前面连续
                        //例如时间戳倒过来（因为如果不倒过来的话，会导致前面的目录名字是一样的）
                        //不然连续请求达到一定量级会有性能影响
                        //https://help.aliyun.com/document_detail/64945.html

                        //阿里云的这个服务，如果资源达到一定量级后，或进行分布式存储(会根据目录放到不同的目录里面)
                        //如果目录名一样会导致大部分的资源会放到同一块里面，会导致 所有的流量都在请求的时候，这个性能会降低
                        //上面的官方文档里面有
                        //我们这里没有进行分目录，直接把所有文件放在根目录里面

                        //oss 中如果名称一样是被替换掉，要保证名字不能一样(相等于个相对路径)
                        String destFileName = UUIDUtil.getUUID() + ".jpg";

                        //upload images:ad87b627524049c7a47c07340270a107.jpg
                        // localMedia.getCompressPath() 选中图片的压缩路径: /storage/emulated/0/Android/data/com.ixuea.courses.mymusic/files/Pictures/1590572407708.jpeg
                        LogUtil.d(TAG, "upload images:" + destFileName + "localMedia.getCompressPath(): " + localMedia.getCompressPath());

                        //创建上传文件请求
                        //上传其他文件也是这样的
                        //他不关心文件具体内容
                        //参数1：bucket
                        // 2：文件图片相对路径(可以这里理解) http://dev-courses-misuc.ixuea.com/ ad87b627524049c7a47c07340270a107.jpg  后面的那串图片名字
                        //    也就是上传到阿里云服务器上面的相对路径  注: 不是本地相对路径
                        PutObjectRequest request = new PutObjectRequest(Constant.ALIYUN_OSS_BUCKET_NAME,
                                //比如adagaddfa.jpg
                                destFileName,
                                //LocalMedia 里面的压缩路径(选中图片后返回的路径(可以理解为本地路径))
                                localMedia.getCompressPath());

                        //上传  注意：这类是result 结果 前面的是添加请求对象
                        PutObjectResult putObjectResult = oss.putObject(request);

                        //如果上传成功
                        //将文件名添加到集合
                        //这里没有很好的处理错误

                        //destFileName  比如adagaddfa.jpg 是个相对路径uri
                        results.add(new Resource(destFileName));
                    }

                    //返回文件名称(集合里面包含了 每个相对路径uri)
                    return results;
                } catch (Exception e) {
                    //出错了
                    e.printStackTrace();
                }
                //默认是返回空的
                return null;
            }

            /**
             * 进度回调
             * 主线程中执行
             */
            @Override
            protected void onProgressUpdate(Integer... values) {
                super.onProgressUpdate(values);
                //取出进度
                Integer index = values[0] + 1;
                LogUtil.d(TAG, "upload images onProgressUpdate: " + index);

                //显示提示
                LoadingUtil.showLoading(getMainActivity(), "正在上传第" + index + "张图片.");
            }

            /**
             * 异步任务执行完成了
             */
            @Override
            protected void onPostExecute(List<Resource> results) {
                super.onPostExecute(results);

                //隐藏提示框
                LoadingUtil.hideLoading();

                //http://dev-courses-misuc.ixuea.com/ ad87b627524049c7a47c07340270a107.jpg
                //http://dev-courses-misuc.ixuea.com/资源路径OSSUtil里面有
                LogUtil.d(TAG, "onPostExecute:" + results);
                //datum ： 本方法uploadImages传入进来的List<LocalMedia>
                if (datum != null && results.size() == datum.size()) {
                    //不等于空 并且大小(上传图片的大小) 和 选中图片的大小一样
                    //说明图片上传成功

                    // 保存动态
                    //保存动态的时候保存这个地址到服务端(List<Resource>保存到服务端 Resource有一个uri地址(相对路径))
                    //也就是说：我们服务端只是保存这个地址，并不保存图片(图片由阿里云服务器保存)

                    saveFeed(results);
                } else {
                    //上传图片失败
                    //真实项目中
                    //可以实现重试
                    //同时重试的时候
                    //只上传失败的图片
                    ToastUtil.errorShortToast(R.string.error_upload_image);
                }
            }
        }
                .execute(datum);
    }

    /**
     * 获取选中的图片
     */
    private List<LocalMedia> getSelectedImage() {
        //因为选中返回的本地图片对象放到适配器里面，取出来里面的数据集合
        List<Object> data = adapter.getData();
        List<LocalMedia> datum = new ArrayList<>();
        //遍历添加到集合
        for (Object o : data) {
            if (o instanceof LocalMedia) {
                datum.add((LocalMedia) o);
            }
        }
        return datum;//返回 List<LocalMedia> 集合
    }

    /**
     * 保存动态
     * @param images
     */
    private void saveFeed(List<Resource> images) {
        Feed feed = new Feed();

        //设置内容
        //因为Feed里面有个content(String类型) 我们直接把内容传递到这个字符串里面
        //网络请求的时候以转换成json传递
        feed.setContent(content);

        //动态的这个Feed模型，里面本来就有这个List<Resource>,直接传递就行了
        //这个Feed 模型 转换成json传递给我们的服务器(这样服务器就保存了我们的List<Resource>(也就是服务器保存了里面uri))
        feed.setImages(images);

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

                    //设置数据  这里需要用Guava 转换成ArrayList(因为要显示多种类型，用这个)
                    setData(Lists.newArrayList(datum));
                    break;
            }
        }
    }

    /**
     * 设置数据  注意：这里是Object类型
     */
    private void setData(List<Object> datum) {
        if (datum.size() != 9) {
            //添加选择图片按钮
            datum.add(R.drawable.ic_add_grey);
        }
        adapter.replaceData(datum);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        //销毁定位客户端
        //同时销毁本地定位服务。
        //因为这个定位是 访问一些硬件资源，不销毁的话，会耗性能
        locationClient.onDestroy();
    }
}
