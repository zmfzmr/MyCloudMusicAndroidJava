package com.ixuea.courses.mymusic.listener;

import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;

/**
 * 通用实现Observer里面的方法
 * <p>
 * 目的是避免要实现所有方法
 */
public class ObserverAdapter<T> implements Observer<T> {

    /**
     * 开始订阅了执行（可以简单理解为开始执行前）
     *
     * @param d Disposable对象
     */
    @Override
    public void onSubscribe(Disposable d) {

    }

    /**
     * 下一个Observer（当前Observer执行完成了）
     *
     * @param t 具体的对象或者集合
     */
    @Override
    public void onNext(T t) {

    }

    /**
     * 发生了错误(执行失败了)
     *
     * @param e Throwable对象
     */
    @Override
    public void onError(Throwable e) {

    }

    /**
     * 回调了onNext方法后调用
     */
    @Override
    public void onComplete() {

    }
}
