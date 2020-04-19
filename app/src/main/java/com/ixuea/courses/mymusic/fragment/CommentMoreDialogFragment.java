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
 * 评论更多对话框
 */
public class CommentMoreDialogFragment extends DialogFragment {
    //点击监听器
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
        //创建对话框构建器
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        //使用设置item方法
        //当然也可以自定义
        //第一个参数：显示的标题
        //第二个参数：点击回调方法(这个onClickListener是从activity那边传入过来的)
        builder.setItems(R.array.dialog_comment_more, onClickListener);

        //创建对话框
        return builder.create();
    }

    /**
     * 显示对话框
     *
     * @param fragmentManager FragmentManager
     * @param onClickListener 注意：是DialogInterface.OnClickListener
     */
    public void show(FragmentManager fragmentManager, DialogInterface.OnClickListener onClickListener) {
        this.onClickListener = onClickListener;//
        //注意:这里的show方法是父类DialogFragment里面的方法
        //参数2：tag ,FragmentManager可以通过tag找到这个fragment
        show(fragmentManager, "CommentMoreDialogFragment");
    }

    /**
     * fragment构造方法
     */
    public static CommentMoreDialogFragment newInstance() {

        Bundle args = new Bundle();

        CommentMoreDialogFragment fragment = new CommentMoreDialogFragment();
        fragment.setArguments(args);
        return fragment;
    }
}
