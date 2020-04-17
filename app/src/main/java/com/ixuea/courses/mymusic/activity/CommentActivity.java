package com.ixuea.courses.mymusic.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.EditText;

import com.ixuea.courses.mymusic.R;
import com.ixuea.courses.mymusic.api.Api;
import com.ixuea.courses.mymusic.domain.Comment;
import com.ixuea.courses.mymusic.domain.response.ListResponse;
import com.ixuea.courses.mymusic.listener.HttpObserver;
import com.ixuea.courses.mymusic.util.Constant;
import com.ixuea.courses.mymusic.util.LogUtil;

import java.util.HashMap;

import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import butterknife.BindView;
import butterknife.OnClick;

/**
 * 评论界面
 */
public class CommentActivity extends BaseTitleActivity {

    private static final String TAG = "CommentActivity";
    /**
     * 列表控件
     */
    @BindView(R.id.rv)
    RecyclerView rv;

    /**
     * 输入框
     */
    @BindView(R.id.et_content)
    EditText et_content;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_comment);
        //请求数据
        fetchData();
    }

    @Override
    protected void initDatum() {
        super.initDatum();

        //固定高度
        rv.setHasFixedSize(true);

        //设置布局管理器
        LinearLayoutManager layoutManager = new LinearLayoutManager(getMainActivity());
        rv.setLayoutManager(layoutManager);

        //设置分割线
        DividerItemDecoration decoration = new DividerItemDecoration(getMainActivity(), DividerItemDecoration.HORIZONTAL);
        rv.addItemDecoration(decoration);
    }

    /**
     * 请求数据
     */
    private void fetchData() {
        //查询参数
        HashMap<String, String> query = new HashMap<>();
        //请求最新评论列表
        Api.getInstance()
                .comments(query)
                .subscribe(new HttpObserver<ListResponse<Comment>>() {
                    @Override
                    public void onSucceeded(ListResponse<Comment> data) {
                        LogUtil.d(TAG, "data size:" + data.getData().size());
                    }
                });
    }

    /**
     * 发送按钮点击回调
     */
    @OnClick(R.id.bt_send)
    public void onSendClick() {
        LogUtil.d(TAG, "onSendClick");
    }
    /**
     * 启动评论界面
     * 重构为方法的好初始化
     * 不查看代码就知道要传递哪些参数
     *
     * @param activity 界面
     * @param sheetId  歌单Id
     */
    public static void start(Activity activity, String sheetId) {
        //创建意图
        //意图：就是你要干什么
        Intent intent = new Intent(activity, CommentActivity.class);
        //传递歌单id
        intent.putExtra(Constant.SHEET_ID, sheetId);
        //启动界面
        activity.startActivity(intent);
    }
}
