package com.ixuea.courses.mymusic.api;

import com.chuckerteam.chucker.api.ChuckerInterceptor;
import com.facebook.stetho.okhttp3.StethoInterceptor;
import com.ixuea.courses.mymusic.AppContext;
import com.ixuea.courses.mymusic.domain.Ad;
import com.ixuea.courses.mymusic.domain.BaseModel;
import com.ixuea.courses.mymusic.domain.Comment;
import com.ixuea.courses.mymusic.domain.Session;
import com.ixuea.courses.mymusic.domain.Sheet;
import com.ixuea.courses.mymusic.domain.Song;
import com.ixuea.courses.mymusic.domain.Topic;
import com.ixuea.courses.mymusic.domain.User;
import com.ixuea.courses.mymusic.domain.response.BaseResponse;
import com.ixuea.courses.mymusic.domain.response.DetailResponse;
import com.ixuea.courses.mymusic.domain.response.ListResponse;
import com.ixuea.courses.mymusic.util.Constant;
import com.ixuea.courses.mymusic.util.LogUtil;
import com.ixuea.courses.mymusic.util.PreferenceUtil;

import org.apache.commons.lang3.StringUtils;

import java.util.HashMap;
import java.util.Map;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

public class Api {
    private static final String TAG = "Api";
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

        //公共请求参数
        okhttpClientBuilder.addNetworkInterceptor(chain -> {
            //获取到偏好设置工具类
            PreferenceUtil sp = PreferenceUtil.getInstance(AppContext.getInstance());
            //获取到request
            Request request = chain.request();

            //
            if (sp.isLogin()) {
                //登录了

                //获取出用户Id 和 和token(其实就是Session)
                String user = sp.getUserId();
                String session = sp.getSession();

                //打印日记是方便调试
                LogUtil.d(TAG, "Api user:" + user + "," + session);
                //将用户id和token(这里是session)
                //注意：这里还要赋值给request
                request = request.newBuilder()
                        .addHeader("User", user)
                        .addHeader("Authorization", session)
                        .build();
            }

            //继续执行网络请求
            return chain.proceed(request);

        });

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
     * 用户详情
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
     * 用户详情(这里的第二个参数为null)
     *
     * @param id 传入的第几个歌曲Id
     * @return Observable<DetailResponse < User>>
     */
    public Observable<DetailResponse<User>> userDetail(String id) {
        return userDetail(id, null);
    }

    /**
     * 关注用户
     */
    public Observable<DetailResponse<BaseModel>> follow(String userId) {
        return service.follow(userId)
                .subscribeOn(Schedulers.io())
                //其实就是HttpObserver中 onFailed onSubscribe onNext 回调方法的执行，在哪个线程执行
                .observeOn(AndroidSchedulers.mainThread());
    }

    /**
     * 取消关注用户
     */
    public Observable<DetailResponse<BaseModel>> deleteFollow(String userId) {
        return service.deleteFollow(userId)
                .subscribeOn(Schedulers.io())
                //其实就是HttpObserver中 onFailed onSubscribe onNext 回调方法的执行，在哪个线程执行
                .observeOn(AndroidSchedulers.mainThread());
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

    /**
     * 重置密码
     *
     * @param data User
     * @return Observable<BaseResponse>
     */
    public Observable<BaseResponse> resetPassword(User data) {
        return service.resetPassword(data)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }

    /**
     * 发送短信验证码
     *
     * @param data User
     * @return Observable<DetailResponse < BaseModel>>
     */
    public Observable<DetailResponse<BaseModel>> sendSMSCode(User data) {
        return service.sendSMSCode(data)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }

    /**
     * 发送邮件验证码
     *
     * @param data User
     * @return Observable<DetailResponse < BaseModel>>
     */
    public Observable<DetailResponse<BaseModel>> sendEmailCode(User data) {
        return service.sendEmailCode(data)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }

    /**
     * 单曲
     *
     * @return Observable<DetailResponse < Song>>
     */
    public Observable<ListResponse<Song>> songs() {
        return service.songs()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }

    /**
     * 单曲详情
     */
    public Observable<DetailResponse<Song>> songDetail(String id) {
        return service.songDetail(id)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }

    /**
     * 广告列表
     *
     * @return Observable<ListResponse < Ad>> 广告的列表
     */
    public Observable<ListResponse<Ad>> ads() {
        return service.ads()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }

    /**
     * 收藏歌单
     * 注意：@Field("id") 就不要了
     */
    public Observable<Response<Void>> collect(String id) {
        return service.collect(id)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }

    /**
     * 取消收藏歌单
     */
    public Observable<Response<Void>> deleteCollect(String id) {
        return service.deleteCollect(id)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }

    /**
     * 评论列表
     */
    public Observable<ListResponse<Comment>> comments(Map<String, String> data) {
        return service.comments(data)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }

    /**
     * 创建评论
     */
    public Observable<DetailResponse<Comment>> createComment(Comment data) {
        return service.createComment(data)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }

    /**
     * 点赞
     * 目前只实现了评论点赞
     */
    public Observable<DetailResponse<BaseModel>> like(String commentId) {
        return service.like(commentId)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }

    /**
     * 取消点赞
     */
    public Observable<Response<Void>> deleteLike(String id) {
        return service.deleteLike(id)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }

    /**
     * 话题列表
     */
    public Observable<ListResponse<Topic>> topics() {
        return service.topics()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }

    /**
     * 好友列表
     */
    public Observable<ListResponse<User>> friends(String id) {
        return service.friends(id)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }

    /**
     * 获取用户创建的歌单
     */
    public Observable<ListResponse<Sheet>> createSheets(String userId) {
        return service.createSheets(userId)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }

    /**
     * 获取用户收藏的歌单
     */
    public Observable<ListResponse<Sheet>> collectSheets(String userId) {
        return service.collectSheets(userId)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }

    /**
     * @param data
     * @return
     */
    public Observable<DetailResponse<Sheet>> createSheet(Sheet data) {
        //注意：这列是createSheet（不是createSheets）
        return service.createSheet(data)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }

    /**
     * 将歌曲收藏到歌单
     *
     * @param sheetId 歌单id
     * @param songId  单曲id
     * @return 注意：这里的是Servicen那边的参数不一样了，我们这里Map(也就是HashMap放到方法里面)，参数用一个songId
     */
    public Observable<Response<Void>> addSongToSheet(String sheetId, String songId) {
        //创建一个字典
        //也可以创建一个对象
        //在传递数据
        HashMap<String, String> data = new HashMap<>();
        //添加歌曲Id
        data.put("id", songId);
        return service.addSongToSheet(sheetId, data)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }

    /**
     * 从歌单中删除音乐
     *
     * @param sheetId 歌单id
     * @param songId  单曲id
     */
    public Observable<Response<Void>> deleteSongInSheet(String sheetId, String songId) {
        return service.deleteSongInSheet(sheetId, songId)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }

}
