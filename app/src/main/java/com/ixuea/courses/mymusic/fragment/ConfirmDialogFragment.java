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
 * 确认对话框(通用对话框)
 */
public class ConfirmDialogFragment extends DialogFragment {
    /**
     * 消息id
     */
    private int messageResourceId;
    //确认点击回调方法
    private DialogInterface.OnClickListener onClickListener;

    /**
     * 创建对话框
     */
    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {

        //注意：尾部用的是 create()  而不是build
        return new AlertDialog.Builder(getActivity())
                //设置标题
                .setTitle(R.string.tips)
                //提示消息
                .setMessage(messageResourceId)
                //确认按钮
                .setPositiveButton(R.string.confirm, onClickListener)
                //取消按钮
                //传入null，表示不处理，默认是关闭对话框的
                .setNegativeButton(R.string.cancel, null)
                .create();
    }

    /**
     * 创建方法
     */
    public static ConfirmDialogFragment newInstance() {

        Bundle args = new Bundle();

        ConfirmDialogFragment fragment = new ConfirmDialogFragment();
        fragment.setArguments(args);
        return fragment;
    }

    /**
     * 显示对话框
     *
     * @param fragmentManager   FragmentManager
     * @param messageResourceId 消息资源 id
     * @param onClickListener   确认点击事件
     */
    public static void show(FragmentManager fragmentManager, int messageResourceId,
                            DialogInterface.OnClickListener onClickListener) {
        //创建对话框
        ConfirmDialogFragment fragment = newInstance();

        //提示消息
        fragment.messageResourceId = messageResourceId;
        //监听器
        fragment.onClickListener = onClickListener;
        //显示
        fragment.show(fragmentManager, "ConfirmDialogFragment");

    }

    /**
     * 显示对话框 (这个是没有消息的)
     *
     * @param fragmentManager FragmentManager
     * @param onClickListener 确认点击事件
     */
    public static void show(FragmentManager fragmentManager,
                            DialogInterface.OnClickListener onClickListener) {
        //直接调用前面写的show方法
        show(fragmentManager, R.string.confirm_message, onClickListener);
    }
}
