package com.ixuea.courses.mymusic.activity;

import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.ixuea.courses.mymusic.R;
import com.ixuea.courses.mymusic.util.LogUtil;

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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
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
