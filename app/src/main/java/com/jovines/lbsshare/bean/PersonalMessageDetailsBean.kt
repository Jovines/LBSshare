package com.jovines.lbsshare.bean

import java.util.*

/**
 * @author jon
 * @create 2020-05-06 5:18 PM
 *
 * 描述:
 *
 */
data class PersonalMessageDetailsBean(
    var id: Long? = null,
    var user: Long? = null,
    var title: String? = null,
    var content: String? = null,
    var time: Date? = null,
    var lon: Double? = null,
    var lat: Double? = null,
    var images: String? = null,
    var viewUserCount: Int? = null,
    var position: String = ""
)