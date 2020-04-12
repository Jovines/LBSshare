package com.jovines.lbsshare.utils.extensions

fun <T> takeIfNoException(doOnException: ((Throwable) -> T?)? = null, doFinally: (() -> Unit)? = null, action: () -> T?): T? {
    return try {
        action.invoke()
    } catch (throwable: Throwable) {
        doOnException?.invoke(throwable)
    } finally {
        doFinally?.invoke()
    }
}