package com.jovines.lbsshare.bean

import java.io.Serializable

/**
 * (User)实体类
 *
 * @author Jovines
 * @since 2020-04-11 21:24:09
 */
class UserBean(
    var phone: Long = 0,
    var nickname: String = "",
    var password: String = "",
    var description: String? = null,
    var avatar: String? = null,
    var lon: Double? = null,
    var lat: Double? = null
) : Serializable {

    companion object {
        private const val serialVersionUID = -87868821129653017L
    }
}