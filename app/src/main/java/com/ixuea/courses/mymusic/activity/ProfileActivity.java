package com.ixuea.courses.mymusic.activity;

import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.ixuea.courses.mymusic.R;
import com.ixuea.courses.mymusic.api.Api;
import com.ixuea.courses.mymusic.domain.User;
import com.ixuea.courses.mymusic.domain.response.DetailResponse;
import com.ixuea.courses.mymusic.listener.HttpObserver;
import com.ixuea.courses.mymusic.util.ImageUtil;
import com.ixuea.courses.mymusic.util.LogUtil;

import org.apache.commons.lang3.StringUtils;

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
    }

    /**
     * 头像容器点击
     */
    @OnClick(R.id.avatar_container)
    public void onAvatarClick() {
        LogUtil.d(TAG, "onAvatarClick");
    }

    /**
     * 性别 点击
     */
    @OnClick(R.id.gender_container)
    public void onGenderClick() {
        LogUtil.d(TAG, "onGenderClick");
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
}
