package com.ixuea.courses.mymusic.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.ixuea.courses.mymusic.R;
import com.ixuea.courses.mymusic.domain.Song;
import com.ixuea.courses.mymusic.util.Constant;
import com.ixuea.courses.mymusic.util.ImageUtil;
import com.ixuea.courses.mymusic.util.LogUtil;

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
                break;
        }
        return super.onOptionsItemSelected(item);
    }
}
