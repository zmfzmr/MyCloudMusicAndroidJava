package com.ixuea.courses.mymusicold.api;

import com.ixuea.courses.mymusicold.domain.BaseModel;
import com.ixuea.courses.mymusicold.domain.Session;
import com.ixuea.courses.mymusicold.domain.Sheet;
import com.ixuea.courses.mymusicold.domain.User;
import com.ixuea.courses.mymusicold.domain.response.DetailResponse;
import com.ixuea.courses.mymusicold.domain.response.ListResponse;

import java.util.Map;

import io.reactivex.Observable;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;
import retrofit2.http.QueryMap;

/**
 * 网络接口配置
 * <p>
 * 之所以调用接口还能返回数据
 * 是因为Retrofit框架内部处理了
 * 这里不讲解原理
 * 在《详解Retrofit》课程中讲解
 */
public interface Service {

    /**
     * 歌单列表
     */
    @GET("v1/sheets")
//    Observable<SheetListWrapper> sheets();
    Observable<ListResponse<Sheet>> sheets();

    /**
     * 歌单详情
     *
     * @param id {id} 这样表示id，表示的@Path("id")里面的id，
     *           path里面的id其实就是接收后面String id 的值
     *           <p>
     *           一般情况下，三个名称都写成一样，比如3个都是id
     *           <p>
     *           //Retrofit如何知道我们传入的是id呢，其实通过Retrofit注解@Path("id")知道
     *           (应该是相等于限定了id，其他的应该会报错)
     *           <p>
     *           Observable<SheetDetailWrapper>：相等于把json数据转换成这个SheetDetailWrapper类型的对象
     *           Observable：rxjava里面的类
     */
//    @GET("v1/sheets11111111/{id}")//404
    @GET("v1/sheets/{id}")
//    Observable<SheetDetailWrapper> sheetDetail(@Path("id") String id);
    Observable<DetailResponse<Sheet>> sheetDetail(@Path("id") String id);

    /**
     * 注册
     *
     * @param data Data
     *             BaseModel:因为返回json只有一个id，所以这个类也行
     */
    @POST("v1/users")
    Observable<DetailResponse<BaseModel>> register(@Body User data);

    /**
     * 登录
     *
     * @param data User
     * @return Observable<DetailResponse < Session>>
     */
    @POST("v1/sessions")
    Observable<DetailResponse<Session>> login(@Body User data);


    /**
     * 用户详情
     * //后面的查询参数会自动添加到后面的
     * http://dev-my-cloud-music-api-rails.ixuea.com/v1/users/-1?nickname=11111111
     * 比如这里吗的 问号？和参数nickname=11111111会添加到后面
     * 因为这里有个参数 @QueryMap Map<String,String> data
     */
    @GET("v1/users/{id}")
    Observable<DetailResponse<User>> userDetail(@Path("id") String id, @QueryMap Map<String, String> data);

}
