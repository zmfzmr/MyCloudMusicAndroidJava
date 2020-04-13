package com.ixuea.courses.mymusic.activity;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.ixuea.courses.mymusic.R;
import com.ixuea.courses.mymusic.adapter.SelectLyricAdapter;
import com.ixuea.courses.mymusic.domain.Song;
import com.ixuea.courses.mymusic.util.LogUtil;
import com.ixuea.courses.mymusic.util.ShareUtil;
import com.ixuea.courses.mymusic.util.ToastUtil;

import org.apache.commons.lang3.StringUtils;

import java.util.ArrayList;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import butterknife.BindView;
import butterknife.OnClick;

/**
 * 选中歌词界面
 */
public class SelectLyricActivity extends BaseTitleActivity implements BaseQuickAdapter.OnItemClickListener {
    private static final String TAG = "SelectLyricActivity";
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

    /**
     * 分享歌词按钮点击
     */
    @OnClick(R.id.bt_share_lyric)
    public void onShareLyricClick() {
        LogUtil.d(TAG, "onShareLyricClick");

        //获取选中的歌词
        String lyricString = getSelectLyricString(", ");
        if (TextUtils.isEmpty(lyricString)) {
            ToastUtil.errorShortToast("请选择歌词");
            return;
        }

        LogUtil.d(TAG, "onShareLyricClick: " + lyricString);
        //data:传入进来的Song对象（也就是当前播放的音乐Song对象）
        ShareUtil.shareLyricText(getMainActivity(), data, lyricString);
    }

    /**
     * 获取选择的歌词文本
     */
    private String getSelectLyricString(String sperator) {
        //创建一个列表
        //用来装 选择字符串
        ArrayList<String> lyrics = new ArrayList<>();

        //adapter.getSelectedIndexes():获取SelectLyricAdapter适配器中选中和没选中（0和1的数组）数组
        int[] selectedIndexes = adapter.getSelectedIndexes();

        for (int i = 0; i < selectedIndexes.length; i++) {

            if (selectedIndexes[i] == 1) {
                //说明歌词是选中的

                //adapter.getItem(i):获取的是这行的Line，
                //adapter.getItem(i).getData():获取的是Line里面的data(整行歌词)
                lyrics.add(adapter.getItem(i).getData());
            }
        }
        //使用分隔符连接字符串（将集合中的数组用分隔符连接（使用工具类StringUtils.join））
        return StringUtils.join(lyrics, sperator);
    }

    /**
     * 歌词图片按钮点击了
     */
    @OnClick(R.id.bt_share_lyric_image)
    public void onShareLyricImageClick() {
        LogUtil.d(TAG, "onShareLyricImageClick");
    }
}
