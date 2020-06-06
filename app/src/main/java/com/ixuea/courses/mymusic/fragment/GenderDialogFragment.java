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
 * 选择性别对话框
 */
public class GenderDialogFragment extends DialogFragment {
    /**
     * 选择索引(默认为0)
     */
    private int selectedIndex = 0;
    /**
     * 选择了监听器
     */
    private DialogInterface.OnClickListener onClickListener;

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        return new AlertDialog.Builder(getActivity())
                .setTitle(R.string.select_gender)
                //设置单选按钮
                .setSingleChoiceItems(R.array.dialog_gender, selectedIndex, onClickListener)
                .create();
    }

    /**
     * 显示
     */
    public static void show(FragmentManager fragmentManager,
                            int selectedIndex,
                            DialogInterface.OnClickListener onClickListener) {
        //创建fragment
        GenderDialogFragment fragment = newInstance();
        //设置索引和回调监听器
        fragment.selectedIndex = selectedIndex;
        fragment.onClickListener = onClickListener;
        //显示
        fragment.show(fragmentManager, "GenderDialogFragment");
    }

    /**
     * 创建方法
     */
    public static GenderDialogFragment newInstance() {
        Bundle args = new Bundle();
        GenderDialogFragment fragment = new GenderDialogFragment();
        fragment.setArguments(args);
        return fragment;
    }
}
