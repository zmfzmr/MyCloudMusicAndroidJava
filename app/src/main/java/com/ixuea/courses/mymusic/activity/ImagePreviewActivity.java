package com.ixuea.courses.mymusic.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

import com.ixuea.courses.mymusic.R;
import com.ixuea.courses.mymusic.util.Constant;

import androidx.appcompat.app.AppCompatActivity;

/**
 * 图片预览界面
 * 可以把该界面实现为项目中通用的图片预览界面
 */
public class ImagePreviewActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_image_preview);
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
