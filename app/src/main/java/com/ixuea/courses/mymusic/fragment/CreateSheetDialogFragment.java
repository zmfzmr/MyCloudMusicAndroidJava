package com.ixuea.courses.mymusic.fragment;

import android.app.Dialog;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

import com.ixuea.courses.mymusic.R;
import com.ixuea.courses.mymusic.domain.event.CreateSheetEvent;
import com.ixuea.courses.mymusic.util.ToastUtil;

import org.apache.commons.lang3.StringUtils;
import org.greenrobot.eventbus.EventBus;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.FragmentManager;

/**
 *
 */
public class CreateSheetDialogFragment extends DialogFragment {

    private EditText et;//输入框

    /**
     * 返回dialog
     *
     * @param savedInstanceState
     * @return
     */
    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        //这类的参数：ViewGroup root(根布局 父布局)   没有这个参数传递，所以传入个null
        //在fragment中，上下文用getActivity()
        View view = View.inflate(getActivity(), R.layout.dialog_create_sheet, null);
        //查找控件
        et = view.findViewById(R.id.et);

        //创建对话框构建器
        //用的是androidx中的，因为不同的手机效果不一样，用这个androidx的里面有一些兼容包
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        //设置标题
        builder.setTitle(R.string.create_sheet)
                //设置view（布局转成的view）也就是AlertDialog对话框上显示这个布局view(因为已经设置标题，
                // 所以这个view位于标题的下面)
                .setView(view);
        //设置确认点击事件
        builder.setPositiveButton(R.string.confirm, (dialog, which) -> {
            //如果是一句代码的话，这{ }2个括号可以不用，如果是多久就必须要加了

            //获取输入的内容
            String data = et.getText().toString().trim();
            //判断是否输入了文本
            if (StringUtils.isNotBlank(data)) {
                //关闭对话框
                dialog.dismiss();
                //发送歌单点击事件
                //也可以通过监听器实现
                //传入data数据，传到外面去处理
                EventBus.getDefault().post(new CreateSheetEvent(data));
            } else {
                //最好的实现是
                //没有输入文本
                //不关闭对话框
                //但默认的事件是关闭

                //如果要实现不关闭
                //要么自定义确认按钮
                //要么反射去掉关闭功能

                //弹出提示
                ToastUtil.errorShortToast(R.string.hint_enter_sheet_name);
            }
        });
        //重点：这里记得返回对话框
        return builder.create();
    }

    public static void show(FragmentManager fragmentManager) {
        //创建对话框
        CreateSheetDialogFragment fragment = newInstance();

        //显示
        //注意:这里的show方法是父类DialogFragment里面的方法
        //参数2：tag ,FragmentManager可以通过tag找到这个fragment
        //注意：是通过本类对象来调动；因为是在static，前缀要使用对象的引用来调用
        fragment.show(fragmentManager, "CreateSheetDialogFragment");
    }

    /**
     * 创建方法
     *
     * @return
     */
    public static CreateSheetDialogFragment newInstance() {

        Bundle args = new Bundle();

        CreateSheetDialogFragment fragment = new CreateSheetDialogFragment();
        fragment.setArguments(args);
        return fragment;
    }
}
