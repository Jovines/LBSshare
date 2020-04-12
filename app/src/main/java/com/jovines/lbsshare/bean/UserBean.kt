package com.jovines.lbs_server.entity

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
        var password: String = "")
    : Serializable {

    private val serialVersionUID = -79397126901599023L
}