package com.ixuea.courses.mymusic.activity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.alibaba.sdk.android.oss.OSSClient;
import com.alibaba.sdk.android.oss.model.PutObjectRequest;
import com.alibaba.sdk.android.oss.model.PutObjectResult;
import com.ixuea.courses.mymusic.R;
import com.ixuea.courses.mymusic.api.Api;
import com.ixuea.courses.mymusic.domain.BaseModel;
import com.ixuea.courses.mymusic.domain.User;
import com.ixuea.courses.mymusic.domain.event.OnUserChangedEvent;
import com.ixuea.courses.mymusic.domain.response.DetailResponse;
import com.ixuea.courses.mymusic.fragment.GenderDialogFragment;
import com.ixuea.courses.mymusic.listener.HttpObserver;
import com.ixuea.courses.mymusic.util.Constant;
import com.ixuea.courses.mymusic.util.ImageUtil;
import com.ixuea.courses.mymusic.util.LogUtil;
import com.ixuea.courses.mymusic.util.OSSUtil;
import com.ixuea.courses.mymusic.util.StringUtil;
import com.ixuea.courses.mymusic.util.ToastUtil;
import com.ixuea.courses.mymusic.util.UUIDUtil;
import com.luck.picture.lib.PictureSelector;
import com.luck.picture.lib.config.PictureConfig;
import com.luck.picture.lib.config.PictureMimeType;
import com.luck.picture.lib.entity.LocalMedia;

import org.apache.commons.lang3.StringUtils;
import org.greenrobot.eventbus.EventBus;

import java.util.List;

import androidx.annotation.Nullable;
import butterknife.BindView;
import butterknife.OnClick;

/**
 * 编辑我的资料界面
 */
public class ProfileActivity extends BaseTitleActivity {

    private static final String TAG = "ProfileActivity";

    /**
     * 头像
     */
    @BindView(R.id.iv_avatar)
    ImageView iv_avatar;

    /**
     * 昵称
     */
    @BindView(R.id.et_nickname)
    EditText et_nickname;

    /**
     * 性别
     */
    @BindView(R.id.tv_gender)
    TextView tv_gender;

    /**
     * 生日
     */
    @BindView(R.id.tv_birthday)
    TextView tv_birthday;

    /**
     * 地区
     */
    @BindView(R.id.tv_area)
    TextView tv_area;

    /**
     * 个人介绍
     */
    @BindView(R.id.et_description)
    EditText et_description;

    /**
     * 手机
     */
    @BindView(R.id.tv_phone)
    TextView tv_phone;

    /**
     * 邮箱
     */
    @BindView(R.id.tv_email)
    TextView tv_email;

    /**
     * qq按钮
     */
    @BindView(R.id.bt_qq)
    Button bt_qq;

