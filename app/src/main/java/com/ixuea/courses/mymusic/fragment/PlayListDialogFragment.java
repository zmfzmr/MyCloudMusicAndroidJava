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
import com.ixuea.courses.mymusic.util.EventBusUtil;
import com.ixuea.courses.mymusic.util.PlayListUtil;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import butterknife.BindView;
import butterknife.OnClick;

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

        //显示循环模式
        PlayListUtil.showLoopModel(listManager, tv_loop_model);
        //显示音乐数据
        //真实项目中建议字符串都放到strings.xml文件中
        //因为这样更方便复用，汉化
        //这里只是给大家演示
        //也可以直接这样写
        tv_count.setText(String.format("(%d)", listManager.getDatum().size()));

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

        //item子控件点击事件 注意：这里是Child（也就是item里面的子控件点击事件）
        adapter.setOnItemChildClickListener(new BaseQuickAdapter.OnItemChildClickListener() {
            @Override
            public void onItemChildClick(BaseQuickAdapter adapter, View view, int position) {
                //关闭弹窗
                dismiss();
                //由于这里只有一个按钮点击
                //所以可以不判断
                if (R.id.iv_remove == view.getId()) {
                    //删除按钮点击

                    //从列表管理器删除
                    listManager.delete(position);

                    //步骤1.发送列表改变了通知（接下来，需要在某个类接收这个通知）
                    //2：这里是在BaseMusicPlayerActivity中注册（接收通知）
                    //3:BaseMusicPlayerActivity改变了事件onPlayListChangedEvent方法（接收通知后做的事情的方法）
                    EventBusUtil.postPlayListChangeEvent();
                }
            }
        });
    }

    /**
     * 删除所有按钮点击
     */
    @OnClick(R.id.iv_delete_all)
    public void onDeleteAll() {

        //关闭对话框
        dismiss();
        //删除列表全部音乐
        listManager.deleteAll();

        //发送音乐列表改变通知
        EventBusUtil.postPlayListChangeEvent();
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
