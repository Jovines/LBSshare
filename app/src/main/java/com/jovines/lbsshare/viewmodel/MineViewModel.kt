package com.jovines.lbsshare.viewmodel

import androidx.lifecycle.MutableLiveData
import com.jovines.lbsshare.base.viewmodel.BaseViewModel
import com.jovines.lbsshare.bean.PersonalMessageDetailsBean
import com.jovines.lbsshare.network.ApiGenerator
import com.jovines.lbsshare.network.UserApiService
import com.jovines.lbsshare.utils.extensions.setSchedulers

/**
 * @author jon
 * @create 2020-05-06 5:15 PM
 *
 * 描述:
 *
 */
class MineViewModel: BaseViewModel() {

    private val userApiService: UserApiService =
        ApiGenerator.getApiService(UserApiService::class.java)

    val personalPublish = MutableLiveData<List<PersonalMessageDetailsBean>>()


    fun queryMessage(){
        userApiService.queryMessage()
            .setSchedulers()
            .subscribe ({
                personalPublish.value = it.data
            },{
                //网络错误等原因，所以赋值为空list并提示
                personalPublish.value = listOf()
                toastEvent.value = "网络错误，请检查网络链接"
            }).isDisposed
    }
}