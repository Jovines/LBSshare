package com.jovines.lbsshare

import android.annotation.SuppressLint
import android.app.Application
import android.content.Context
import com.jovines.lbs_server.entity.UserBean
import com.jovines.lbsshare.config.PASSWORD
import com.jovines.lbsshare.config.USER_NAME
import org.jetbrains.anko.defaultSharedPreferences

/**
 * @author jon
 * @create 2020-04-11 5:11 PM
 *
 * 描述:
 *
 */
class APP : Application() {

    companion object {
        lateinit var context: Context
            private set
        var user: UserBean = UserBean(0,"")

        fun isLogin()= user.phone!= 0L && user.password.isNotEmpty()
    }

    override fun attachBaseContext(base: Context) {
        super.attachBaseContext(base)
        context = base
        user.phone = context.defaultSharedPreferences.getString(USER_NAME, "0")!!.toLong()
        user.password = context.defaultSharedPreferences.getString(PASSWORD, "")!!
    }
}