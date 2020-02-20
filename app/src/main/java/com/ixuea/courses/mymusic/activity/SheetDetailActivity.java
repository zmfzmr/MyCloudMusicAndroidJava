package com.ixuea.courses.mymusic.activity;

import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.ixuea.courses.mymusic.R;
import com.ixuea.courses.mymusic.adapter.SongAdapter;
import com.ixuea.courses.mymusic.api.Api;
import com.ixuea.courses.mymusic.domain.Sheet;
import com.ixuea.courses.mymusic.domain.response.DetailResponse;
import com.ixuea.courses.mymusic.listener.HttpObserver;
import com.ixuea.courses.mymusic.util.Constant;
import com.ixuea.courses.mymusic.util.ImageUtil;
import com.ixuea.courses.mymusic.util.LogUtil;

import org.apache.commons.lang3.StringUtils;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import butterknife.BindView;

/**
 * 歌单详情界面
 */
public class SheetDetailActivity extends BaseTitleActivity {

    private static final String TAG = "SheetDetailActivity";

    /**
     * 列表控件
     */
    @BindView(R.id.rv)
    RecyclerView rv;

    /**
     * 歌单Id
     */
    private String id;
    private Sheet data;//歌单对象数据
    private SongAdapter adapter;
    /**
     * 头部容器
     */
    private LinearLayout ll_header;//这里也可以命名为LinearLayout，也可以不命名
    private ImageView iv_banner;//封面图
    private TextView tv_title;//歌单详情头部标题（也就是歌单的名称）
    private ImageView iv_avatar;//头像
    private TextView tv_nickname;//头像
    private View ll_comment_container;//评论容器
    private TextView tv_comment_count;//评论数
    private Button bt_collection;//收藏按钮
    private View ll_play_all_container;//播放全部容器
    private TextView tv_count;//歌曲数

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sheet_detail);
    }

    @Override
    protected void initView() {
        super.initView();

        //尺寸固定
        rv.setHasFixedSize(true);

        //设置布局管理器(这里是线性布局管理器)
        LinearLayoutManager layoutManager = new LinearLayoutManager(getMainActivity());
        //设置管理器
        rv.setLayoutManager(layoutManager);

//        //创建分割线
//        DividerItemDecoration decoration = new DividerItemDecoration(getMainActivity(),RecyclerView.VERTICAL);
//
//        //添加到控件
//        //可以添加多个
//        rv.addItemDecoration(decoration);
    }

    /**
     * 创建头部
     */
    private View createHeaderView() {
        //从XML创建布局
        View view = getLayoutInflater().inflate(R.layout.header_sheet_detail, (ViewGroup) rv.getParent(), false);

        //头部容器
        ll_header = view.findViewById(R.id.ll_header);

        //封面图
        iv_banner = view.findViewById(R.id.iv_banner);

        //标题
        tv_title = view.findViewById(R.id.tv_title);

        //头像
        iv_avatar = view.findViewById(R.id.iv_avatar);

        //昵称
        tv_nickname = view.findViewById(R.id.tv_nickname);

        //评论容器
        ll_comment_container = view.findViewById(R.id.ll_comment_container);

        //评论数据
        tv_comment_count = view.findViewById(R.id.tv_comment_count);

        //收藏按钮
        bt_collection = view.findViewById(R.id.bt_collection);

        //播放全部容器
        ll_play_all_container = view.findViewById(R.id.ll_play_all_container);

        //歌曲数
        tv_count = view.findViewById(R.id.tv_count);

        //返回view
        return view;
    }

    @Override
    protected void initDatum() {
        super.initDatum();

        //获取传递的参数
        getIntent().getStringExtra(Constant.ID);

        //使用重构后的方法
        id = extraId();

        //创建适配器
        adapter = new SongAdapter(R.layout.item_sheet_detail);

        //添加头部（注意：在设置适配器前添加头部布局）
        adapter.addHeaderView(createHeaderView());

        //设置适配器
        rv.setAdapter(adapter);
        //这里RecyclerView在前面，请求网络数据在后

        //请求数据
        fetchData();
    }

    /**
     * //请求数据
     */
    private void fetchData() {
        Api.getInstance()
                .sheetDetail(id)
                .subscribe(new HttpObserver<DetailResponse<Sheet>>() {
                    @Override
                    public void onSucceeded(DetailResponse<Sheet> data) {
                        next(data.getData());
                    }
                });
    }

    /**
     * 显示数据
     *
     * @param data Sheet数据
     */
    private void next(Sheet data) {
        this.data = data;

        LogUtil.d(TAG, "next:" + data);
        if (data.getSongs() != null && data.getSongs().size() > 0) {
            //有音乐才设置
            //设置数据
            adapter.replaceData(data.getSongs());
        }

        //显示封面
        if (StringUtils.isBlank(data.getBanner())) {
            //如果图片为空

            //就用默认图片
            iv_banner.setImageResource(R.drawable.placeholder);
        } else {
            //有图片
            ImageUtil.show(getMainActivity(), iv_banner, data.getBanner());
        }

        //显示标题
        tv_title.setText(data.getTitle());

        //头像
        ImageUtil.showAvatar(getMainActivity(), iv_avatar, data.getUser().getAvatar());

        //昵称
        tv_nickname.setText(data.getUser().getNickname());

        //评论数
        tv_comment_count.setText(String.valueOf(data.getComments_count()));

        //音乐数
        tv_count.setText(getResources().getString(R.string.music_count, data.getSongs().size()));
    }
}