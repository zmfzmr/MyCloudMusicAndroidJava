package com.ixuea.courses.mymusicold.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import butterknife.BindView;

import android.os.Bundle;


import com.ixuea.courses.mymusicold.R;

/**
 * 登录界面
 */
public class LoginActivity extends BaseCommonActivity {

    /**
     * 标题控件
     */
    @BindView(R.id.toolbar)
    Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
    }

    @Override
    protected void initView() {
        super.initView();

        //初始化Toolbar
        setSupportActionBar(toolbar);
    }

//    @Override
//    public void setTitle(int titleId) {//这个方法可以不用写，因为已经继承了
//        super.setTitle(titleId);
//    }
}
