package com.jovines.lbsshare.network

import com.jovines.lbs_server.entity.UserBean
import com.jovines.lbsshare.App
import com.jovines.lbsshare.bean.CardMessageReturn
import com.jovines.lbsshare.bean.LifecircleMessageItem
import com.jovines.lbsshare.bean.StatusWarp
import com.jovines.lbsshare.config.DEFAULT_LATITUDE
import com.jovines.lbsshare.config.DEFAULT_LONGITUDE
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
        @Field("range") range: Int,
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
        @Field("lon") lon: Double,
        @Field("lat") lat: Double,
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
        @Field("range") range: Int,
        @Field("time") time: Int,
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

}