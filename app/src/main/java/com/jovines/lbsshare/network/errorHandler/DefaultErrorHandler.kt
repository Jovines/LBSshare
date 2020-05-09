package com.jovines.lbsshare.network.errorHandler

import com.jovines.lbsshare.App
import com.jovines.lbsshare.BuildConfig
import org.jetbrains.anko.toast
import retrofit2.HttpException
import java.net.ConnectException
import java.net.SocketTimeoutException
import java.net.UnknownHostException

/**
 * 只处理简单的网络异常及账号密码错误
 * Created By jay68 on 2018/8/12.
 */
object DefaultErrorHandler : ErrorHandler {
    override fun handle(e: Throwable?): Boolean {
        when {
            e == null -> App.context.toast(
                if (BuildConfig.DEBUG) throw NullPointerException("throwable must be not null") else "未知错误"
            )
            e is SocketTimeoutException || e is ConnectException || e is UnknownHostException -> e.tipToast(
                "服务失联啦～～"
            )
            e is HttpException -> App.context.toast(
                if (BuildConfig.DEBUG) e.response()?.raw().toString() else "此服务暂时不可用"
            )
            e.message != null && BuildConfig.DEBUG -> App.context.toast("error: ${e.message}")

            else -> if (BuildConfig.DEBUG) e.toString()
        }
        return true
    }

    fun Throwable.tipToast(releaseMessage: String) {
        App.context.toast(
            if (BuildConfig.DEBUG) toString() else releaseMessage
        )
    }
}