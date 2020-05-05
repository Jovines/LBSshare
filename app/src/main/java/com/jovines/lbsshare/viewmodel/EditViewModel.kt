package com.jovines.lbsshare.viewmodel

import android.net.Uri
import androidx.databinding.ObservableField
import com.amap.api.mapcore.util.it
import com.jovines.lbsshare.App
import com.jovines.lbsshare.base.viewmodel.BaseViewModel
import com.jovines.lbsshare.network.ApiGenerator
import com.jovines.lbsshare.network.UserApiService
import com.jovines.lbsshare.utils.addImageToMultipartBodyBuilder
import com.jovines.lbsshare.utils.extensions.setSchedulers
import okhttp3.MultipartBody

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

    fun publishAnArticle() {
        val builder = MultipartBody.Builder()
        builder.addFormDataPart("phone", App.user.phone.toString())
        builder.addFormDataPart("lat", App.user.lat.toString())
        builder.addFormDataPart("lon", App.user.lon.toString())
        builder.addFormDataPart("password", App.user.password)
        builder.addFormDataPart("title", title.get() ?: "")
        builder.addFormDataPart("content", content.get() ?: "")
        builder.addImageToMultipartBodyBuilder("images", imageUriList)
        userApiService.postAMessage(builder.build().parts).setSchedulers()
            .subscribe {
                if (it.code == 1000) {
                    toastEvent.value = "发布成功"
                } else {
                    toastEvent.value = "位置原因，发布失败"
                }
            }.isDisposed
    }
}