package com.ixuea.courses.mymusic.activity;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.ixuea.courses.mymusic.R;
import com.ixuea.courses.mymusic.domain.Song;
import com.ixuea.courses.mymusic.util.BitmapUtil;
import com.ixuea.courses.mymusic.util.Constant;
import com.ixuea.courses.mymusic.util.ImageUtil;
import com.ixuea.courses.mymusic.util.LogUtil;
import com.ixuea.courses.mymusic.util.ShareUtil;
import com.ixuea.courses.mymusic.util.StorageUtil;
import com.ixuea.courses.mymusic.util.ToastUtil;
import com.ixuea.courses.mymusic.util.ViewUtil;

import java.io.File;

import butterknife.BindView;

/**
 * 分享歌词图片界面
 */
public class ShareLyricImageActivity extends BaseTitleActivity {

    private static final String TAG = "ShareLyricImageActivity";
    /**
     * 歌词容器
     */
    @BindView(R.id.lyric_container)
    View lyric_container;

    /**
     * 封面
     */
    @BindView(R.id.iv_banner)
    ImageView iv_banner;

    /**
     * 歌词
     */
    @BindView(R.id.tv_lyric)
    TextView tv_lyric;

    /**
     * 歌曲信息（歌词右下角:歌手和歌曲名称）
     */
    @BindView(R.id.tv_song)
    TextView tv_song;
    private Song data;
    private String lyric;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_share_lyric_image);
    }

    /**
     * 启动方法
     */
    public static void start(Activity activity, Song data, String lyric) {
        //创建intent
        Intent intent = new Intent(activity, ShareLyricImageActivity.class);
        //传递音乐
        intent.putExtra(Constant.DATA, data);
        //传递歌词
        intent.putExtra(Constant.ID, lyric);
        //启动界面
        activity.startActivity(intent);
    }

    @Override
    protected void initDatum() {
        super.initDatum();
        //获取歌曲
        data = (Song) extraData();

        //获取歌词(我们在本类的start 告知外界用的就是id传递过来的，所以这类用extraId获取)
        lyric = extraId();

        //显示封面
        ImageUtil.show(getMainActivity(), iv_banner, data.getBanner());

        //显示歌词
        tv_lyric.setText(lyric);
        //设置歌曲信息 参数:1.歌手 2：歌曲名称
        tv_song.setText(getResources().getString(R.string.share_song_name,
                data.getSinger().getNickname(),
                data.getTitle()));
    }

    /**
     * 创建菜单方法
     * <p>
     * 有点类似显示布局要写到onCreate方法一样
     */
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        //加载布局文件
        getMenuInflater().inflate(R.menu.menu_share_lyric_image, menu);
        return super.onCreateOptionsMenu(menu);
    }

    /**
     * 菜单点击了回调
     */
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_share:
                //分享点击了
                LogUtil.d(TAG, "share click");

                onShareClick();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    /**
     * 分享按钮点击了
     */
    private void onShareClick() {
        //从View生成一张图片
        //Bitmap就是常说的位图
        //lyric_container: 就是ScrollView这个滚动容器 改：不是ScrollView，是里面的子view LinearLayout容器
        //实际上截图的是里面的那个子View LinearLayout
        Bitmap bitmap = ViewUtil.captureBitmap(lyric_container);

        LogUtil.d(TAG, "onShareClick: " + bitmap);

        //先保存到自己应用的私有目录(外部存储(SD卡私有目录))
        //例如：/storage/emulated/0/Android/data/com.ixuea.courses.mymusicold/files/Download/MyCloudMusic/1/jpg/share.jpg
        //Android10.0以下，是以下的写法，但是10.0后就不能这么写了
        //File targetFile = new File(Environment.getExternalStorageDirectory().getAbsolutePath().concat("/MyCloudMusic"),"share.jpg");

        //参数2；用户id（区分多用户，我们这里是下载的时候用到）3.图片名称
        File file = StorageUtil.getExternalPath(getMainActivity(), sp.getUserId(), "share", StorageUtil.JPG);

        //保存图片到文件（也就是保存Bitmap到文件）
        BitmapUtil.saveToFile(bitmap, file);

        LogUtil.d(TAG, "2:" + file.getAbsolutePath());

        //将私有路径的图片保存到相册
        //这样其他应用才能访问
        //savePicture:这个方法是前面实现（固定写法） file：SD卡（外部存储）私有目录file对象(通过对象可以获取路径)
        Uri uri = StorageUtil.savePicture(getMainActivity(), file);

        if (uri != null) {
            //获取uri路径（也就是保存到本地相册的那个路径，是个绝对路径）
            // /storage/emulated/0/Pictures/share.jpg 等于/sdcard/Pictures/share.jpg
            String path = StorageUtil.getMediaStorePath(getMainActivity(), uri);

            LogUtil.d(TAG, "onShareClick save bitmap to file success:" + path);

            //分享图片
            ShareUtil.shareImage(getMainActivity(), path);
        } else {
            ToastUtil.errorShortToast(R.string.error_share_failed);
        }

    }
}
