package com.ixuea.courses.mymusic.api;

import com.ixuea.courses.mymusic.domain.Ad;
import com.ixuea.courses.mymusic.domain.BaseModel;
import com.ixuea.courses.mymusic.domain.Book;
import com.ixuea.courses.mymusic.domain.Comment;
import com.ixuea.courses.mymusic.domain.Feed;
import com.ixuea.courses.mymusic.domain.Session;
import com.ixuea.courses.mymusic.domain.Sheet;
import com.ixuea.courses.mymusic.domain.Song;
import com.ixuea.courses.mymusic.domain.Topic;
import com.ixuea.courses.mymusic.domain.User;
import com.ixuea.courses.mymusic.domain.Video;
import com.ixuea.courses.mymusic.domain.param.OrderParam;
import com.ixuea.courses.mymusic.domain.response.BaseResponse;
import com.ixuea.courses.mymusic.domain.response.DetailResponse;
import com.ixuea.courses.mymusic.domain.response.ListResponse;

import java.util.Map;

import io.reactivex.Observable;
import retrofit2.Response;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.PATCH;
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
     * 重置密码
     */
    @POST("v1/users/reset_password")
    Observable<BaseResponse> resetPassword(@Body User data);

    /**
     * 发送短信验证码
     *
     * @param data User
     * @return Observable<BaseModel> 因为返回的response中有code字段，
     * 而我们也不需要处理这个验证码（这个是服务器帮我们返回的），只需要通过ChuckerTeam（build中查看）就可以查看验证码
     */
    @POST("v1/codes/request_sms_code")
    Observable<DetailResponse<BaseModel>> sendSMSCode(@Body User data);

    /**
     * 发送邮箱验证码
     *
     * @param data User
     * @return Observable<DetailResponse < BaseModel>>
     */
    @POST("v1/codes/request_email_code")
    Observable<DetailResponse<BaseModel>> sendEmailCode(@Body User data);

    /**
     * 用户详情
     * //后面的查询参数会自动添加到后面的
     * http://dev-my-cloud-music-api-rails.ixuea.com/v1/users/-1?nickname=11111111
     * 比如这里吗的 问号？和参数nickname=11111111会添加到后面
     * 因为这里有个参数 @QueryMap Map<String,String> data
     */
    @GET("v1/users/{id}")
    Observable<DetailResponse<User>> userDetail(@Path("id") String id, @QueryMap Map<String, String> data);

    /**
     * 更新用户
     * 注意：更新用户数据 是用的是@PATCh请求
     */
    @PATCH("v1/users/{id}")
    Observable<DetailResponse<BaseModel>> updateUser(@Path("id") String id, @Body User data);

    /**
     * 绑定第三方账号
     * <p>
     * 这里之所以用Map 是因为方便(map里面添加多个数据)
     * 如果要是用对象，还要找有没有重用的对象;没有就要创建对象的类，并把字段添加到类上
     * <p>
     * 用map的第二个解析：防止以后要加多个字段，所以用Map也方便
     */
    @POST("v1/users/bind")
    Observable<DetailResponse<BaseModel>> bindAccount(@Body Map<String, String> data);

    /**
     * 解绑第三方账号
     * 解绑(其实也就是删除，所以用DELETE)
     *
     * @param platform 其实这里的是20(QQ) 40(微博)  这个是和服务端协商好的
     */
    @DELETE("v1/users/{platform}/unbind")
    Observable<DetailResponse<BaseModel>> unbindAccount(@Path("platform") int platform);

    /**
     * 关注用户
     * <p>
     * 因为关注成功后 返回了一个code 我们并不需要接收，所以可以用
     * <p>
     * 注意：这里用的是表单形式(这个接口通知支持2个 表单和body 都是可以的)
     *
     * @ Field("id") String userId    后面的userId 可以随便命名，但是前面的id必须是写id(key是id)
     */
    @FormUrlEncoded
    @POST("v1/friends")
    Observable<DetailResponse<BaseModel>> follow(@Field("id") String userId);

    /**
     * 取消关注用户
     * <p>
     * 注意：这类用的是 @DELETE @Path
     */
    @DELETE("v1/friends/{userId}")
    Observable<DetailResponse<BaseModel>> deleteFollow(@Path("userId") String userId);

    /**
     * 单曲（单曲集合）
     */
    @GET("v1/songs")
    Observable<ListResponse<Song>> songs();

    /**
     * 单曲详情
     *
     * @param id
     * @return
     */
    @GET("v1/songs/{id}")
    Observable<DetailResponse<Song>> songDetail(@Path("id") String id);

    /**
     * 广告列表
     */
    @GET("v1/ads")
    Observable<ListResponse<Ad>> ads();

    /**
     * 收藏歌单
     * 这个id的值传递给服务端的时候就会以sheet_id这个名称向服务端传递
     * 表单形式：sheet_id=1（类似的如：username=ixuea&password=123）
     * <p>
     * 如果是json就是这种{"sheet_id","1"}
     * 记得加上这个@POST
     * <p>
     * Response<Void>：这个表示接收的返回值没有，所以用这个，虽然返回code字段，但是我们并没有定义这个字段
     *
     * 这里收藏的时候，之所以没有传递用户id，因为在请求Retrofit 请求头里面已经添加了用户id了(如果用户登录，就保存用户id到持久化)
     */
    @FormUrlEncoded
    @POST("v1/collections")
    Observable<Response<Void>> collect(@Field("sheet_id") String id);

    /**
     * http://dev-my-cloud-music-api-rails.ixuea.com/v1/collections/1
     * <p>
     * 因为后面只跟一个具体数字，后面并没有?sheet_id = 1或者携带body（就是json数据）所以用 @ Path
     * <p>
     * 注意：这里用的是@DELETE，从服务器端删除收藏内容
     */
    @DELETE("v1/collections/{id}")
    Observable<Response<Void>> deleteCollect(@Path("id") String id);

    /**
     * 评论列表
     *
     * @param data
     * @return 注意: @QueryMap是个注解，前面要有@符号
     * @QueryMap: 就是放一些参数进这map里面  这里key和value都是String类型
     * 原来的地址：
     * http://dev-my-cloud-music-api-rails.ixuea.com/v1/comments
     *
     * order=10 以键值对的方式添加到Map集合里面变成如下
     * http://dev-my-cloud-music-api-rails.ixuea.com/v1/comments?order=10
     *
     * 当然也可以用列表(@Field("sheet_id") String id)这种方式
     * //但是我们这里是查询，后面需要不断的更改参数，所以用@QueryMap 在这Map在里面添加参数即可
     */
    @GET("v1/comments")
    Observable<ListResponse<Comment>> comments(@QueryMap Map<String, String> data);

    /**
     * 创建评论
     * <p>
     * 这里用DetailResponse和用BaseModel效果是一样的
     *
     * @return 用@Body，retrofit这个框架，才知道把这个对象转换成json
     * <p>
     * 创建的话是post请求（因为是请求服务器创建评论）， 而GET是获取请求
     */
    @POST("v1/comments")
    Observable<DetailResponse<Comment>> createComment(@Body Comment data);

    /**
     * 评论点赞
     *
     * @return
     * @ Field 这种就是表单形式 比如后面携带的?sheet_id=1
     */
    @FormUrlEncoded
    @POST("v1/likes")
    Observable<DetailResponse<BaseModel>> like(@Field("comment_id") String commentId);

    /**
     * 取消评论点赞
     * 因为服务端返回null
     * 所以这里要用Response<Void>
     * 不然会有异常
     * <p>
     * Response<Void>：这个表示接收的返回值没有，所以用这个，虽然返回code字段，但是我们并没有定义这个字段
     * 因为后面只跟一个具体数字，后面并没有?id = 1或者携带body（就是json数据）所以用 @ Path
     * //这里跟取消收藏是类似的
     */
    @DELETE("v1/likes/{id}")
    Observable<Response<Void>> deleteLike(@Path("id") String id);

    /**
     * 话题列表
     * 目前没有参数携带
     */
    @GET("v1/topics")
    Observable<ListResponse<Topic>> topics();

    /**
     * 好友列表(我关注的人)
     * <p>
     * <p>
     * ListResponse<User>: 好友列表(我关注的人) 可能有多个 用集合装起来
     * 注意：id 后面还有个 /following  通过id查看是谁的好友(我关注的人)
     *
     * @return
     */
    @GET("v1/users/{id}/following")
    Observable<ListResponse<User>> friends(@Path("id") String id);

    /**
     * 我的粉丝(关注我的人)
     * id：用户的id
     */
    @GET("v1/users/{id}/followers")
    Observable<ListResponse<User>> fans(@Path("id") String id);

    /**
     * 获取用户创建的歌单
     * <p>
     * 这里之所以要传递id，主要是获取其他用户的歌单(查看其他人的歌单)
     */
    @GET("v1/users/{userId}/create")
    Observable<ListResponse<Sheet>> createSheets(@Path("userId") String userId);

    /**
     * 获取用户收藏的歌单(传递的id，主要是查看别人的歌单)
     *
     * @param userId
     * @return
     */
    @GET("v1/users/{userId}/collect")
    Observable<ListResponse<Sheet>> collectSheets(@Path("userId") String userId);

    /**
     * 这里是(用户)创建歌单
     * （上面的那个是通过用户id，获取创建歌单）
     * <p>
     * 当然这里返回的只有一个id（用到的是DetailResponse<Sheet>）
     * 因为这里看json，外层是一层包裹，里面是一个对象；最好用包裹的对，所以用DetailResponse<Sheet>
     * {
     * "data": {
     * "id": 43
     * }
     * }
     */
    @POST("v1/sheets")
    Observable<DetailResponse<Sheet>> createSheet(@Body Sheet data);

    /**
     * 将歌曲收藏到歌单
     *
     * @ Body Map<String,String> data:这里会把Map这集合转换成json数据传输的
     * 当然也可以用一个对象
     */
    @POST("v1/sheets/{sheetId}/relations")
    Observable<Response<Void>> addSongToSheet(@Path("sheetId") String sheetId, @Body Map<String, String> data);

    /**
     * 从歌单中删除音乐(只能删除歌单里面的音乐，别人的音乐无法删除(就算客户端不做限制也无法删除比人的音乐，
     * 因为服务端限制了))
     * <p>
     * 注意：这里是删除，只需要传入id值就行，并不像post请求那样需要传入@Body(Map 或者对象)
     * 对于前面的QueryMap，这是GET请求的时候用到的
     */
    @DELETE("v1/sheets/{sheetId}/relations/{songId}")
    Observable<Response<Void>> deleteSongInSheet(@Path("sheetId") String sheetId, @Path("songId") String songId);

    /**
     * 视频列表
     */
    @GET("v1/videos")
    Observable<ListResponse<Video>> videos();

    /**
     * 视频详情
     */
    @GET("v1/videos/{id}")
    Observable<DetailResponse<Video>> videoDetail(@Path("id") String id);

    /**
     * 动态列表
     * QueryMap 后面可能需要添加参数查询，所以用这个
     */
    @GET("v1/feeds")
    Observable<ListResponse<Feed>> feeds(@QueryMap Map<String, String> data);

    /**
     * 发布动态
     * <p>
     * 为啥要传递个Feed 对象 因为是POST请求, 把对象转换成json传递
     */
    @POST("v1/feeds")
    Observable<DetailResponse<BaseModel>> createFeed(@Body Feed data);

    /**
     * 商品列表
     */
    @GET("v1/books")
    Observable<ListResponse<Book>> shops();

    /**
     * 商品详情
     *
     * @param id 从点击item传过来的id
     */
    @GET("v1/books/{id}")
    Observable<DetailResponse<Book>> shopDetail(@Path("id") String id);

    /**
     * 创建订单
     * 因为没有什么接收的，所以用DetailResponse<BaseModel>
     */
    @POST("v1/orders")
    Observable<DetailResponse<BaseModel>> createOrder(@Body OrderParam data);
}
