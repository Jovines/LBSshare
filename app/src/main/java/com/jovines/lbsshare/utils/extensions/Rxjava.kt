package com.jovines.lbsshare.utils.extensions

import io.reactivex.Observable
import io.reactivex.Scheduler
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers

/**
 * Created By jay68 on 2018/8/10.
 */

/**
 * note：请放在有UI操作的操作符前（map等操作符后）, 否则将抛出异常，原因：{@see <a href="https://www.jianshu.com/p/3e5d53e891db"/>}
 */
fun <T> Observable<T>.setSchedulers(
        subscribeOn: Scheduler = Schedulers.io(),
        unsubscribeOn: Scheduler = Schedulers.io(),
        observeOn: Scheduler = AndroidSchedulers.mainThread()
): Observable<T> = subscribeOn(subscribeOn)
        .unsubscribeOn(unsubscribeOn)
        .observeOn(observeOn)

/**
 * 未实现onError时不会抛出[io.reactivex.exceptions.OnErrorNotImplementedException]异常
 */
fun <T> Observable<T>.safeSubscribeBy(
        onError: (Throwable) -> Unit = {},
        onComplete: () -> Unit = {},
        onNext: (T) -> Unit = {}
): Disposable = subscribe(onNext, onError, onComplete)