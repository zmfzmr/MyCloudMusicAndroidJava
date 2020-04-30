package com.ixuea.courses.mymusic.activity;

import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import com.ixuea.courses.mymusic.R;
import com.ixuea.courses.mymusic.domain.Song;
import com.ixuea.courses.mymusic.util.LogUtil;

import java.util.List;

/**
 * 本地音乐界面
 */
public class LocalMusicActivity extends BaseTitleActivity {

    private static final String TAG = "LocalMusicActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_local_music);
    }

    @Override
    protected void initDatum() {
        super.initDatum();

        fetchData();
    }

    private void fetchData() {
        //查询本地音乐
        List<Song> datum = orm.queryLocalMusic();
        //这里可以不用datum!=null判断，因为这个queryLocalMusic查询后，就算数据为null，也会返回一个空集合对象
        //所datum不会为null
        if (datum.size() > 0) {
            //有本地音乐

            //TODO 设置到适配器
        } else {
            //没有本地音乐

            //跳转到扫描本地音乐界面
            toScanLocalMusic();
        }
    }

    /**
     * 跳转到扫描本地音乐界面
     */
    private void toScanLocalMusic() {
        LogUtil.d(TAG, "toScanLocalMusic:");

        startActivity(ScanLocalMusicActivity.class);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_local_music, menu);
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
            case R.id.action_edit:
                //TODO 批量编辑按钮点击了
                break;
            case R.id.action_scan_local_music:
                //扫描本地音乐(跳转到扫描界面就行了)
                toScanLocalMusic();
                break;
            case R.id.action_sort:
                //TODO 排序
                break;
            default:
                break;
        }
        return super.onOptionsItemSelected(item);
    }
}
