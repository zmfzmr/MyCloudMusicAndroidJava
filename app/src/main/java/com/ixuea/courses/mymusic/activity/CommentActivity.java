package com.ixuea.courses.mymusic.activity;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.EditText;

import com.github.jdsjlzx.interfaces.OnLoadMoreListener;
import com.github.jdsjlzx.interfaces.OnRefreshListener;
import com.github.jdsjlzx.recyclerview.LRecyclerView;
import com.github.jdsjlzx.recyclerview.LRecyclerViewAdapter;
import com.ixuea.courses.mymusic.R;
import com.ixuea.courses.mymusic.adapter.BaseRecyclerViewAdapter;
import com.ixuea.courses.mymusic.adapter.CommentAdapter;
import com.ixuea.courses.mymusic.adapter.CommentAdapterListener;
import com.ixuea.courses.mymusic.api.Api;
import com.ixuea.courses.mymusic.domain.BaseModel;
import com.ixuea.courses.mymusic.domain.Comment;
import com.ixuea.courses.mymusic.domain.event.SelectedFriendEvent;
import com.ixuea.courses.mymusic.domain.event.SelectedTopicEvent;
import com.ixuea.courses.mymusic.domain.response.DetailResponse;
import com.ixuea.courses.mymusic.domain.response.ListResponse;
import com.ixuea.courses.mymusic.domain.response.Meta;
import com.ixuea.courses.mymusic.fragment.CommentMoreDialogFragment;
import com.ixuea.courses.mymusic.listener.HttpObserver;
import com.ixuea.courses.mymusic.listener.OnItemClickListener;
import com.ixuea.courses.mymusic.util.ClipBoardUtil;
import com.ixuea.courses.mymusic.util.Constant;
import com.ixuea.courses.mymusic.util.KeyBoardUtil;
import com.ixuea.courses.mymusic.util.LogUtil;
import com.ixuea.courses.mymusic.util.StringUtil;
import com.ixuea.courses.mymusic.util.ToastUtil;

import org.apache.commons.lang3.StringUtils;
import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.HashMap;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import butterknife.BindView;
import butterknife.OnClick;
import retrofit2.Response;

import static com.ixuea.courses.mymusic.util.Constant.SHEET_ID;

/**
 * 评论界面
 */
public class CommentActivity extends BaseTitleActivity implements CommentAdapterListener, TextWatcher, OnRefreshListener, OnLoadMoreListener {

    private static final String TAG = "CommentActivity";
    /**
     * 列表控件
     * 布局中用的是LRecyclerView extends RecyclerView
     * RecyclerView：父类
     *
     * 改：用LRecyclerView，用到了里面的方法
     */
    @BindView(R.id.rv)
    LRecyclerView rv;

    /**
     * 输入框
     */
    @BindView(R.id.et_content)
    EditText et_content;
    private CommentAdapter adapter;
    private LRecyclerViewAdapter adapterWrapper;
    private String parentId;//设置被恢复评论的id
    private String sheetId;//传递过来的歌单id
    private int lastContentLength;//上一次的编辑框 文本长度
    private Meta pageMeta;//分页模型对象


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
        //注册发布订阅框架
        //最好的实现是：用户选择了话题，或者选择了好友的时候注册，为了简单，我们在initDatum里面注册EventBus
        //如果用户没有点击话题等，这个资源也就是会浪费的（但是如果要实现用户点击了话题后才去注册，
        // 需要复杂一点（比较点击话题的是在另外一个界面点击））
        EventBus.getDefault().register(this);

        //获取传递的数据（这里是歌单id）
        sheetId = extraString(SHEET_ID);

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

        //设置评论适配器监听器
        adapter.setCommentAdapterListener(this);

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

        //输入框添加文本监听器
        et_content.addTextChangedListener(this);

        //设置列表下拉刷新监听器
        rv.setOnRefreshListener(this);

