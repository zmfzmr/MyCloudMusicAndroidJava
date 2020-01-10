package com.ixuea.courses.mymusicold.api;

import com.chuckerteam.chucker.api.ChuckerInterceptor;
import com.facebook.stetho.okhttp3.StethoInterceptor;
import com.ixuea.courses.mymusicold.AppContext;
import com.ixuea.courses.mymusicold.domain.BaseModel;
import com.ixuea.courses.mymusicold.domain.Session;
import com.ixuea.courses.mymusicold.domain.Sheet;
import com.ixuea.courses.mymusicold.domain.User;
import com.ixuea.courses.mymusicold.domain.response.DetailResponse;
import com.ixuea.courses.mymusicold.domain.response.ListResponse;
import com.ixuea.courses.mymusicold.util.Constant;
import com.ixuea.courses.mymusicold.util.LogUtil;

import org.apache.commons.lang3.StringUtils;

import java.util.HashMap;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

public class Api {
    /**
     * Api单例字段
     */
    private static Api instance;
    /**
     * Service单例
     */
    private final Service service;

    /**
     * 返回当前对象的唯一实例
     * <p>
     * 单例设计模式
     * 由于移动端很少有高并发
     * 所以这个就是简单判断
     *
     * @return 本类单例
     */
    public static Api getInstance() {
        if (instance == null) {
            instance = new Api();
        }
        return instance;
    }


    /**
     * 私有构造方法
     */
    private Api() {
        OkHttpClient.Builder okhttpClientBuilder = new OkHttpClient.Builder();
        if (LogUtil.isDebug) {//是//调试模式的时候才添加拦截器
            HttpLoggingInterceptor loggingInterceptor = new HttpLoggingInterceptor();
            ///设置日志等级
            loggingInterceptor.level(HttpLoggingInterceptor.Level.BASIC);
            //打印请求头
//            loggingInterceptor.level(HttpLoggingInterceptor.Level.HEADERS);
            //打印BODy
//            loggingInterceptor.level(HttpLoggingInterceptor.Level.BODY);
            //添加拦截器到OkHttpClient.Builder（添加到网络框架中）
            okhttpClientBuilder.addInterceptor(loggingInterceptor);

            //添加Stetho抓包拦截器(注意：addNetworkInterceptor，而不是addInterceptor)
            okhttpClientBuilder.addNetworkInterceptor(new StethoInterceptor());

            /**
             * 前面的实现，基本上是要配合电脑才能使用，或者说只有开发人员使用才方便，但真实项目中，
             * 可能希望除开发外的人员（测试，产品等）也能在网络请求出错了，
             * 自己查看是什么错误，从而将Bug直接提交给相应的人，而不是，本来是后端问题，
             * 结果因为测试不确定是哪里的问题，就直接将Bug反馈给客户端开发人员了，
             * 而我们一查看后，还需要转给服务端人员；所以我们希望有一个工具，集成到客户端里面的，
             * 其他人员能直接在手机上查看网络请求，这样可以使用Chucker，他也是基于OkHttp拦截器来实现的。
             */
            //添加Chucker实现应用内显示网络请求信息拦截器
            okhttpClientBuilder.addInterceptor(new ChuckerInterceptor(AppContext.getInstance()));

        }

        //构建者模式
        //初始化Retrofit
        Retrofit retrofit = new Retrofit.Builder()
                //让Retrofit使用okhttp
                .client(okhttpClientBuilder.build())
                //api地址
                .baseUrl(Constant.ENDPOINT)//这里使用的是地址的公共前缀
                //适配Rxjava（就是所返回的对象以Rxjava这种方式来工作（比如我们使用了Observable这种方式，接口Service查看））
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                //使用gson解析json
                //包括请求参数和响应
                // （比如使用Retrofit请求框架请求数据，发送对象，也会转换成json（使用gson转换））
                .addConverterFactory(GsonConverterFactory.create())
                //创建Retrofit
                .build();

        //创建Service
        service = retrofit.create(Service.class);
    }

    /**
     * 歌单列表
     *
     * @return 返回Observable<SheetListWrapper>
     */
    public Observable<ListResponse<Sheet>> sheets() {
        return service.sheets()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }

    /**
     * 歌单详情
     *
     * @param id 传入的第几个歌曲Id
     * @return 返回Retrofit接口实例 里面的方法返回的对象
     */
    public Observable<DetailResponse<Sheet>> sheetDetail(String id) {
        return service.sheetDetail(id)
                .subscribeOn(Schedulers.io())//在子线程执行
                .observeOn(AndroidSchedulers.mainThread());//在主线程观察（操作UI在主线程）
    }

    /**
     * 歌单详情
     *
     * @param id 传入的第几个歌曲Id
     * @return 返回Retrofit接口实例 里面的方法返回的对象
     */
    public Observable<DetailResponse<User>> userDetail(String id, String nickname) {

        //添加查询参数
        HashMap<String, String> data = new HashMap<>();

        if (StringUtils.isNotBlank(nickname)) {
            //如果昵称不为空才添加
            // nickname=11111111  键Constant.NICKNAME对应nickname;  值nickname对应11111111
            data.put(Constant.NICKNAME, nickname);
        }

        return service.userDetail(id, data)
                .subscribeOn(Schedulers.io())//在子线程执行
                .observeOn(AndroidSchedulers.mainThread());//在主线程观察（操作UI在主线程）
    }

    /**
     * 注册
     *
     * @param data User
     * @return Observable<DetailResponse < BaseModel>>
     */
    public Observable<DetailResponse<BaseModel>> register(User data) {
        return service.register(data)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }

    /**
     * 登录方法
     *
     * @param data User
     * @return Observable<DetailResponse < Session>>
     */
    public Observable<DetailResponse<Session>> login(User data) {
        return service.login(data)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }


}
