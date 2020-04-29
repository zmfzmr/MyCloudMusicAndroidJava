package com.ixuea.courses.mymusic.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.ixuea.courses.mymusic.R;
import com.ixuea.courses.mymusic.adapter.SheetAdapter;
import com.ixuea.courses.mymusic.domain.Sheet;
import com.ixuea.courses.mymusic.domain.event.OnSelectSheetEvent;

import org.greenrobot.eventbus.EventBus;

import java.util.List;

import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import butterknife.BindView;

/**
 * 选择歌单对话框
 */
public class SelectSheetDialogFragment extends BaseBottomSheetDialogFragment {
    /**
     * 列表控件
     */
    @BindView(R.id.rv)
    RecyclerView rv;
    private List<Sheet> datum;//数据(里面是Sheet歌单对象)
    private SheetAdapter adapter;

    @Override
    protected void initViews() {
        super.initViews();


        //尺寸固定
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

        //创建适配器
        adapter = new SheetAdapter(R.layout.item_topic);
        //设置适配器
        rv.setAdapter(adapter);

        //设置数据(这个数据是：Activity那边网络请求传递过来的)
        adapter.replaceData(datum);
    }

    @Override
    protected void initListeners() {
        super.initListeners();

        //item点击事件
        adapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                //获取点击的事件
                Sheet data = (Sheet) adapter.getItem(position);

                //发布事件(MeFragment那边接收它)
                EventBus.getDefault().post(new OnSelectSheetEvent(data));

                //关闭对话框(因为当前Fragment就是一个dialog，所以可以直接调用dismiss())
                dismiss();
            }
        });
    }

    @Override
    protected View getLayoutView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.dialog_select_sheet, container, false);
    }

    /**
     * 选择歌单对话框
     */
    public static void show(FragmentManager fragmentManager, List<Sheet> datum) {
        //创建歌单fragment
        SelectSheetDialogFragment fragment = newInstance();

        //设置数据
        //(Fragment推荐用bundle，但是这里是list集合数据，不太好处理，所以领建立一个方法传递数据)
        fragment.setDatum(datum);

        //显示
        //注意：是通过本类对象来调动；因为是在static，前缀要使用对象的引用来调用
        fragment.show(fragmentManager, "SelectSheetDialogFragment");
    }

    /**
     * 创建方法
     */
    public static SelectSheetDialogFragment newInstance() {

        Bundle args = new Bundle();

        SelectSheetDialogFragment fragment = new SelectSheetDialogFragment();
        fragment.setArguments(args);
        return fragment;
    }

    public void setDatum(List<Sheet> datum) {
        this.datum = datum;
    }

    public List<Sheet> getDatum() {
        return datum;
    }
}
