package com.ixuea.courses.mymusicold.listener;

import com.ixuea.courses.mymusicold.util.LogUtil;

/**
 * 网络请求Observer
 * <p>
 * 有些错误不方便放在ObserverAdapter处理，所以定义了HttpObserver类这个是继承ObserverAdapter<T>的
 * <p>
 * 本类有成功与失败方法
 */
public abstract class HttpObserver<T> extends ObserverAdapter<T> {


    private static final String TAG = "HttpObserver";

    /**
     * 请求成功
     *
     * @param data 数据（对象或者集合）
     *             Succeeded:success  后面的2个s改成ed
     *             改成抽象类，让子类实现
     */
    public abstract void onSucceeded(T data);

    /**
     * 请求失败
     *
     * @param data 数据（对象或者集合）
     * @param e    Throwable
     * @return boolean
     */
    public boolean onFailed(T data, Throwable e) {
        return false;
    }

    @Override
    public void onNext(T t) {
        super.onNext(t);
        LogUtil.d(TAG, "onNext:" + t);

        //TODO 处理错误

        //请求正常
        onSucceeded(t);
    }

    @Override
    public void onError(Throwable e) {
        super.onError(e);
        LogUtil.d(TAG, "onError:" + e.getLocalizedMessage());

        //TODO 处理错误
    }

}
