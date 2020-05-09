package com.jovines.lbsshare.bean

import java.util.*

/**
 * @author Jovines
 * create 2020-05-07 9:32 下午
 * description:
 */
data class PremiumUsersReturn(val id: Long,
                              val user: Long,
                              val jointime: Date,
                              val goodreason: String,
                              val nickname: String,
                              val description: String,
                              val avatar: String,
                              val lon: Double,
                              val lat: Double,
                              var messages: List<CardMessageReturn>? = null)