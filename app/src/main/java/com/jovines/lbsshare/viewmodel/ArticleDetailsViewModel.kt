package com.jovines.lbsshare.viewmodel

import android.annotation.SuppressLint
import android.view.View
import androidx.databinding.ObservableInt
import androidx.lifecycle.MutableLiveData
import com.jovines.lbs_server.entity.CommentBean
import com.jovines.lbsshare.base.viewmodel.BaseViewModel
import com.jovines.lbsshare.network.ApiGenerator
import com.jovines.lbsshare.network.UserApiService
import com.jovines.lbsshare.utils.extensions.setSchedulers

/**
 * Created by Jovines on 2020/06/07 11:15
 * description :
 */
@SuppressLint("CheckResult")
class ArticleDetailsViewModel: BaseViewModel(){

    val isPictureShow = ObservableInt(View.GONE)

    val service = ApiGenerator.getApiService(UserApiService::class.java)

    val comments = MutableLiveData<List<CommentBean>>(listOf())


    fun addComment(content: String, msgId: Long,action:(code: Int)->Unit){
        service.addComment(content = content, msgtId = msgId)
            .setSchedulers()
            .subscribe({
                action(it.code)
            },{})
    }

    fun deleteComment(commentId: Long, action: (code: Int) -> Unit) {

        val service = ApiGenerator.getApiService(UserApiService::class.java)

        service.deleteComment(commentId = commentId)
            .setSchedulers()
            .subscribe({
                action(it.code)
            }, {})

    }

    fun queryComments(msgId: Long) {
        service.queryComment(msgtId = msgId)
            .setSchedulers()
            .subscribe({
                comments.value = it.data
            }, {})
    }




}