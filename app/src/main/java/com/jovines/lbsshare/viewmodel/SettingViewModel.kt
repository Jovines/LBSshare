package com.jovines.lbsshare.viewmodel

import android.net.Uri
import androidx.databinding.ObservableField
import com.jovines.lbs_server.entity.UserBean
import com.jovines.lbsshare.App
import com.jovines.lbsshare.base.viewmodel.BaseViewModel
import com.jovines.lbsshare.network.ApiGenerator
import com.jovines.lbsshare.network.UserApiService
import com.jovines.lbsshare.utils.ExecuteOnceObserver
import com.jovines.lbsshare.utils.addImageToMultipartBodyBuilder
import com.jovines.lbsshare.utils.extensions.setSchedulers
import okhttp3.MultipartBody

/**
 * @author jon
 * @create 2020-05-06 10:15 PM
 *
 * 描述:
 *
 */
class SettingViewModel : BaseViewModel() {


    private val userApiService: UserApiService =
        ApiGenerator.getApiService(UserApiService::class.java)

    lateinit var avatar: ObservableField<String>

    /**
     * 改变头像
     */
    fun changeHeadImage(uri: Uri, callBack: (UserBean) -> Unit) {
        val builder = MultipartBody.Builder()
            .setType(MultipartBody.FORM)
        builder.addFormDataPart("phone", App.user.phone.toString())
        builder.addFormDataPart("password", App.user.password)
        builder.addImageToMultipartBodyBuilder("image", listOf(uri))
        userApiService.changeAvatar(builder.build().parts)
            .setSchedulers()
            .subscribe(ExecuteOnceObserver(onExecuteOnceError = {
                print("")
            }) {
                it?.data?.let { userBean ->
                    App.user = userBean
                    callBack(userBean)
                }
            })
    }

    fun updateUserInformation(
        nickname: String? = null,
        description: String? = null
    ) {
        userApiService.updateUserInformation(nickname,description).setSchedulers().
                subscribe ({
                    if (it.code == 1000) {
                        if (nickname!=null){
                            App.user.nickname = it.data.nickname
                        }
                        if (description != null) {
                            App.user.description = it.data.description
                        }
                        App.saveUser()
                    }
                },{
                    toastEvent.value = "网络错误，请检查网络"
                }).isDisposed
    }
}