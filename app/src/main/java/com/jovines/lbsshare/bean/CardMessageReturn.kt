package com.jovines.lbsshare.bean

/**
 * @author Jovines
 * create 2020-05-01 10:37 下午
 * description:
 */
data class CardMessageReturn(
    var id: Long? = null,
    var user: Long? = null,
    var title: String? = null,
    var content: String? = null,
    var time: String? = null,
    var nickname: String? = null,
    var description: String? = null,
    var avatar: String? = null,
    var lon: Double? = null,
    var lat: Double? = null,
    var images: String? = null
)