    /**
     * 微博按钮
     */
    @BindView(R.id.bt_weibo)
    Button bt_weibo;
    private User data;//用户对象
    private String avatarFileName;//图片文件名称(就是图片相对路径(阿里云里面的相对路径 如： dgaddad.jpg))
    private String nickname;
    private String description;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
    }

    @Override
    protected void initDatum() {
        super.initDatum();

        Api.getInstance()
                .userDetail(sp.getUserId())
                .subscribe(new HttpObserver<DetailResponse<User>>() {
                    @Override
                    public void onSucceeded(DetailResponse<User> data) {
                        next(data.getData());
                    }
                });
    }

    /**
     * 网络请求下一步
     */
    private void next(User data) {
        this.data = data;

        //头像
        ImageUtil.showAvatar(getMainActivity(), iv_avatar, data.getAvatar());

        //昵称
        et_nickname.setText(data.getNickname());

        //性别
        showGender();

        //生日
        showBirthday();

        //地区
        showArea();

        //描述
        if (StringUtils.isNotBlank(data.getDescription())) {
            //注意：这里我们不用getDescriptionFormat，直接用getDescription就行了
            //（因为没有个人描述，直接显示空值就好了,因为这个是EditText 可以编辑的）
            et_description.setText(data.getDescriptionFormat());
        }

        //手机号
        tv_phone.setText(data.getPhone());

        //邮箱
        tv_email.setText(data.getEmail());

        //qq绑定状态
        if (StringUtils.isNotBlank(data.getQq_id())) {
            //qqId 也就是qq登录后的id
            //绑定了
            showUnbindButtonStatus(bt_qq);

        } else {
            //没有绑定

            //显示绑定状态
            showBindButtonStatus(bt_qq);
        }


        //微博绑定状态  微博登录后的id 这个值是加密，就算拿到也是无法登录
        if (StringUtils.isNotBlank(data.getWeibo_id())) {
            //绑定了

            //显示解绑状态
            showUnbindButtonStatus(bt_weibo);
        } else {
            //没有绑定

            //显示绑定状态
            showBindButtonStatus(bt_weibo);
        }
    }

    /**
     * 显示绑定状态
     */
    private void showBindButtonStatus(Button button) {
        button.setText(R.string.bind);
        button.setTextColor(getResources().getColor(R.color.colorPrimary));
        button.setBackgroundResource(R.drawable.shape_border_color_primary);
    }

    /**
     * 显示解绑状态
     */
    private void showUnbindButtonStatus(Button button) {
        button.setText(R.string.unbind);
        button.setTextColor(getResources().getColor(R.color.light_grey));
        button.setBackgroundResource(R.drawable.shape_light_grey);
    }

    /**
     * 显示地区
     */
    private void showArea() {
        //地区也不一定有，所以需要判断
        if (StringUtils.isNotBlank(data.getProvince())) {
            //省-市-区
            String area = getResources().getString(R.string.area_value2,
                    data.getProvince(),
                    data.getCity(),
                    data.getArea());
            tv_area.setText(area);
        }
    }

    /**
     * 显示生日
     */
    private void showBirthday() {
        //生日 并一定有，所以需要判断下
        if (StringUtils.isNotBlank(data.getBirthday())) {
            tv_birthday.setText(data.getBirthday());
        }
    }

    /**
     * 显示性别
     */
    private void showGender() {
        tv_gender.setText(data.getGenderFormat());

    }

    /**
     * 返回菜单
     *
     * @param menu
     * @return
     */
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.save, menu);
        return true;
    }

    /**
     * 菜单点击了
     *
     * @param item
     * @return
     */
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_save:
                //保存按钮点击了
                onSaveClick();
                break;
            default:
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    /**
     * 保存按钮点击了
     */
    private void onSaveClick() {
        LogUtil.d(TAG, "onSaveClick");
        //这里就不判断用户是否更改了资料
        //只要点击保存就更新
        //因为要判断比较麻烦

        //获取昵称
        nickname = et_nickname.getText().toString().trim();

        //判断是否输入昵称
        if (TextUtils.isEmpty(nickname)) {
            //提示： 请输入手机号/邮箱
            ToastUtil.errorShortToast(R.string.enter_username);
            return;
        }

        //昵称格式判断(记得取非)
        if (!StringUtil.isNickName(nickname)) {
            ToastUtil.errorShortToast(R.string.error_nickname_format);
        }

        //获取描述
        description = et_description.getText().toString().trim();

        //更新资料
        updateUserInfo();
    }

    /**
     * 头像容器点击
     * <p>
     * PictureSelector 前面发动态时用的 那个图片选择框架
     */
    @OnClick(R.id.avatar_container)
    public void onAvatarClick() {
        LogUtil.d(TAG, "onAvatarClick");

        //进入相册
        //以下是例子
        //用不到的api可以不写
        PictureSelector
                .create(getMainActivity())
                .openGallery(PictureMimeType.ofImage())//全部.PictureMimeType.ofAll()、图片.ofImage()、视频.ofVideo()、音频.ofAudio()
                //.theme()//主题样式(不设置为默认样式) 也可参考demo values/styles下 例如：R.style.picture.white.style
                .maxSelectNum(1)// 最大图片选择数量 int
                .minSelectNum(1)// 最小选择数量 int
                .imageSpanCount(3)// 每行显示个数 int
                .selectionMode(PictureConfig.SINGLE)// 多选 or 单选 PictureConfig.MULTIPLE or PictureConfig.SINGLE
                .previewImage(true)// 是否可预览图片 true or false
                .previewVideo(false)// 是否可预览视频 true or false
                .enablePreviewAudio(false) // 是否可播放音频 true or false
                .isCamera(true)// 是否显示拍照按钮 true or false
                .imageFormat(PictureMimeType.JPEG)// 拍照保存图片格式后缀,默认jpeg
                .isZoomAnim(true)// 图片列表点击 缩放效果 默认true
                .sizeMultiplier(0.5f)// glide 加载图片大小 0~1之间 如设置 .glideOverride()无效
                //.setOutputCameraPath("/CustomPath")// 自定义拍照保存路径,可不填
                .enableCrop(true)// 是否裁剪 true or false
                .compress(true)// 是否压缩 true or false
                //.glideOverride()// int glide 加载宽高，越小图片列表越流畅，但会影响列表图片浏览的清晰度
                .withAspectRatio(1, 1)// int 裁剪比例 如16:9 3:2 3:4 1:1 可自定义
                //.hideBottomControls()// 是否显示uCrop工具栏，默认不显示 true or false
                .isGif(false)// 是否显示gif图片 true or false
                //.compressSavePath(getPath())//压缩图片保存地址
                //.freeStyleCropEnabled()// 裁剪框是否可拖拽 true or false
                //.circleDimmedLayer()// 是否圆形裁剪 true or false
                .showCropFrame(true)// 是否显示裁剪矩形边框 圆形裁剪时建议设为false   true or false
                .showCropGrid(true)// 是否显示裁剪矩形网格 圆形裁剪时建议设为false    true or false
                .openClickSound(false)// 是否开启点击声音 true or false
                //.selectionMedia()// 是否传入已选图片 List<LocalMedia> list
                .previewEggs(true)// 预览图片时 是否增强左右滑动图片体验(图片滑动一半即可看到上一张是否选中) true or false
                .cropCompressQuality(50)// 裁剪压缩质量 默认90 int
                .minimumCompressSize(50)// 小于100kb的图片不压缩
                .synOrAsy(true)//同步true或异步false 压缩 默认同步
                //.cropWH()// 裁剪宽高比，设置如果大于图片本身宽高则无效 int
                .rotateEnabled(false) // 裁剪是否可旋转图片 true or false
                .scaleEnabled(true)// 裁剪是否可放大缩小图片 true or false
                //.videoQuality()// 视频录制质量 0 or 1 int
                //.videoMaxSecond(15)// 显示多少秒以内的视频or音频也可适用 int
                //.videoMinSecond(10)// 显示多少秒以内的视频or音频也可适用 int
                //.recordVideoSecond()//视频秒数录制 默认60s int
                .isDragFrame(false)// 是否可拖动裁剪框(固定)
                .forResult(PictureConfig.CHOOSE_REQUEST);//结果回调onActivityResult code
    }

    /**
     * 性别 点击
     *
     * 当用户点击有上角保存的时候，这里的设置到User里面的性别，也通过网络请求带过去了
     */
    @OnClick(R.id.gender_container)
    public void onGenderClick() {
        LogUtil.d(TAG, "onGenderClick");

        GenderDialogFragment
                //参数2： 默认选中哪个
                //因为服务器返回的数据getGender等于0 10 20
                // 0/10: 第0个(保密)  10/10： 第1个(男)  20/10: 第2个(女)
                //那第二次点击呢? 因为第第一次点击的时候把设置的0或10或20设置到data(User)中
                //所以第二次点击弹出来的是获取的gender:data.getGender() 是第一次设置的那个int值
                .show(getSupportFragmentManager(), data.getGender() / 10, (dialog, which) -> {
                    //关闭对话框
                    dialog.dismiss();

                    //转换性别格式
                    switch (which) {
                        case 1:
                            //男
                            data.setGender(User.MALE);
                            break;
                        case 2:
                            //女
                            data.setGender(User.FEMALE);
                            break;
                        default:
                            //保密(也就是不知道性别)
                            data.setGender(User.UNKNOWN);
                            break;
                    }
                    //显示性别
                    showGender();
                });
    }

    /**
     * 生日 点击
     */
    @OnClick(R.id.birthday_container)
    public void onBirthdayClick() {
        LogUtil.d(TAG, "onBirthdayClick");
    }

    /**
     * 地区 点击
     */
    @OnClick(R.id.area_container)
    public void onAreaClick() {
        LogUtil.d(TAG, "onAreaClick");
    }

    //手机号 和邮箱 这里不能直接更改，这个是需要在其他地方验证一下，不能在这个地方直接编辑
    // （因为手机号和邮箱就是注册的账号，更改注册账号的话，比较难办）


    /**
     * QQ 点击
     */
    @OnClick(R.id.bt_qq)
    public void onQQClick() {
        LogUtil.d(TAG, "onQQClick");
    }

    /**
     * 微博 点击
     */
    @OnClick(R.id.bt_weibo)
    public void onWeiboClick() {
        LogUtil.d(TAG, "onWeiboClick");
    }

    /**
     * 界面结果回调
     *
     * @param requestCode
     * @param resultCode
     * @param data
     */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            //请求成功了

            //获取选中的图片 data: Intent 对象
            List<LocalMedia> results = PictureSelector.obtainMultipleResult(data);

            //获取第一个图片(因为既然能回到这个onActivityResult这个方法，
            // 说明是选中的了图片的，所以不用判断集合的size是否大于0啦)
            LocalMedia localMedia = results.get(0);

            //上传头像
            uploadAvatar(localMedia.getCompressPath());
        }
    }

    /**
     * 上传头像(上传头像是耗时操作，在子线程中执行)
     */
    @SuppressLint("StaticFieldLeak")
    private void uploadAvatar(String path) {
        LogUtil.d(TAG, "uploadAvatar: " + path);

        //由于头像一般比较小
        //同时也只有一张图片
        //所以可以不用显示加载框

        new AsyncTask<String, Void, Boolean>() {

            @Override
            protected Boolean doInBackground(String... strings) {
                try {
                    //创建oss客户端
                    OSSClient oss = OSSUtil.getInstance(getMainActivity());
                    //上传
                    //OSS如果没有特殊需求建议不要分目陆
                    //如果一定要分不要让目陆名前面连续
                    //例如时间戳倒过来
                    //不然连续请求达到一定量级会有性能影响
                    //https://help.aliyun.com/document_detail/64945.html
                    avatarFileName = UUIDUtil.getUUID() + ".jpg";
                    //创建上传文件请求
                    //参数1：bucket 2.图片相对路径 3.图片本地压缩路径
                    PutObjectRequest putObjectRequest = new PutObjectRequest(
                            Constant.ALIYUN_OSS_BUCKET_NAME, avatarFileName, path);

                    //可以用返回来的的PutObjectResult判断是否上传成功，但是我们这里的代码的是在try catch中的
                    //所以说，如果有上传不成功的话，会直接报异常的；所以所以说不用判断了
                    PutObjectResult putObjectResult = oss.putObject(putObjectRequest);
                    //上传成功
                    return true;
                } catch (Exception e) {
                    //上传失败了
                    e.printStackTrace();
                }
                //默认返回false
                return false;
            }

            /**
             * 异步任务执行完成后调用
             */
            @Override
            protected void onPostExecute(Boolean result) {
                super.onPostExecute(result);
                if (result) {
                    //上传成功

                    //更新用户资料
                    updateUserInfo();
                } else {
                    //原因：上传图片没有成功，avatarFileName(图片相对路径还是原来的值)，
                    // 点击右上角的保存按钮会把整个图片名称保存到用户的信息里面了
                    //这里最好置为null
                    avatarFileName = null;

                    //头像上传失败
                    //真实项目中
                    //可以实现重试
                    //同时重试的时候
                    //只上传失败的图片
                    ToastUtil.errorShortToast(R.string.error_upload_image);
                }
            }
        }.execute(path);
    }

    /**
     * 更新用户资料
     * <p>
     * data:initDatum请求用户详情回来的User对象，把这对象更改下数据，重新发送给服务器就行了(PATCH请求)
     */
    private void updateUserInfo() {
        LogUtil.d(TAG, "updateUserInfo:" + avatarFileName);

        if (StringUtils.isNotBlank(avatarFileName)) {
            //设置头像 avatarFileName: 阿里云上面的相对路径  如 ： 145622.jpg
            //我们服务器保存的是 阿里云的图片相对路径
            data.setAvatar(avatarFileName);
        }

        //昵称是不能为空到的，服务器判断了，为啥客户端也要判断呢
        //主要是为了防止你这个接口被非法判断了
        if (StringUtils.isNotBlank(nickname)) {
            //设置昵称
            data.setNickname(nickname);
        }
        //设置描述(这个描述可以不用判断不为空添加，因为为空的的话，我们在客户端的User对象里面的getDescriptionFormat 做了处理
        // 获取这个描述的时候，为空的话，会直接返回方法里面的内容)
        data.setDescription(description);

        //调用更新用户接口
        Api.getInstance()
                .updateUser(data)
                .subscribe(new HttpObserver<DetailResponse<BaseModel>>() {
                    @Override
                    public void onSucceeded(DetailResponse<BaseModel> data) {
                        //更新成功
                        //不提示任何信息

                        //关闭当前界面
                        //当然也可以不关闭
                        //真实项目中根据业务需求调整就行了

                        //发送通知
                        EventBus.getDefault().post(new OnUserChangedEvent());

                        //关闭界面
                        finish();
                    }
                });
    }
}
