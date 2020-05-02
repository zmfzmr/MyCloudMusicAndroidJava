package com.ixuea.courses.mymusic.activity;

import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.ixuea.courses.mymusic.MusicPlayerActivity;
import com.ixuea.courses.mymusic.R;
import com.ixuea.courses.mymusic.adapter.SongAdapter;
import com.ixuea.courses.mymusic.domain.Song;
import com.ixuea.courses.mymusic.fragment.SortDialogFragment;
import com.ixuea.courses.mymusic.manager.ListManager;
import com.ixuea.courses.mymusic.service.MusicPlayerService;
import com.ixuea.courses.mymusic.util.LogUtil;
import com.ixuea.courses.mymusic.util.ToastUtil;

import java.util.List;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import butterknife.BindView;
import butterknife.OnClick;

/**
 * 本地音乐界面
 */
public class LocalMusicActivity extends BaseTitleActivity implements BaseQuickAdapter.OnItemClickListener {

    private static final String TAG = "LocalMusicActivity";
    /**
     * 列表控件
     */
    @BindView(R.id.rv)
    RecyclerView rv;

    /**
     * 按钮容器
     */
    @BindView(R.id.ll_button_container)
    View ll_button_container;

    /**
     * 选择按钮(全选)
     */
    @BindView(R.id.bt_select)
    Button bt_select;

    /**
     * 删除按钮
     */
    @BindView(R.id.bt_delete)
    Button bt_delete;

    private SongAdapter adapter;
    private ListManager listManager;//列表管理器
    private MenuItem editMenuItem;//编辑菜单

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
        //初始化播放列表管理器
        listManager = MusicPlayerService.getListManager(getApplicationContext());

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
        List<Song> datum = orm.queryLocalMusic(sp.getLocalMusicSortIndex());
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

    @Override
    protected void initListeners() {
        super.initListeners();

        //添加item点击事件
        adapter.setOnItemClickListener(this);
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
        //查找编辑菜单
        editMenuItem = menu.findItem(R.id.action_edit);
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
                //批量编辑按钮点击了
                onEditClick();
                break;
            case R.id.action_scan_local_music:
                //扫描本地音乐(跳转到扫描界面就行了)
                toScanLocalMusic();
                break;
            case R.id.action_sort:
                //排序
                showSortDialog();
                break;
            default:
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    /**
     * 批量编辑按钮 点击了
     */
    private void onEditClick() {
        //判断是否有数据(有本地音乐才去编辑)
        if (adapter.getItemCount() == 0) {
            ToastUtil.successShortToast("没有本地音乐!");
            return;
        }

        if (adapter.isEditing()) {
            //在编辑模式下

            //退出编辑模式
            exitEditMode();
        } else {
            //没有进入编辑模式

            //进入编辑模式

            //设置编辑按钮标题
            editMenuItem.setTitle(R.string.cancel_edit);
            //显示编辑容器（全选和删除按钮）
            ll_button_container.setVisibility(View.VISIBLE);
            //适配器进入编辑状态
            adapter.setEditing(true);
        }
    }

    /**
     * 退出编辑
     */
    private void exitEditMode() {
        //退出编辑（取消编辑）恢复原来的文字 （批量编辑）
        editMenuItem.setTitle(R.string.batch_edit);
        //编辑容器设置隐藏
        ll_button_container.setVisibility(View.GONE);
        //重置编辑按钮状态
        defaultButtonStatus();//设置成一个方法，后面用到
        //退出编辑设置编辑状态为false
        adapter.setEditing(false);
    }

    /**
     * 重置编辑按钮状态
     */
    private void defaultButtonStatus() {
        //设置全选文字
        bt_select.setText(R.string.select_all);
        //禁用删除按钮
        bt_delete.setEnabled(false);
    }

    /**
     * 显示排序对话框
     */
    private void showSortDialog() {
        SortDialogFragment.show(getSupportFragmentManager(), sp.getLocalMusicSortIndex(), (dialog, which) -> {
            LogUtil.d(TAG, "onClick: " + which);
            //关闭对话框
            dialog.dismiss();

            //保存排序索引
            sp.setLocalMusicSortIndex(which);

            //点击重新下数据
            fetchData();
        });
    }

    /**
     * item点击回调
     *
     * @param adapter
     * @param view
     * @param position
     */
    @Override
    public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
        //获取当前点击音乐
        Song data = (Song) adapter.getItem(position);
        //adapter.getData():获取到的是List<Song> 因为前面从本地数据查询回来会转换成List<Song>
        listManager.setDatum(adapter.getData());
        //播放当前音乐
        listManager.play(data);
        //跳转到播放界面
        startActivity(MusicPlayerActivity.class);
    }

    /**
     * 选择按钮点击事件
     */
    @OnClick(R.id.bt_select)
    public void onSelectAllClick() {
        LogUtil.d(TAG, "onSelectAllClick");
    }

    /**
     * 删除点击
     */
    @OnClick(R.id.bt_delete)
    public void onDeleteClick() {
        LogUtil.d(TAG, "onDeleteClick");
    }

}
