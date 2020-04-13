package com.jovines.lbsshare.network

import com.jovines.lbs_server.entity.UserBean
import com.jovines.lbsshare.bean.StatusWarp
import io.reactivex.Observable
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.Headers
import retrofit2.http.POST

/**
 * @author Jovines
 * @create 2020-04-11 10:10 PM
 *
 * 描述:
 *   用户接口
 */
interface UserApiService {

    @FormUrlEncoded
    @POST(Api.register_url)
    fun register(@Field("phone") phone: Long,
                 @Field("password") password: String,
                 @Field("nickname") nickname: String): Observable<StatusWarp<UserBean>>

    @FormUrlEncoded
    @POST(Api.land_url)
    fun land(@Field("phone") phone: Long,
             @Field("password") password: String):Observable<StatusWarp<UserBean>>
}