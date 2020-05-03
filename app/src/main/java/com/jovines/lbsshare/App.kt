package com.jovines.lbsshare

import android.app.Application
import android.content.Context
import com.google.gson.Gson
import com.jovines.lbs_server.entity.UserBean
import com.jovines.lbsshare.config.USER_NAME
import org.jetbrains.anko.defaultSharedPreferences

/**
 * @author Jovines
 * @create 2020-04-11 5:11 PM
 *
 * 描述:启动类
 *
 */
class App : Application() {

    companion object {
        lateinit var context: Context
            private set
        var user: UserBean = UserBean(0, "")
            set(value) {
                field = value
                saveUser()
            }

        fun isLogin() = user.phone != 0L && user.password.isNotEmpty()

        private fun saveUser() {
            context.defaultSharedPreferences.edit().putString(USER_NAME, Gson().toJson(user))
                .apply()
        }
    }

    override fun attachBaseContext(base: Context) {
        super.attachBaseContext(base)
        context = base
        user = Gson().fromJson(
            context.defaultSharedPreferences.getString(USER_NAME, "{}"),
            UserBean::class.java
        )
    }
}