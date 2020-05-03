package com.jovines.lbsshare.bean

import java.io.Serializable
import java.util.*

/**
 * (Lifecirclemessageitem)实体类
 *
 * @author Jovines
 * @since 2020-04-30 20:43:50
 */
class LifecircleMessageItem
    (var id: Long? = null,
    var user: Long? = null,
    var title: String? = null,
    var content: String? = null,
    var time: Date? = null,
    var lon: Double? = null,
    var lat: Double? = null): Serializable {

    companion object {
        private const val serialVersionUID = -40812006033387974L
    }
}