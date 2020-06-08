package com.jovines.lbsshare.viewmodel

import android.annotation.SuppressLint
import androidx.lifecycle.MutableLiveData
import com.jovines.lbsshare.base.viewmodel.BaseViewModel
import com.jovines.lbsshare.bean.ActivityBean
import com.jovines.lbsshare.bean.BannerBean
import com.jovines.lbsshare.bean.CardMessageReturn
import com.jovines.lbsshare.network.ApiGenerator
import com.jovines.lbsshare.network.UserApiService
import com.jovines.lbsshare.utils.extensions.errorHandler
import com.jovines.lbsshare.utils.extensions.setSchedulers

/**
 * Created by Jovines on 2020/06/06 15:10
 * description :
 */
class FoundViewModel : BaseViewModel() {

    val qualityUserArticles = MutableLiveData<List<CardMessageReturn>>()


    val eventsList = MutableLiveData<List<ActivityBean>>()


    private val service: UserApiService = ApiGenerator.getApiService(UserApiService::class.java)

    fun getQualityUserArticles(action: () -> Unit = {}) = service.getQualityUserNews()
        .setSchedulers()
        .errorHandler()
        .subscribe( {
            qualityUserArticles.value = it.data
            action()
        },{})

    fun getSpecificEvents(action: () -> Unit) {
        val subscribe = service.getUnexpiredEvents()
            .setSchedulers()
            .errorHandler()
            .subscribe( {
                eventsList.value = it.data
            },{})
    }

    @SuppressLint("CheckResult")
    fun getBannerPics(action: (List<BannerBean>) -> Unit) {
        service.getBannerPic()
            .setSchedulers()
            .errorHandler()
            .subscribe({
                action(it.data)
            }, {})
    }
}