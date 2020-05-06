package com.jovines.lbsshare

import androidx.databinding.ObservableField
import androidx.lifecycle.ViewModel

/**
 * @author jon
 * @create 2020-05-06 10:23 PM
 *
 * 描述:
 *
 */
class AppViewModel: ViewModel() {

    /**
     * 头像的链接
     */
    val avatar = ObservableField<String>(App.user.avatar)

    /**
     * 昵称
     */
    val nickname = ObservableField<String>(App.user.nickname)

    /**
     * 用户描述
     */
    val description = ObservableField<String>(App.user.description)
}