        //设置列表上拉加载更多监听器
        rv.setOnLoadMoreListener(this);
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
     * http://dev-my-cloud-music-api-rails.ixuea.com/v1/comments?SHEET_ID=1&page=1&order=10
     *
     * SHEET_ID=1&page=1&order=10
     * SHEET_ID 歌单id
     * page 页数
     * order 最热评论(点赞数最多) 只要传递了这个参数，返回的json评论数据就是点赞数 多到少排序
     *
     * 这里请求成功后，调用了loadMore，再次进行网络请求
     * http://dev-my-cloud-music-api-rails.ixuea.com/v1/comments?SHEET_ID=1&page=1
     * 这里是请求回来的数据在 原来（上一次请求的数据中）再次添加（没有情况） adapter.addDatum(data.getData());
     * loadMore请求回来的数据并没有按照最热评论(点赞数多少排序)，这里是默认排序
     *
     * 总结：fetchData里本来请求的是10条数据，在里面调用loadMore方法请求10条数据后
     * 所以总的数据是20条
     *
     */
    private void fetchData() {
        //查询参数
        HashMap<String, String> query = getQuery();

        //添加最热查询参数
        //http://dev-my-cloud-music-api-rails.ixuea.com/v1/comments?page=1&order=10
        //这里的page没有添加,不过不影响(因为总的数据为10条)
        //后面添加参数order=10 表示按最热评论(点赞数按多到少排序)
        //如果没有添加后面的order=10参数，那么就是默认排序
        query.put(Constant.ORDER, String.valueOf(Constant.ORDER_HOT));

        //请求最新评论列表
        Api.getInstance()
                .comments(query)
                .subscribe(new HttpObserver<ListResponse<Comment>>() {
                    @Override
                    public void onSucceeded(ListResponse<Comment> data) {
                        LogUtil.d(TAG, "data size:" + data.getData().size());
                        //设置数据
                        adapter.setDatum(data.getData());
                        //告诉控件刷新完成了(参数2：默认刷新的数据：10条)
//                        rv.refreshComplete(Constant.DEFAULT_PAGE_SIZE);

                        //加载更多评论
                        loadMore();
                    }
                });
    }

    /**
     * 获取查询参数
     *
     * @return
     */
    private HashMap<String, String> getQuery() {
        //创建map
        HashMap<String, String> query = new HashMap<>();

        //添加歌单id
        if (StringUtils.isNotBlank(sheetId)) {
            //这个SHEET_ID=1  是我们和服务器协商好要传递的参数
            query.put(SHEET_ID, sheetId);
        }
        return query;
    }

