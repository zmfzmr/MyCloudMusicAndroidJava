package com.ixuea.courses.mymusic.fragment;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.os.Bundle;
import android.widget.DatePicker;

import com.ixuea.courses.mymusic.R;
import com.ixuea.courses.mymusic.util.LogUtil;

import java.util.Calendar;
import java.util.Locale;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.FragmentManager;

/**
 * 选择日期对话框
 */
public class DateDialogFragment extends DialogFragment implements DatePickerDialog.OnDateSetListener {

    private static final String TAG = "DateDialogFragment";
    /**
     * //回调监听器 也可以通过EvenBus回调，不一定需要通过监听器回调
     */
    private DateListener dateListener;

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        //获取日历控件 注意：不用new 直接用getInstance
        Calendar calendar = Calendar.getInstance();

        //获取年  Calendar.YEAR = 1
        int year = calendar.get(Calendar.YEAR);
        //获取月 (注意：这个月默认是返回0的)  MONTH = 2
        int month = calendar.get(Calendar.MONTH);
        //获取日(DAY_OF_MONTH： 月里面的天)  DAY_OF_MONTH = 5;
        int day = calendar.get(Calendar.DAY_OF_MONTH);

        //这个DatePickerDialog并没有androidx, 这里用的系统里面的(android.app)
        //系统的，有个坏处，在不同的手机上可能不一样
        //这里getContext或者getActivity
        //参数2：Theme_AppCompat_Light_Dialog_Alert； light：翻译 ：光亮
        // 用亮色的这个对话框样式
        //参数3：回调监听  4 5 6：默认选中的年月日
        //比如当前时间是：2020-6-07  但是这里的year, month, day分别是2020 05 07  05的原因主要是从0开始算的
        //但是点击对话框的时候： 是以当前的时间来算的2020-6-07;只不过在下面的onDateSet方法中month要+1才行(因为从0开始)
        DatePickerDialog datePickerDialog = new DatePickerDialog(getContext(),
                R.style.Theme_AppCompat_Light_Dialog_Alert,
                this, year, month, day);

        return datePickerDialog;//本身就是对话框，不用.create()(因为不是通过build这种方式)
    }

    /**
     * 选中了日期回调
     *
     * @param view
     * @param year
     * @param month
     * @param dayOfMonth
     */
    @Override
    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
        LogUtil.d(TAG, "onDateSet: " + year + ", " + month + ", " + dayOfMonth);
        //Locale.getDefault(): 本地化实例对象
        //%02d:表示不足2位用0填充，比如：2,不足2位,用0填充，就是：02;
        //      这里是0占位，用其他占位也行，比如：*，可以写成 %*2d  那就是： *2
        //注意：这个月默认是返回0的 所以需要+1
        String dataString = String.format(Locale.getDefault(), "%d-%02d-%02d", year, month + 1, dayOfMonth);

        //回调监听器
        dateListener.onSelected(dataString);
    }

    /**
     * 显示对话框
     */
    public static void show(FragmentManager fragmentManager, DateListener dateListener) {
        //创建fragment
        DateDialogFragment fragment = DateDialogFragment.newInstance();
        //设置监听器
        fragment.dateListener = dateListener;
        //显示
        fragment.show(fragmentManager, "DateDialogFragment");

    }

    /**
     * 创建方法
     * <p>
     * Calendar.getInstance()
     * 注意：前面java包里面用的是getInstance这种方式，而我们fragment用的是newInstance这种方式
     */
    public static DateDialogFragment newInstance() {
        Bundle args = new Bundle();
        DateDialogFragment fragment = new DateDialogFragment();
        fragment.setArguments(args);
        return fragment;
    }

    /**
     * 日期回调监听器
     * <p>
     * 创建在里面，因为外部也没有用到
     */
    public interface DateListener {
        /**
         * 这里我们用的是String类型，项目中根据自己的需要选择使用类型
         *
         * @param date date
         */
        void onSelected(String date);
    }
}
