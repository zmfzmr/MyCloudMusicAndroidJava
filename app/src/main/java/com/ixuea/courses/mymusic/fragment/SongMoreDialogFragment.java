package com.ixuea.courses.mymusic.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.ixuea.courses.mymusic.R;
import com.ixuea.courses.mymusic.api.Api;
import com.ixuea.courses.mymusic.domain.Sheet;
import com.ixuea.courses.mymusic.domain.Song;
import com.ixuea.courses.mymusic.domain.event.CollectSongClickEvent;
import com.ixuea.courses.mymusic.domain.event.SheetChangedEvent;
import com.ixuea.courses.mymusic.listener.HttpObserver;
import com.ixuea.courses.mymusic.util.Constant;
import com.ixuea.courses.mymusic.util.ImageUtil;
import com.ixuea.courses.mymusic.util.PreferenceUtil;
import com.ixuea.courses.mymusic.util.ToastUtil;

import org.greenrobot.eventbus.EventBus;

import androidx.fragment.app.FragmentManager;
import butterknife.BindView;
import butterknife.OnClick;
import retrofit2.Response;

/**
 * 歌曲-更多对话框
 */
public class SongMoreDialogFragment extends BaseBottomSheetDialogFragment {
    /**
     * 封面图
     */
    @BindView(R.id.iv_banner)
    ImageView iv_banner;

    /**
     * 标题
     */
    @BindView(R.id.tv_title)
    TextView tv_title;

    /**
     * 歌手信息
     */
    @BindView(R.id.tv_info)
    TextView tv_info;

    /**
     * 评论数
     */
    @BindView(R.id.tv_comment_count)
    TextView tv_comment_count;

    /**
     * 歌手名称（这个和前面的歌手信息是一样的）
     */
    @BindView(R.id.tv_singer_name)
    TextView tv_singer_name;

    /**
     * 从歌单中删除音乐容器
     */
    @BindView(R.id.ll_delete_song_in_sheet)
    View ll_delete_song_in_sheet;

    private Sheet sheet;//歌单
    private Song song;//音乐

    @Override
    protected void initDatum() {
        super.initDatum();
        //获取传递的对象
        sheet = (Sheet) getArguments().getSerializable(Constant.SHEET);
        song = (Song) getArguments().getSerializable(Constant.SONG);

        //封面
        ImageUtil.show(getMainActivity(), iv_banner, sheet.getBanner());

        //标题（歌曲名称）
        tv_title.setText(song.getTitle());

        //歌手信息（歌手名字）
        tv_info.setText(song.getSinger().getNickname());

        //评论 (注意：本来是这里是Song的评论，而这里实现的是Sheet的评论)
        tv_comment_count.setText(getResources().getString(R.string.comment_count, sheet.getComments_count()));


        //歌手名称(分享 下面的 )
        tv_singer_name.setText(
                getResources().getString(R.string.singer_name, song.getSinger().getNickname()));

        //只有我的歌单才能显示删除音乐
        //这里已经判断了  当前登录的用户 == 歌单的用户
        if (PreferenceUtil
                .getInstance(getMainActivity())
                .getUserId()
                .equals(sheet.getUser().getId())) {
            //当前登录的用户等于歌单的用户，才能显示删除
            //(登录的用户信息保存在本地持久化里面)
            ll_delete_song_in_sheet.setVisibility(View.VISIBLE);
        }

    }

    @Override
    protected View getLayoutView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_dialog_song_more, null);
    }

    /**
     * 构造方法
     * //这里Bundle包含了：传过来的Sheet和Song对象
     */
    public static SongMoreDialogFragment newInstance(Sheet sheet, Song song) {
        //创建bundle
        Bundle args = new Bundle();
        //创建fragment
        SongMoreDialogFragment fragment = new SongMoreDialogFragment();
        //添加参数
        //因为Song实现了Serializable接口
        //所以才可以直接添加（继承BaseMultiItemEntity extends BaseModel implements Serializable ）
        args.putSerializable(Constant.SHEET, sheet);
        args.putSerializable(Constant.SONG, song);
        //设置参数
        fragment.setArguments(args);
        return fragment;
    }

    /**
     * 显示对话框
     */
    public static void show(FragmentManager fragmentManager, Sheet sheet, Song song) {
        //创建fragment
        SongMoreDialogFragment fragment = newInstance(sheet, song);

        //显示 参数2：tag，这里用不到，可以随便传
        fragment.show(fragmentManager, "song_more_dialog");
    }

    /**
     * 收藏歌曲到歌单按钮点击
     * 这里是发送通知，回调到SheetDetailActivity中处理
     */
    @OnClick(R.id.ll_collect_song)
    public void onCollectSongClick() {
        //关闭对话框
        dismiss();

        //可以使用监听器回调到Activity中，但是我们这里用的EventBus发送通知的这种方式
        EventBus.getDefault().post(new CollectSongClickEvent(song));
    }

    /**
     * 从歌单中删除音乐点击
     */
    @OnClick(R.id.ll_delete_song_in_sheet)
    public void OnDeleteSongInSheetClick() {
        //也可以将逻辑写到这里(因为这个(从歌单删除)按钮只有我创建的歌单才有，所以逻辑可以直接写到这里来)
        Api.getInstance()
                .deleteSongInSheet(sheet.getId(), song.getId())
                .subscribe(new HttpObserver<Response<Void>>() {
                    @Override
                    public void onSucceeded(Response<Void> data) {
                        //提示
                        ToastUtil.successShortToast(R.string.success_delete_song_in_sheet);

                        //发布事件(可以用之前创建的SheetChangedEvent)
                        EventBus.getDefault().post(new SheetChangedEvent());

                        //关闭对话框
                        dismiss();
                    }
                });
    }
}
