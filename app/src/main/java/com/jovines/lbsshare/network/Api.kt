package com.jovines.lbsshare.network

/**
 * @author Jovines
 * @create 2020-04-11 10:06 PM
 *
 * 描述:
 *   网络接口
 */

object Api {

    const val BASE_URL = "http://139.196.143.240:8080/nest_server/"

    const val LAND_URL = "user/login"

    const val REGISTER_URL = "user/register"

    const val CHANGE_AVATAR_URL = "user/changeAvatar"

    const val BASE_PICTURE_URI = "${BASE_URL}image"

    const val FIND_NEARBY_URI = "user/findNearby"

    const val UPDATE_LOCATION_URI = "user/updateLocation"


    const val GET_PREMIUM_USERS = "user/getPremiumUsers"


    const val UPDATE_USER_INFORMATION = "user/updateUserInformation"

    const val QUERY_SOMEONE_MESSAGE = "lifecirclemessageitem/checkPersonMessage"

    const val FIND_NEARBY_MESSAGES = "lifecirclemessageitem/findLatestNewsNearby"

    const val POST_A_MESSAGE = "lifecirclemessageitem/postAMessage"

    const val ADD_TIME_VISITED = "lifecirclemessageitem/addTimeVisited"

    const val GET_NEWS_ACTIVE_USERS = "lifecirclemessageitem/getNewsActiveUsers"

    const val QUERY_MESSAGE = "lifecirclemessageitem/queryMessage"

    const val DELETE_MESSAGE = "lifecirclemessageitem/deleteMessage"

    const val GET_QUALITY_USER_NEWS = "lifecirclemessageitem/getQualityUserNews"


    const val GET_UNEXPIRED_EVENTS = "activity/getUnexpiredEvents"


    const val DELETE_COMMENT = "comment/delete"

    const val ADD_COMMENT = "comment/addComment"

    const val QUERY_COMMENT = "comment/queryCommentsByMessageId"

    const val GET_BANNER = "banner/getBanner"


}