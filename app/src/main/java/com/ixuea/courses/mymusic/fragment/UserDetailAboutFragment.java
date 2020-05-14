package com.ixuea.courses.mymusic.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.ixuea.courses.mymusic.R;
import com.ixuea.courses.mymusic.api.Api;
import com.ixuea.courses.mymusic.domain.User;
import com.ixuea.courses.mymusic.domain.response.DetailResponse;
import com.ixuea.courses.mymusic.listener.HttpObserver;
import com.ixuea.courses.mymusic.util.Constant;

import org.apache.commons.lang3.StringUtils;

import butterknife.BindView;

/**
 * 用户详情-关于界面
 */
public class UserDetailAboutFragment extends BaseCommonFragment {

    /**
     * 昵称
     */
    @BindView(R.id.tv_nickname)
    TextView tv_nickname;

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
     * 描述
     */
    @BindView(R.id.tv_description)
    TextView tv_description;
    private String userId;//用户id

    @Override
    protected void initDatum() {
        super.initDatum();

        //获取用户id
        userId = extraId();

        //可以再次请求网络接口
        //也可以把用户对象传递过来(因为我们在UserDetailActivity那边已经请求过User对象了，不过我们是通过网络请求的)
        Api.getInstance()
                .userDetail(userId)
                .subscribe(new HttpObserver<DetailResponse<User>>() {
                    @Override
                    public void onSucceeded(DetailResponse<User> data) {
                        next(data.getData());
                    }
                });
    }

    /**
     * 显示数据
     *
     * @param data
     */
    private void next(User data) {
        //昵称
        tv_nickname.setText(getResources()
                .getString(R.string.nickname_value, data.getNickname()));

        //性别 注意：这里调用的是getGenderFormat，而不是 getGender
        tv_gender.setText(getResources()
                .getString(R.string.gender_value, data.getGenderFormat()));

        //生日
        tv_birthday.setText(getResources()
                .getString(R.string.birthday_value, data.getBirthday()));

        //地区(因为并不是每个用户都有 地区)

        String area = "";
        if (StringUtils.isNotBlank(data.getProvince())) {
            //有省市区

            StringBuilder sb = new StringBuilder();

            //拼接地区 (这样写可能比较满烦，可以放到string.xml 格式化的方式进行)
            sb.append(data.getProvince());//添加省
            sb.append("-");
            sb.append(data.getCity());//添加市
            sb.append("-");
            sb.append(data.getArea());//添加地区
            area = sb.toString();
        }
        //设置地区到文本控件
        tv_area.setText(getResources().getString(R.string.area_value, area));

        //描述
        tv_description.setText(getResources()
                .getString(R.string.description_value, data.getDescriptionFormat()));
    }

    /**
     * 返回显示的布局
     */
    @Override
    protected View getLayoutView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_user_detail_about, container, false);
    }

    /**
     * 创建方法
     * @param userId
     */
    public static UserDetailAboutFragment newInstance(String userId) {

        Bundle args = new Bundle();
        //传递用户id
        args.putString(Constant.ID, userId);

        UserDetailAboutFragment fragment = new UserDetailAboutFragment();
        fragment.setArguments(args);
        return fragment;
    }
}
