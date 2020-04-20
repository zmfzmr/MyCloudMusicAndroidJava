package com.ixuea.courses.mymusic.activity;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.widget.EditText;

import com.github.jdsjlzx.recyclerview.LRecyclerViewAdapter;
import com.ixuea.courses.mymusic.R;
import com.ixuea.courses.mymusic.adapter.BaseRecyclerViewAdapter;
import com.ixuea.courses.mymusic.adapter.CommentAdapter;
import com.ixuea.courses.mymusic.api.Api;
import com.ixuea.courses.mymusic.domain.Comment;
import com.ixuea.courses.mymusic.domain.response.DetailResponse;
import com.ixuea.courses.mymusic.domain.response.ListResponse;
import com.ixuea.courses.mymusic.fragment.CommentMoreDialogFragment;
import com.ixuea.courses.mymusic.listener.HttpObserver;
import com.ixuea.courses.mymusic.listener.OnItemClickListener;
import com.ixuea.courses.mymusic.util.ClipBoardUtil;
import com.ixuea.courses.mymusic.util.Constant;
import com.ixuea.courses.mymusic.util.KeyBoardUtil;
import com.ixuea.courses.mymusic.util.LogUtil;
import com.ixuea.courses.mymusic.util.ToastUtil;

import org.apache.commons.lang3.StringUtils;

import java.util.HashMap;

import androidx.annotation.NonNull;
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
     * 布局中用的是LRecyclerView extends RecyclerView
     * RecyclerView：父类
     */
    @BindView(R.id.rv)
    RecyclerView rv;

    /**
     * 输入框
     */
    @BindView(R.id.et_content)
    EditText et_content;
    private CommentAdapter adapter;
    private LRecyclerViewAdapter adapterWrapper;
    private String parentId;//设置被恢复评论的id
    private String sheetId;//传递过来的歌单id


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_comment);
//        //请求数据
//        fetchData();//这个应该放到initDatum中
    }

    @Override
    protected void initView() {
        super.initView();

        //固定高度
        rv.setHasFixedSize(true);

        //设置布局管理器
        LinearLayoutManager layoutManager = new LinearLayoutManager(getMainActivity());
        rv.setLayoutManager(layoutManager);

        //设置分割线
        DividerItemDecoration decoration = new DividerItemDecoration(getMainActivity(), DividerItemDecoration.VERTICAL);
        rv.addItemDecoration(decoration);
    }

    @Override
    protected void initDatum() {
        super.initDatum();

        //获取传递的数据（这里是歌单id）
        sheetId = extraString(Constant.SHEET_ID);

        adapter = new CommentAdapter(getMainActivity());

        //创建包裹适配器
        //对应LRecyclerView框架来说
        //就是这种写法
        adapterWrapper = new LRecyclerViewAdapter(adapter);

        //设置适配器
        rv.setAdapter(adapterWrapper);

        //请求数据
        fetchData();

    }

    @Override
    protected void initListeners() {
        super.initListeners();

        //设置item点击事件
        adapter.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(BaseRecyclerViewAdapter.ViewHolder holder, int position) {
                LogUtil.d(TAG, "onItemClick: " + position);
                Object data = adapter.getDatum().get(position);
                if (data instanceof Comment) {
                    showCommentMoreDialogFragment((Comment) data);
                }

            }
        });

        //添加列表滚动监听器
        rv.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                if (Math.abs(dy) > 100) {
                    //y轴滚动方向大于100

                    if (StringUtils.isEmpty(et_content.getText().toString())) {
                        //并且还没有输入内容
                        //才清除回复
                        clearInputContent();
                    }
                }

            }
        });
    }

    /**
     * 显示评论更多对话框fragment
     *
     * @param data Comment
     */
    private void showCommentMoreDialogFragment(Comment data) {
        //创建CommentMoreDialogFragment对象
        CommentMoreDialogFragment dialogFragment = CommentMoreDialogFragment.newInstance();

        dialogFragment.show(getSupportFragmentManager(), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();//关闭对话框

                //which：点击的索引(弹出框里面的item索引) data：点击的item的对象
                processOnClick(which, data);
            }
        });

    }

    /**
     * 处理评论更多对话框点击事件
     *
     * @param which 点击的索引
     * @param data  item对象
     */
    private void processOnClick(int which, Comment data) {

        LogUtil.d(TAG, "which " + data.getContent());
        switch (which) {
            case 0:
                //回复评论
                parentId = data.getId();//该条评论的id
                //比如 ： 回复爱学啊
                et_content.setHint(getResources().getString(R.string.reply_hint, data.getUser().getNickname()));
                break;
            case 1:
                //TODO 分享评论
                break;
            case 2:
                //复制评论
                //参数2：data.getContent()：评论的内容
                ClipBoardUtil.copyText(getMainActivity(), data.getContent());

                ToastUtil.successShortToast(R.string.copy_success);
                break;
            case 3:
                //TODO 举报评论
                break;
        }
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
                        //设置数据
                        adapter.setDatum(data.getData());
                    }
                });
    }

    /**
     * 发送按钮点击回调
     */
    @OnClick(R.id.bt_send)
    public void onSendClick() {
        LogUtil.d(TAG, "onSendClick");
        //思路：因为这个Comment中 有这个content 字段，所以直接拿这个对象里面的content来发送请求，创建评论
        //@Body：传送的过程中，retrofit会转换成json对象
        sendComment();
    }

    private void sendComment() {
        //获取输入的文本
        String content = et_content.getText().toString().trim();

        if (StringUtils.isEmpty(content)) {
            ToastUtil.errorShortToast(R.string.enter_content);
            return;
        }

        //设置评论对象
        Comment data = new Comment();

        //设置被恢复评论的id(这个parentId：是点击弹出对话框中的 标题才会有值的)
        //这个id如果有值的话，那么就是回复别人的评论
        data.setParent_id(parentId);

        //设置内容
        data.setContent(content);

        //设置歌单id
        data.setSheet_id(sheetId);
        //调用评论接口
        Api.getInstance()
                .createComment(data)
                .subscribe(new HttpObserver<DetailResponse<Comment>>() {
                    @Override
                    public void onSucceeded(DetailResponse<Comment> data) {
                        //发布成功了
                        ToastUtil.successShortToast(R.string.comment_create_success);

                        //刷新数据(发布评论成功了，重新请求显示评论列表数据)
                        fetchData();

                        //清空输入框
                        clearInputContent();

                        //关闭键盘
                        KeyBoardUtil.hideKeyBoard(getMainActivity());
                    }
                });


    }

    /**
     * 清空输入框
     */
    private void clearInputContent() {
        parentId = null;
        et_content.setText("");
        et_content.setHint(R.string.hint_comment);//发布了，重新设置提示
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
