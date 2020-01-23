package com.ixuea.courses.mymusic.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.webkit.WebSettings;
import android.webkit.WebView;

import com.ixuea.courses.mymusic.R;
import com.ixuea.courses.mymusic.util.Constant;

import butterknife.BindView;

/**
 * 通用WebView界面
 */
public class WebViewActivity extends BaseTitleActivity {
    /**
     * WebView控件
     */
    @BindView(R.id.wv)
    WebView wv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_web_view);
    }

    @Override
    protected void initView() {
        super.initView();

        //获取webview设置
        WebSettings webSettings = wv.getSettings();

        //支持多窗口
        webSettings.setSupportMultipleWindows(true);

        //显示图片(Block:禁止  2个false表示true)
        webSettings.setBlockNetworkImage(false);

        //允许显示手机中的网页
        webSettings.setAllowUniversalAccessFromFileURLs(true);

        //开启js支持
        webSettings.setJavaScriptEnabled(true);

        //允许访问文件
        webSettings.setAllowFileAccess(true);

        //允许文件访问 （这个跟上面那边有点区别）
        webSettings.setAllowFileAccessFromFileURLs(true);

        //允许dmo存储
        webSettings.setDomStorageEnabled(true);

        //int LOLLIPOP = 21
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            //允许混合类型
            //也就说支持网页中有http/https(也即是说版本大于21 可能不支持混合类型，这里设置支持)
            webSettings.setMixedContentMode(WebSettings.MIXED_CONTENT_ALWAYS_ALLOW);
        }

        //记忆：前（多窗口）中：（多窗口）后：允许访问文件


    }

    @Override
    protected void initDatum() {
        super.initDatum();

        //获取传递的数据
        String title = extraString(Constant.TITLE);
        String url = extraString(Constant.URL);

        //设置标题
        setTitle(title);

        //加载网页
        wv.loadUrl(url);
    }


    /**
     * 定义静态的启动方法
     * 好处是用户只要看到声明
     * 就知道该界面需要哪些参数
     *
     * @param activity Activity
     * @param title    标题
     * @param url      Url地址
     */
    public static void start(Activity activity, String title, String url) {
        //创建Intent
        Intent intent = new Intent(activity, WebViewActivity.class);

        //添加标题
        intent.putExtra(Constant.TITLE, title);

        intent.putExtra(Constant.URL, url);

        //启动界面
        activity.startActivity(intent);

    }
}
