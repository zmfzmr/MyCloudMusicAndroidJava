package com.ixuea.courses.mymusic.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.ixuea.courses.mymusic.R;
import com.ixuea.courses.mymusic.adapter.PlayListAdapter;
import com.ixuea.courses.mymusic.manager.ListManager;
import com.ixuea.courses.mymusic.service.MusicPlayerService;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import butterknife.BindView;

/**
 * 迷你控制器 播放列表
 * <p>
 * 由于BottomSheetDialogFragment在现在这个项目中
 * 使用的比较少
 * 所以不用像BaseFragment那样封装
 * 如果使用的比较多
 * 可以进一步封装
 * <p>
 * BottomSheetDialogFragment：这个是需要和父布局配合使用的（外层是CoordinatorLayout配合使用）
 */
public class PlayListDialogFragment extends BaseBottomSheetDialogFragment {
    /**
     * 循环模式
     */
    @BindView(R.id.tv_loop_model)
    TextView tv_loop_model;

    /**
     * 音量数量
     */
    @BindView(R.id.tv_count)
    TextView tv_count;

    /**
     * 列表控件
     */
    @BindView(R.id.rv)
    RecyclerView rv;
    private PlayListAdapter adapter;//适配器
    private ListManager listManager;//列表管理器

    @Override
    protected void initViews() {
        super.initViews();

        //设置规定尺寸
        rv.setHasFixedSize(true);

        //设置布局管理器
        LinearLayoutManager layoutManager = new LinearLayoutManager(getMainActivity());
        rv.setLayoutManager(layoutManager);

        //分割线
        DividerItemDecoration decoration = new DividerItemDecoration(getMainActivity(), RecyclerView.VERTICAL);

        rv.addItemDecoration(decoration);
    }

    @Override
    protected void initDatum() {
        super.initDatum();

        //创建列表管理器
        listManager = MusicPlayerService.getListManager(getMainActivity());
        //创建适配器
        adapter = new PlayListAdapter(R.layout.item_play_list, listManager);

        //设置适配器
        rv.setAdapter(adapter);

        //设置数据
        adapter.replaceData(listManager.getDatum());
    }

    @Override
    protected void initListeners() {
        super.initListeners();
        //设置item点击事件
        adapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                //关闭dialog
                //可以根据具体的业务逻辑来决定是否关闭
                dismiss();
                //播放点击的这首音乐
                //2中方式都可以
//                listManager.play((Song) adapter.getItem(position));
                listManager.play(listManager.getDatum().get(position));
            }
        });
    }

    /**
     * 返回布局
     */
    @Override
    protected View getLayoutView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_dialog_play_list, null);
    }

    /**
     * View创建了
     */
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }

    /**
     * 构造方法
     */
    public static PlayListDialogFragment newInstance() {

        Bundle args = new Bundle();

        PlayListDialogFragment fragment = new PlayListDialogFragment();
        fragment.setArguments(args);
        return fragment;
    }

    /**
     * 显示对话框
     *
     * @param fragmentManager FragmentManager
     */
    public static void show(FragmentManager fragmentManager) {
        //创建fragment
        PlayListDialogFragment fragment = newInstance();

        //显示
        //TAG只是用来查找Fragment的
        //我们这里不需要查找
        //所以值可以随便写
        fragment.show(fragmentManager, "song_play_list_dialog");
    }
}
