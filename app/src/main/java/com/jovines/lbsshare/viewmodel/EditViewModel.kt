package com.jovines.lbsshare.viewmodel

import android.app.Activity
import android.net.Uri
import androidx.databinding.ObservableField
import com.jovines.lbsshare.App
import com.jovines.lbsshare.base.viewmodel.BaseViewModel
import com.jovines.lbsshare.event.PrivateMessageChanges
import com.jovines.lbsshare.network.ApiGenerator
import com.jovines.lbsshare.network.UserApiService
import com.jovines.lbsshare.ui.helper.DialogHelper.articleRelease
import com.jovines.lbsshare.utils.addImageToMultipartBodyBuilder
import com.jovines.lbsshare.utils.extensions.errorHandler
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import okhttp3.MultipartBody
import org.greenrobot.eventbus.EventBus

/**
 * @author jon
 * @create 2020-04-30 4:00 PM
 *
 * 描述:
 *
 */
class EditViewModel : BaseViewModel() {

    val imageUriList = mutableListOf<Uri>()

    val title = ObservableField("")

    val content = ObservableField("")

    private val userApiService: UserApiService =
        ApiGenerator.getApiService(UserApiService::class.java)

    fun publishAnArticle(
        activity: Activity,
        isUploadLocation: Boolean = true
    ) {
        val dialog = articleRelease(activity)
        dialog.show()
        val time = System.currentTimeMillis()
        Observable.create<List<MultipartBody.Part>> {
            val builder = MultipartBody.Builder()
            builder.addFormDataPart("phone", App.user.phone.toString())
            if (isUploadLocation) {
                builder.addFormDataPart("lat", App.user.lat.toString())
                builder.addFormDataPart("lon", App.user.lon.toString())
            }
            builder.addFormDataPart("password", App.user.password)
            builder.addFormDataPart("title", title.get() ?: "")
            builder.addFormDataPart("content", content.get() ?: "")
            builder.addImageToMultipartBodyBuilder("images", imageUriList)
            it.onNext(builder.build().parts)
        }
            .subscribeOn(Schedulers.io())
            .doOnNext {
                //防止上传时间过短导致一闪而过，觉得貌似是bug
                val currentTimeMillis = System.currentTimeMillis()
                if (currentTimeMillis - time < 600) {
                    Thread.sleep(600 - (currentTimeMillis - time))
                }
            }
            .subscribe {
                userApiService.postAMessage(it)
                    .observeOn(AndroidSchedulers.mainThread())
                    .errorHandler()
                    .subscribe({ statusWarp ->
                        if (statusWarp.code == 1000) {
                            toastEvent.value = "发布成功"
                            EventBus.getDefault().post(PrivateMessageChanges())
                            activity.finish()
                        } else {
                            toastEvent.value = "未知原因，发布失败"
                        }
                        dialog.dismiss()
                    }, {
                        toastEvent.value = "图片过大，请重新选择"
                        dialog.dismiss()
                    })
            }.isDisposed
    }
}