package com.ixuea.courses.mymusic.activity;

import android.os.Bundle;
import android.view.View;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.ixuea.courses.mymusic.R;
import com.ixuea.courses.mymusic.adapter.SelectLyricAdapter;
import com.ixuea.courses.mymusic.domain.Song;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import butterknife.BindView;

/**
 * 选中歌词界面
 */
public class SelectLyricActivity extends BaseTitleActivity implements BaseQuickAdapter.OnItemClickListener {
    /**
     * 列表控件
     */
    @BindView(R.id.rv)
    RecyclerView rv;
    /**
     * 音乐对象
     */
    private Song data;
    private SelectLyricAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_lyric);
    }

    @Override
    protected void initView() {
        super.initView();
        //尺寸固定
        rv.setHasFixedSize(true);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getMainActivity());
        rv.setLayoutManager(layoutManager);
    }

    @Override
    protected void initDatum() {
        super.initDatum();

        //获取传递的数据
        data = (Song) extraData();

        //创建适配器
        adapter = new SelectLyricAdapter(R.layout.item_select_lyric);
        //设置适配器
        rv.setAdapter(adapter);

        //设置数据  data.getParsedLyric().getDatum():获取所有歌词List<Line>
        adapter.replaceData(data.getParsedLyric().getDatum());
    }

    @Override
    protected void initListeners() {
        super.initListeners();

        //设置Item点击事件
        adapter.setOnItemClickListener(this);

    }

    /**
     * 歌词item点击回调
     */
    @Override
    public void onItemClick(BaseQuickAdapter a, View view, int position) {
        //这个position是从参数里面来的
//        if (adapter.isSelected(position)) {
//            //是选中的  设置为不选中状态
//
//            adapter.setSelected(position,false);
//        } else {
//            adapter.setSelected(position,true);
//        }

        adapter.setSelected(position, !adapter.isSelected(position));
    }
}
