package com.ixuea.courses.mymusic.activity;

import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import com.ixuea.courses.mymusic.R;
import com.ixuea.courses.mymusic.adapter.SongAdapter;
import com.ixuea.courses.mymusic.domain.Song;
import com.ixuea.courses.mymusic.util.LogUtil;

import java.util.List;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import butterknife.BindView;

/**
 * 本地音乐界面
 */
public class LocalMusicActivity extends BaseTitleActivity {

    private static final String TAG = "LocalMusicActivity";
    /**
     * 列表控件
     */
    @BindView(R.id.rv)
    RecyclerView rv;
    private SongAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_local_music);
    }

    @Override
    protected void initView() {
        super.initView();
        //设置尺寸固定
        rv.setHasFixedSize(true);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getMainActivity());
        rv.setLayoutManager(layoutManager);
    }

    @Override
    protected void initDatum() {
        super.initDatum();
        //复用歌单详情里面的单曲适配器
        //不过这样写有个问题（就是前面的位置tv_position，会从0开始）
        //复用的适配器SongAdapter（在歌单详情那边是有header的）
        //如果想处理这个问题的，需要获取RecyclerView的header判断处理
        //还有右边的更多按钮 并没有实现，我们这里就不处理了
        adapter = new SongAdapter(R.layout.item_sheet_detail);
        rv.setAdapter(adapter);

//        adapter.addHeaderView(LayoutInflater.from(getMainActivity()).inflate(R.layout.item_song,null,false));
//        adapter.addHeaderView(LayoutInflater.from(getMainActivity()).inflate(R.layout.item_song,null,false));
//        adapter.getHeaderLayout().getChildCount();//获取头布局的数量

        fetchData();
    }

    private void fetchData() {
        //查询本地音乐
        List<Song> datum = orm.queryLocalMusic();
        //这里可以不用datum!=null判断，因为这个queryLocalMusic查询后，就算数据为null，也会返回一个空集合对象
        //所datum不会为null
        if (datum.size() > 0) {
            //有本地音乐

            //设置到适配器
            adapter.replaceData(datum);
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
