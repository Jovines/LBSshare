package com.jovines.lbsshare.network

import com.jovines.lbs_server.entity.Comment
import com.jovines.lbs_server.entity.UserBean
import com.jovines.lbsshare.App
import com.jovines.lbsshare.bean.*
import com.jovines.lbsshare.config.DEFAULT_LATITUDE
import com.jovines.lbsshare.config.DEFAULT_LONGITUDE
import com.jovines.lbsshare.config.MESSAGE_QUERY_TIME
import com.jovines.lbsshare.config.SEARCH_SCOPE
import io.reactivex.Observable
import okhttp3.MultipartBody
import retrofit2.http.*

/**
 * @author Jovines
 * @create 2020-04-11 10:10 PM
 *
 * 描述:
 *   用户接口
 */
interface UserApiService {

    /**
     * 注册
     */
    @FormUrlEncoded
    @POST(Api.REGISTER_URL)
    fun register(
        @Field("phone") phone: Long,
        @Field("password") password: String,
        @Field("nickname") nickname: String
    ): Observable<StatusWarp<UserBean>>

    /**
     * 登陆
     */
    @FormUrlEncoded
    @POST(Api.LAND_URL)
    fun land(
        @Field("phone") phone: Long,
        @Field("password") password: String
    ): Observable<StatusWarp<UserBean>>


    /**
     * 寻找附近的人
     */
    @FormUrlEncoded
    @POST(Api.FIND_NEARBY_URI)
    fun findNearby(
        @Field("range") range: Int = SEARCH_SCOPE,
        @Field("time") time: Int = MESSAGE_QUERY_TIME,
        @Field("lat") lat: Double = App.user.lat ?: DEFAULT_LATITUDE,
        @Field("lon") lon: Double = App.user.lon ?: DEFAULT_LONGITUDE,
        @Field("phone") phone: Long = App.user.phone
    ): Observable<StatusWarp<List<UserBean>>>


    /**
     * 更新位置信息
     */
    @FormUrlEncoded
    @POST(Api.UPDATE_LOCATION_URI)
    fun updateLocation(
        @Field("lat") lat: Double,
        @Field("lon") lon: Double,
        @Field("phone") phone: Long = App.user.phone,
        @Field("password") password: String = App.user.password
    ): Observable<StatusWarp<UserBean>>


    /**
     * 更换头像
     */
    @Multipart
    @POST(Api.CHANGE_AVATAR_URL)
    fun changeAvatar(
        @Part partList: List<MultipartBody.Part>
    ): Observable<StatusWarp<UserBean>>


    /**
     * 获取某人所发消息
     */
    @FormUrlEncoded
    @POST(Api.QUERY_SOMEONE_MESSAGE)
    fun querySomeoneMessage(
        @Field("checkUser") checkUser: Long,
        @Field("userOwner") userOwner: Long = App.user.phone
    ): Observable<StatusWarp<List<LifecircleMessageItem>>>


    /**
     * 获取最新消息
     */
    @FormUrlEncoded
    @POST(Api.FIND_NEARBY_MESSAGES)
    fun findLatestNewsNearby(
        @Field("range") range: Int = SEARCH_SCOPE,
        @Field("time") time: Int = MESSAGE_QUERY_TIME,
        @Field("lon") lon: Double = App.user.lon ?: 0.0,
        @Field("phone") phone: Long = App.user.phone,
        @Field("lat") lat: Double = App.user.lat ?: 0.0
    ): Observable<StatusWarp<List<CardMessageReturn>>>


    /**
     * 获取最新消息
     */
    @Multipart
    @POST(Api.POST_A_MESSAGE)
    fun postAMessage(
        @Part partList: List<MultipartBody.Part>
    ): Observable<StatusWarp<String>>

    @FormUrlEncoded
    @POST(Api.ADD_TIME_VISITED)
    fun addTimeVisited(
        @Field("messageId") messageId: Long,
        @Field("password") password: String = App.user.password,
        @Field("phone") phone: Long = App.user.phone
    ): Observable<StatusWarp<String>>


    @FormUrlEncoded
    @POST(Api.GET_NEWS_ACTIVE_USERS)
    fun getNewsActiveUsers(
        @Field("messageId") messageId: Long,
        @Field("password") password: String = App.user.password,
        @Field("phone") phone: Long = App.user.phone
    ): Observable<StatusWarp<List<UserBean>>>


    @FormUrlEncoded
    @POST(Api.DELETE_MESSAGE)
    fun deleteMessage(
        @Field("messageId") messageId: Long,
        @Field("password") password: String = App.user.password,
        @Field("phone") phone: Long = App.user.phone
    ): Observable<StatusWarp<String>>


    @FormUrlEncoded
    @POST(Api.QUERY_MESSAGE)
    fun queryMessage(
        @Field("password") password: String = App.user.password,
        @Field("phone") phone: Long = App.user.phone
    ): Observable<StatusWarp<List<PersonalMessageDetailsBean>>>


    @FormUrlEncoded
    @POST(Api.UPDATE_USER_INFORMATION)
    fun updateUserInformation(
        @Field("nickname") nickname: String? = null,
        @Field("description") description: String? = null,
        @Field("password") password: String = App.user.password,
        @Field("phone") phone: Long = App.user.phone
    ): Observable<StatusWarp<UserBean>>


    @GET(Api.GET_PREMIUM_USERS)
    fun getPremiumUsers(): Observable<StatusWarp<List<PremiumUsersReturn>>>


    @GET(Api.GET_QUALITY_USER_NEWS)
    fun getQualityUserNews(): Observable<StatusWarp<List<CardMessageReturn>>>


    @GET(Api.GET_UNEXPIRED_EVENTS)
    fun getUnexpiredEvents(): Observable<StatusWarp<List<ActivityBean>>>

    @FormUrlEncoded
    @POST(Api.DELETE_COMMENT)
    fun deleteComment(
        @Field("commentId") commentId: Long,
        @Field("password") password: String = App.user.password,
        @Field("phone") phone: Long = App.user.phone
    ): Observable<StatusWarp<String>>

    @FormUrlEncoded
    @POST(Api.QUERY_COMMENT)
    fun queryComment(
        @Field("messageId") msgtId: Long,
        @Field("password") password: String = App.user.password,
        @Field("phone") phone: Long = App.user.phone
    ): Observable<StatusWarp<List<Comment>>>

    @FormUrlEncoded
    @POST(Api.ADD_COMMENT)
    fun addComment(
        @Field("content" ) content: String,
        @Field("messageId") msgtId: Long,
        @Field("password") password: String = App.user.password,
        @Field("phone") phone: Long = App.user.phone
    ): Observable<StatusWarp<String>>








}
