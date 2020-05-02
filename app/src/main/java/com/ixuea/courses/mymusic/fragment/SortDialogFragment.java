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
                .setSingleChoiceItems(R.array.dialog_song_sort, 0, onClickListener);
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
     *
     * @param fragmentManager
     * @param onClickListener 回调到外界判断点击了哪个按钮
     */
    public static void show(FragmentManager fragmentManager, DialogInterface.OnClickListener onClickListener) {
        //创建对话框
        SortDialogFragment fragment = newInstance();
        //点击监听器
        //因为是static方法中，所以不能用this
        fragment.onClickListener = onClickListener;
        //显示
        fragment.show(fragmentManager, "SortDialogFragment");
    }
}
