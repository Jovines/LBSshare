package com.jovines.lbs_server.entity

/**
 * Created by Jovines on 2020/6/7 15:07
 * description :
 */
data class Comment(
    var id: Long? = null,
    var messageId: Long? = null,
    var userNickName: String? = null,
    var avatar: String? = null,
    var content: String? = null,
    var time: String? = null,
    var isSelf: Boolean = false
)