    /**
     * 加载更多评论
     * http://dev-my-cloud-music-api-rails.ixuea.com/v1/comments?SHEET_ID=1&page=1
     */
    private void loadMore() {
        //查询参数
        HashMap<String, String> query = getQuery();
        //添加分页查询参数
        query.put(Constant.PAGE, String.valueOf(Meta.nextPage(pageMeta)));
        //最新评论(因为没有添加最新那个参数：order=10；所以默认就是最新的)
        Api.getInstance()
                .comments(query)//此时中参数包括了SHEET_ID和page这2个字段参数
                .subscribe(new HttpObserver<ListResponse<Comment>>() {
                    @Override
                    public void onSucceeded(ListResponse<Comment> data) {
                        //保存分页参数
                        pageMeta = data.getMeta();

                        //添加数据到适配器
                        //因为这里是上拉加载更多，是不能包原来的数据清空的，所以用addDatum
                        adapter.addDatum(data.getData());

                        //告诉控件刷新完成了(参数2：默认刷新的数据：10条)
                        rv.refreshComplete(Constant.DEFAULT_PAGE_SIZE);
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
        intent.putExtra(SHEET_ID, sheetId);
        //启动界面
        activity.startActivity(intent);
    }

    /**
     * 头像点击了
     */
    @Override
    public void onAvatarClick(Comment data) {
        //携带用户id跳转到用户详情界面
        UserDetailActivity.startWithId(getMainActivity(), data.getUser().getId());
    }

    /**
     * 点赞 点击
     * <p>
     * 思路是：点赞成功后，返回一个id（保存到Comment对象的 like_id字段上）
     * 取消点赞，传入Comment对象的 like_id字段值，取消点赞
     *
     * @param data
     */
    @Override
    public void onLikeClick(Comment data) {
        if (data.isLiked()) {
            //已经点赞了

            //取消点赞

            //传递的是这条Comment对象中 关联 的Id
            Api.getInstance()
                    //data.getId():该item评论对象Comment的id
                    .deleteLike(data.getLike_id())
                    .subscribe(new HttpObserver<Response<Void>>() {
                        @Override
                        public void onSucceeded(Response<Void> d) {
                            //在原来的基础上 - 1
                            data.setLikes_count(data.getLikes_count() - 1);

                            //取消点赞后，记得把整个 字符串数据置为null
                            data.setLike_id(null);

                            //通知适配器刷新数据
                            adapter.notifyDataSetChanged();
                        }
                    });

        } else {
            //没有点赞

            //点赞
            Api.getInstance()
                    //data.getId():该item评论对象Comment的id
                    .like(data.getId())
                    .subscribe(new HttpObserver<DetailResponse<BaseModel>>() {
                        @Override
                        public void onSucceeded(DetailResponse<BaseModel> d) {
                            //这里在本地的List集合中的Comment对象中的 likes_count+1，其实这个时候，服务器那边的点赞数据已经改变了
                            //而我们这里不用重新调用网络请求，直接在item对象(Comment对象)上+1
                            //刷新的时候根据List中的Comment对象的字段值 来刷新的

                            //我们关闭界面后，重新进入界面，这个时候才是网络请求（这个时候的数据就是从服务器返回的）
                            //2个区别：前面的：1.网络请求后，在本地Comment对象字段值+1
                            //               2:关闭界面，重新进入界面网络请求显示数据

                            //在原来的基础上 + 1
                            data.setLikes_count(data.getLikes_count() + 1);

                            //点赞成功后，会返回一个id（把整个id设置到Comment对象关联的字段上like_id上）
                            data.setLike_id(d.getData().getId());
                            //通知适配器刷新数据
                            adapter.notifyDataSetChanged();
                        }
                    });
        }
    }
    //文本监听器

    /**
     * 文本改变前调用
     */
    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {
        LogUtil.d(TAG, "beforeTextChanged:" + s + "," + et_content.getText().toString());
    }

    /**
     * 文本改变了调用
     *
     * @param s
     * @param start
     * @param before
     * @param count
     */
    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {
        LogUtil.d(TAG, "onTextChanged:" + s + "," + et_content.getText().toString());
    }

    /**
     * 文本改变了后调用
     *
     * 修复了减少文本跳转的bug（比如：123#123   删除123变成 123# 会跳转页面的bug）
     *
     * 但是还是有个小问题：比如从中间输入文本也是会跳转的（比如：123#  中间输入 1253# 还是会跳转）
     * //这里我们就不处理，处理起来还是有点麻烦
     * @param s
     */
    @Override
    public void afterTextChanged(Editable s) {
        //s.toString():输入框中的文本
        LogUtil.d(TAG, "afterTextChanged:" + s.toString());
        //获取当前文本长度
        int currentLength = s.toString().length();

        //如果当前的长度大于原来的长度
        if (currentLength > lastContentLength) {
            //新增内容

            //如果不判断
            //用户删除到@,#符号也会跳转

            //获取现在的文本
            String data = s.toString();

            if (data.endsWith(Constant.HASH_TAG)) {
                //结尾输入了#

                //跳转到选择话题界面 (startActivity:是父类BaseCommonActivity的)
                startActivity(SelectTopicActivity.class);
            } else if (data.endsWith(Constant.MENTION)) {
                //输入了@

                //跳转到选择好友界面
                startActivity(SelectFriendActivity.class);
            }
        }
        //保存当前长度
        lastContentLength = currentLength;

    }
    //end 文本监听器

    /**
     * 选中了话题回调
     *
     * @param event
     */
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onSelectedTopic(SelectedTopicEvent event) {
        //添加话题标题
        //(event.getData():获取的是传过来的topic item对象)
        //因为在编辑框中结尾中 输入了# 跳转页面
        //点击话题返回本页面中，
        et_content.append(event.getData().getTitle());

        //添加结尾字符
        et_content.append("# ");

        //高亮文本
        highlightText();

    }

    /**
     * 选择了好友（关注的人）回调
     *
     * @param event
     */
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onSelectedFriend(SelectedFriendEvent event) {

        //添加用户
        et_content.append(event.getUser().getNickname());

        //对于@ 这种结尾添加空格就行了
        et_content.append(" ");

        highlightText();
    }

    /**
     * 高亮文本
     */
    private void highlightText() {
        //设置文本高亮(参数2：编辑框文本：这里是重新获取添加后的内容)
        et_content.setText(StringUtil.processHighlight(getMainActivity(),
                et_content.getText().toString()));

        //设置光标位置(这里传入文本长度：表示光标放到最后)
        et_content.setSelection(et_content.getText().toString().length());
    }

    /**
     * 页面销毁了会调用
     */
    @Override
    protected void onDestroy() {
        //取消注册发布订阅框架
        EventBus.getDefault().unregister(this);
        super.onDestroy();
    }

    /**
     * 下拉刷新回调
     *  下拉刷新的时候记得置为null(让数据回到原来的1)
     *  query.put(Constant.PAGE,String.valueOf(Meta.nextPage(pageMeta)));
     *  也就是这里pageMeta为null，那么Meta.nextPage(pageMeta)的值为：默认返回1
     *  那么query添加键值对：page = 1
     */
    @Override
    public void onRefresh() {
        LogUtil.d(TAG, "onRefresh");
        fetchData();

        //刷新的时候记得置为null(让数据回到原来的1 page = 1)
        pageMeta = null;
    }

    /**
     * 上拉加载更多回调方法
     *
     *     "meta": {
     *         "current_page": 1,
     *         "next_page": 2,
     *         "prev_page": null,
     *         "total_pages": 11,
     *         "total_count": 102
     *     }
     *
     * 因为返回的Meta对象中有个current_page，当我们每次进入网络请求的时候，
     * 我们会在地址：比如http://dev-my-cloud-music-api-rails.ixuea.com/v1/comments?SHEET_ID=1&page=1
     * 字段中的page +1
     * 然后第二次进行网络请求时地址变为http://dev-my-cloud-music-api-rails.ixuea.com/v1/comments?SHEET_ID=1&page=2
     * 那么返回的对象Meta中
     *
     *     "meta": {
     *          "current_page": 2,
     *          "next_page": 3,
     *          "prev_page": 1,
     *          "total_pages": 11,
     *          "total_count": 102
     *          },
     * 这个current_page就会变为2
     * 那么我们只要判断请求回来的 current_page(当前页) < total_pages(总页数) 说明还可以加载下一页
     * 否则加载完成了
     */
    @Override
    public void onLoadMore() {
        LogUtil.d(TAG, "onLoadMore");
        //第一次 pageMeta == null，进入到if里面
        //第二次 pageMeta不等于null，但是pageMeta.getCurrent_page() < pageMeta.getTotal_pages()为true，进入if里面
        if (pageMeta == null || pageMeta.getCurrent_page() < pageMeta.getTotal_pages()) {
            //还有数据

            //加载更多
            loadMore();

            //告诉框架有更多数据了(因为如果有新的数据进来了，直接调用loadMore()方法进行下拉刷新的话，是没有数据的；
            // 但是上拉加载更多的时候是有数据的；所以这里通知LRecyclerView控件)
            /*
              数据
              1 2 3 4  比如第一次加载2条也就是1 2(第一页的数据 1 2)，此时有数据0进来
              变成了0 1 2 3 4  此时可能获取的是3 4(当然不一定是3 4，只是举个例子)
              //当然上面的表述也不一定准确，最好还是设置下，通知有更多的数据加载进来了
             */
            rv.setNoMore(false);
        } else {
            //没有数据了

            //告诉框架没有更多数据了
            rv.setNoMore(true);
        }
    }
}
