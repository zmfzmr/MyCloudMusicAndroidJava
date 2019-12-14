package com.ixuea.courses.mymusicold.adapter;

import android.content.Context;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;

/**
 * 通用FragmentPagerAdapter
 * 主要是创建了列表
 * 实现了通用的方法
 * 直接用FragmentPagerAdapter有可能有内存泄漏
 * 所以使用FragmentStatePagerAdapter
 * 具体的这里不讲解
 * 会在详解系列课程中讲解
 *
 * getItem 在子类重写
 */
public abstract class BaseFragmentPagerAdapter<T> extends FragmentStatePagerAdapter {

    protected final Context context;
    private List<T> datum = new ArrayList<>();

    public BaseFragmentPagerAdapter(Context context, @NonNull FragmentManager fm) {
        super(fm);
        this.context = context;
    }

//    @NonNull
//    @Override
//    public Fragment getItem(int position) { //这个方法用子类重写
//        return datum.get(position);
//    }

    @Override
    public int getCount() {
        return datum.size();
    }

    /**
     * 返回当前的数据
     * @param position
     * @return
     */
    protected T getData(int position) {//返回当前的数据
        return datum.get(position);
    }

    /**
     * 设置数据
     * @param datum
     */
    public void setDatum(List<T> datum) {
        if (datum != null && datum.size() > 0) {
            this.datum.clear();
            this.datum.addAll(datum);
            notifyDataSetChanged();
        }
    }

    /**
     * 添加数据
     * @param datum
     */
    public void addDatum(List<T> datum) {
        if (datum != null && datum.size() > 0) {
            this.datum.addAll(datum);
            notifyDataSetChanged();
        }
    }




}
