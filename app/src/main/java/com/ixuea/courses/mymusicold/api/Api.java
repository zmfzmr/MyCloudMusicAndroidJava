package com.ixuea.courses.mymusicold.api;

import com.ixuea.courses.mymusicold.domain.Sheet;
import com.ixuea.courses.mymusicold.domain.response.DetailResponse;
import com.ixuea.courses.mymusicold.domain.response.ListResponse;
import com.ixuea.courses.mymusicold.util.Constant;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;
import okhttp3.OkHttpClient;
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


}
