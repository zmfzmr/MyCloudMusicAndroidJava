package com.ixuea.courses.mymusicold.listener;

import com.ixuea.courses.mymusicold.activity.BaseCommonActivity;
import com.ixuea.courses.mymusicold.domain.response.BaseResponse;
import com.ixuea.courses.mymusicold.util.HttpUtil;
import com.ixuea.courses.mymusicold.util.LoadingUtil;
import com.ixuea.courses.mymusicold.util.LogUtil;

import io.reactivex.disposables.Disposable;

/**
 * 网络请求Observer
 * <p>
 * 有些错误不方便放在ObserverAdapter处理，所以定义了HttpObserver类这个是继承ObserverAdapter<T>的
 * <p>
 * 本类有成功与失败方法
 */
public abstract class HttpObserver<T> extends ObserverAdapter<T> {


    private static final String TAG = "HttpObserver";
    //final 修饰的成员变量，必须要初始化一次，而这里有个空构造方法，有可能没有初始化成员变量，
    // 所以final修饰的成员变量在编译的时候可能没有初始化，故报错
//    private final BaseCommonActivity activity;
//    private final boolean isShowLoading;
    private BaseCommonActivity activity;//公共Activity
    private boolean isShowLoading;//是否显示加载对话框

    /**
     * 无参构造方法
     */
    public HttpObserver() {

    }

    /**
     * 有参构造方法
     *
     * @param activity      BaseCommonActivity
     * @param isShowLoading 是否显示加载提示框
     */
    public HttpObserver(BaseCommonActivity activity, boolean isShowLoading) {
        this.activity = activity;
        this.isShowLoading = isShowLoading;
    }


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
     * @return boolean false :表示父类（本类）要处理错误（内部处理）；true：表示子类要处理错误（外部处理）
     */
    public boolean onFailed(T data, Throwable e) {
        return false;
    }

    @Override
    public void onSubscribe(Disposable d) {
        super.onSubscribe(d);
        if (isShowLoading) {
            //显示加载对话框
            LoadingUtil.showLoading(activity);
        }
    }

    /**
     * 如果要404错误，只需要把http://dev-my-cloud-music-api-rails.ixuea.com/v1/sheets中的sheets改下，就变成404
     * 如果要500错误，只要用户名不存在就会变成500错误
     * 比如：
     * http://dev-my-cloud-music-api-rails.ixuea.com/v1/users/-1?nickname=11111111这个地址就是500错误
     * <p>
     * 总结：发生错误都会发生在onError中
     *
     *
     *   已经请求成功，但是登录失败了
     *   但是如果用户名 密码错误会返回false
     *
     *   可以理解为200~299之间的值就会返回到这里来
     *   这里面的错误，可以先看看，到时候遇到再说
     * @param t 具体的对象或者集合
     */


    @Override
    public void onNext(T t) {
        super.onNext(t);
        LogUtil.d(TAG, "onNext:" + t);

        //检查是否需要隐藏加载提示框（其他地方如onError中用到，提取到一个方法中）
        checkHideLoading();

        if (isSucceeded(t)) {
            //请求正常
            onSucceeded(t);
        } else {
            //请求出错了（就是登录失败了）
            handlerRequest(t, null);
        }

    }


    @Override
    public void onError(Throwable e) {
        super.onError(e);
        LogUtil.d(TAG, "onError:" + e.getLocalizedMessage());
        //检查是否需要隐藏加载提示框
        checkHideLoading();

        handlerRequest(null, e);//第一个参数为null，出错了，没有数据对象传递到这个方法里面来
    }

    /**
     * 判断网络请求是否成功
     *
     * @param t T
     * @return 是否成功
     */
    private boolean isSucceeded(T t) {
        //比如返回的code=200,（比如用户名 密码错误）
        if (t instanceof BaseResponse) {
            //判断具体的业务请求是否成功
            BaseResponse baseResponse = (BaseResponse) t;

            //没有状态码表示成功
            //这是我们和服务端的一个规定(一般情况下status等于0才是成功，我们这里是null才成功)
            //一般==null，则return true；否则return false
            return baseResponse.getStatus() == null;
        }
        return false;
    }

    /**
     * 处理错误网络请求
     *
     * @param data  T 数据模型对象
     * @param error Throwable错误对象
     */
    private void handlerRequest(T data, Throwable error) {
        if (onFailed(data, error)) {//fasle就会走else，父类（可以说本类）处理错误；true：就是外部处理错误
            //回调了请求失败方法
            //并且该方法返回了true

            //返回true就表示外部手动处理错误
            //那我们框架内部就不用做任何事情了
        } else {
            //调用工具处理错误（这个是父类，内部处理错误）
            HttpUtil.handlerRequest(data, error);

        }


    }

    /**
     * 检查是否需要隐藏加载提示框
     */
    private void checkHideLoading() {
        if (isShowLoading) {
            LoadingUtil.hideLoading();
        }
    }


}
