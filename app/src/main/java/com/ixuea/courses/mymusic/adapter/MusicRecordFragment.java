package com.ixuea.courses.mymusic.adapter;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.ixuea.courses.mymusic.R;
import com.ixuea.courses.mymusic.domain.Song;
import com.ixuea.courses.mymusic.fragment.BaseCommonFragment;
import com.ixuea.courses.mymusic.util.Constant;
import com.ixuea.courses.mymusic.util.ImageUtil;

import androidx.constraintlayout.widget.ConstraintLayout;
import butterknife.BindView;
import de.hdodenhof.circleimageview.CircleImageView;

/**
 * 音乐黑胶唱片界面
 */
public class MusicRecordFragment extends BaseCommonFragment {

    /**
     * 黑胶唱片容器
     */
    @BindView(R.id.cl_content)
    ConstraintLayout cl_content;

    /**
     * 歌曲封面
     */
    @BindView(R.id.iv_banner)
    CircleImageView iv_banner;

    @Override
    protected void initDatum() {
        super.initDatum();

        //获取传递的数据
        Song data = (Song) extraData();

        //显示封面
        ImageUtil.show(getMainActivity(), iv_banner, data.getBanner());
    }

    /**
     * 返回要显示的控件
     */
    @Override
    protected View getLayoutView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_record_music, container, false);
    }

    /**
     * 创建方法
     */
    public static MusicRecordFragment newInstance(Song data) {

        Bundle args = new Bundle();

        //传递数据
        args.putSerializable(Constant.DATA, data);

        MusicRecordFragment fragment = new MusicRecordFragment();
        fragment.setArguments(args);
        return fragment;
    }
}
