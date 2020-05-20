package com.jovines.lbsshare

import android.app.Activity
import android.app.Application
import android.content.Context
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelStore
import androidx.lifecycle.ViewModelStoreOwner
import com.google.gson.Gson
import com.jovines.lbsshare.bean.UserBean
import com.jovines.lbsshare.config.USER_NAME
import org.jetbrains.anko.defaultSharedPreferences

/**
 * @author Jovines
 * @create 2020-04-11 5:11 PM
 *
 * 描述:启动类
 *
 */
class App : Application(), ViewModelStoreOwner {


    companion object {
        private var appViewModel:AppViewModel? = null

        lateinit var context: Context
            private set
        var user: UserBean = UserBean(0, "")
            set(value) {
                field = value
                appViewModel?.avatar?.set(value.avatar)
                appViewModel?.description?.set (value.description)
                appViewModel?.nickname?.set (value.nickname)
                saveUser()
            }

        fun isLogin() = user.phone != 0L && user.password.isNotEmpty()

        fun saveUser() {
            context.defaultSharedPreferences.edit().putString(USER_NAME, Gson().toJson(user))
                .apply()
        }


        fun getAppViewModelProvider(activity: Activity): ViewModelProvider {
            val app = checkApplication(activity) as App
            return ViewModelProvider(app, app.getAppFactory())
        }

        fun getAppViewModelProvider(fragment: Fragment): ViewModelProvider {
            return getAppViewModelProvider(fragment.requireActivity())
        }

        private fun checkApplication(activity: Activity): Application {
            return activity.application
                ?: throw IllegalStateException(
                    "Your activity is not yet attached to the Application instance." +
                            "You can't request ViewModel before onCreate call.")
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

    private lateinit var mAppViewModelStore: ViewModelStore
    private lateinit var mFactory: ViewModelProvider.Factory

    override fun onCreate() {
        super.onCreate()
        mAppViewModelStore = ViewModelStore()
        mFactory = ViewModelProvider
            .AndroidViewModelFactory
            .getInstance(this)
        appViewModel = ViewModelProvider(this, getAppFactory())[AppViewModel::class.java]
    }

    override fun getViewModelStore(): ViewModelStore {
        return mAppViewModelStore
    }

    private fun getAppFactory(): ViewModelProvider.Factory {
        return mFactory
    }



}