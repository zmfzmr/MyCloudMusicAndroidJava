package com.ixuea.courses.mymusic.fragment;

import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;

import com.ixuea.courses.mymusic.R;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.FragmentManager;

/**
 * 歌曲排序对话框
 */
public class SortDialogFragment extends DialogFragment {
    /**
     * 点击事件
     */
    private DialogInterface.OnClickListener onClickListener;
    private int sortIndex;//排序索引

    /**
     * 创建对话框
     *
     * @param savedInstanceState
     * @return
     */
    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity())
                .setTitle(R.string.sort)
                //设置单选按钮 2；默认选择第几个 3. 监听器 DialogInterface.OnClickListener类型

//                .setSingleChoiceItems(R.array.dialog_song_sort, 0, onClickListener);
                //sortIndex:是从持久化取出的（如果之前选中的是哪个index，保存到持久化，下次从持久化中取出来设置到本类对象sortIndex中，
                // 这样下次点击进来的时候，就是你选中的那个单选按钮位置了）
                .setSingleChoiceItems(R.array.dialog_song_sort, sortIndex, onClickListener);
        return builder.create();//创建对话框
    }

    /**
     * 创建方法
     */
    public static SortDialogFragment newInstance() {

        Bundle args = new Bundle();

        SortDialogFragment fragment = new SortDialogFragment();
        fragment.setArguments(args);
        return fragment;
    }

    /**
     * 显示对话框
     *  @param fragmentManager
     * @param localMusicSortIndex
     * @param onClickListener 回调到外界判断点击了哪个按钮
     */
    public static void show(FragmentManager fragmentManager, int sortIndex, DialogInterface.OnClickListener onClickListener) {
        //创建对话框
        SortDialogFragment fragment = newInstance();
        //排序索引
        fragment.sortIndex = sortIndex;
        //点击监听器
        //因为是static方法中，所以不能用this
        fragment.onClickListener = onClickListener;
        //显示
        fragment.show(fragmentManager, "SortDialogFragment");
    }
}
