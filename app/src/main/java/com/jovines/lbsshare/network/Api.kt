package com.jovines.lbsshare.network

/**
 * @author Jovines
 * @create 2020-04-11 10:06 PM
 *
 * 描述:
 *   网络接口
 */

object Api {

    const val BASE_URL = "http://192.168.0.106:9899"

    const val LAND_URL = "/user/login"

    const val REGISTER_URL = "/user/register"

    const val CHANGE_AVATAR_URL = "/user/changeAvatar"

    const val BASE_PICTURE_URI = "$BASE_URL/image"

    const val FIND_NEARBY_URI = "/user/findNearby"

    const val UPDATE_LOCATION_URI = "/user/updateLocation"

    const val QUERY_SOMEONE_MESSAGE = "/lifecirclemessageitem/checkPersonMessage"

    const val FIND_NEARBY_MESSAGES = "/lifecirclemessageitem/findLatestNewsNearby"

    const val POST_A_MESSAGE = "/lifecirclemessageitem/postAMessage"

}