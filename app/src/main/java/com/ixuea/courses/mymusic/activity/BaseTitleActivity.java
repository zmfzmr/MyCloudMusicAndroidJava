package com.ixuea.courses.mymusic.activity;

import android.view.MenuItem;

import com.ixuea.courses.mymusic.R;

import androidx.appcompat.widget.Toolbar;
import butterknife.BindView;

/**
 * 通用标题界面
 */
public class BaseTitleActivity extends BaseCommonActivity {
    /**
     * 标题控件
     */
    @BindView(R.id.toolbar)
    Toolbar toolbar;


    @Override
    protected void initView() {
        super.initView();

        //初始化Toolbar
        setSupportActionBar(toolbar);

        //是否显示返回按钮
        if (isShowBackMenu()) {
            showBackMenu();
        }
    }

    /**
     * 显示返回按钮
     */
    protected void showBackMenu() {
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    /**
     * 是否显示返回按钮
     * @return
     */
    protected boolean isShowBackMenu() {
        return true;
    }

    /**
     * 按钮点击回调事件
     *
     * @param item
     * @return
     */
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                //Toolbar返回按钮点击
                finish();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    //    @Override
//    public void setTitle(int titleId) {//这个方法可以不用写，因为已经继承了
//        super.setTitle(titleId);
//    }
}
