package com.jovines.lbsshare.viewmodel

import androidx.core.content.edit
import com.jovines.lbsshare.App
import com.jovines.lbsshare.base.viewmodel.BaseViewModel
import com.jovines.lbsshare.config.PASSWORD
import com.jovines.lbsshare.config.USER_NAME
import com.jovines.lbsshare.event.LoginStateChangeEvent
import com.jovines.lbsshare.network.ApiGenerator
import com.jovines.lbsshare.network.UserApiService
import com.jovines.lbsshare.utils.ExecuteOnceObserver
import com.jovines.lbsshare.utils.extensions.setSchedulers
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import org.greenrobot.eventbus.EventBus
import org.jetbrains.anko.defaultSharedPreferences
import java.util.regex.Pattern

class LoginViewModel : BaseViewModel() {

    private val userApiService: UserApiService =
        ApiGenerator.getApiService(UserApiService::class.java)

    /**
     * 登陆
     * @param password 密码
     * @param phone 手机号码
     * @param successCallBack 登陆成功后回调
     * @param failedCallback 登陆失败回调
     */
    fun land(
        password: String,
        phone: Long,
        successCallBack: () -> Unit,
        failedCallback: (Int) -> Unit
    ) {
        val time = System.currentTimeMillis()
        userApiService
            .land(phone, password)
            .setSchedulers(observeOn = Schedulers.io())
            .doOnNext {
                val l = 2000 - (System.currentTimeMillis() - time)
                Thread.sleep(if (l > 0) l else 0)
            }
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(ExecuteOnceObserver(onExecuteOnceError = {

            }, onExecuteOnceNext = {
                if (it.code == 1000) {
                    App.context.defaultSharedPreferences.edit {
                        putLong(USER_NAME, phone)
                        putString(PASSWORD, password)
                    }
                    App.user = it.data
                    EventBus.getDefault().post(LoginStateChangeEvent(true))
                    successCallBack.invoke()
                } else {
                    failedCallback(it.code)
                }
            }))
    }

    fun register(
        nickname: String,
        phone: Long,
        password: String,
        successCallBack: () -> Unit,
        failedCallback: (Int) -> Unit
    ) {
        val time = System.currentTimeMillis()
        userApiService.register(phone, password, nickname)
            .setSchedulers(observeOn = Schedulers.io())
            .doOnNext {
                val l = 2000 - (System.currentTimeMillis() - time)
                Thread.sleep(if (l > 0) l else 0)
            }
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(ExecuteOnceObserver(
                onExecuteOnceNext = {
                    if (it.code == 1000) {
                        App.context.defaultSharedPreferences.edit {
                            putLong(USER_NAME, phone)
                            putString(PASSWORD, password)
                        }
                        App.user = it.data
                        EventBus.getDefault().post(LoginStateChangeEvent(true))
                        successCallBack.invoke()
                    } else {
                        failedCallback(it.code)
                    }
                }
            ))
    }

    /**
     * 检查手机号是否正确
     */
    fun checkPhone(phone: String): Boolean {
        val matcher = Pattern.compile("1[0-9]{10}").matcher(phone)
        return matcher.matches()
    }

    /**
     * 检查密码是否正确
     */
    fun checkPassword(password: String): Boolean {
        val matcher = Pattern.compile("[\\w\\S]{6,16}").matcher(password)
        return matcher.matches()
    }

    /**
     * 检查昵称是否正确
     */
    fun checkNickname(nickname: String): Boolean {
        val matcher = Pattern.compile("[\\u2E80-\\u9FFF\\w]{1,12}").matcher(nickname)
        return matcher.matches()
    }
}
