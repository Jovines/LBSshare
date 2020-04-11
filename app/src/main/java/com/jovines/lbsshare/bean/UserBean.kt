package com.jovines.lbsshare.bean

import javax.security.auth.callback.PasswordCallback

/**
 * @author jon
 * @create 2020-04-11 5:14 PM
 *
 * 描述:
 *   用户类
 */
data class UserBean(var id: String, var password: String)