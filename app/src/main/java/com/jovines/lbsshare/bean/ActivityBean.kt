package com.jovines.lbsshare.bean

import java.util.*

/**
 * Created by Jovines on 2020/6/6 17:48
 * description :
 */
class ActivityBean(
    var id: Long? = null,
    var activityName: String? = null,
    var content: String? = null,
    var time: Date? = null,
    var frontCover: String? = null,
    var expired: Int? = null
)