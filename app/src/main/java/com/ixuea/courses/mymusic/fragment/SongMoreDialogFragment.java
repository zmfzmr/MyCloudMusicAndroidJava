package com.ixuea.courses.mymusic.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.ixuea.courses.mymusic.R;
import com.ixuea.courses.mymusic.domain.Sheet;
import com.ixuea.courses.mymusic.domain.Song;
import com.ixuea.courses.mymusic.util.Constant;

import androidx.fragment.app.FragmentManager;

/**
 * 歌曲-更多对话框
 */
public class SongMoreDialogFragment extends BaseBottomSheetDialogFragment {

    @Override
    protected View getLayoutView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_dialog_song_more, null);
    }

    /**
     * 构造方法
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
}
