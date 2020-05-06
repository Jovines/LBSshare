package com.jovines.lbsshare.ui

import android.content.Intent
import android.os.Bundle
import androidx.databinding.DataBindingUtil
import com.jovines.lbsshare.App
import com.jovines.lbsshare.AppViewModel
import com.jovines.lbsshare.R
import com.jovines.lbsshare.base.BaseViewModelActivity
import com.jovines.lbsshare.databinding.ActivityUserSettingBindingImpl
import com.jovines.lbsshare.viewmodel.SettingViewModel
import kotlinx.android.synthetic.main.activity_user_setting.*

class UserSettingActivity : BaseViewModelActivity<SettingViewModel>() {

    lateinit var appViewModel: AppViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val bindingImpl = DataBindingUtil.setContentView<ActivityUserSettingBindingImpl>(
            this,
            R.layout.activity_user_setting
        )
        bindingImpl.appViewModel = App.getAppViewModelProvider(this)[AppViewModel::class.java]
        appViewModel = App.getAppViewModelProvider(this)[AppViewModel::class.java]
        initActivity()
    }

    private fun initActivity() {
        head_image_change.setOnClickListener {
            val intent = Intent()
            intent.type = "image/*"
            intent.action = "android.intent.action.PICK"
            intent.addCategory("android.intent.category.DEFAULT")
            startActivityForResult(intent, EditActivity.PICTURE_SELECTION)
        }
    }


    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        when (requestCode) {
            EditActivity.PICTURE_SELECTION -> {
                data?.data?.let {
                    viewModel.changeHeadImage(it) { userBean ->
                        appViewModel.avatar.set(userBean.avatar)
                    }
                }
            }
        }

    }

    override val viewModelClass = SettingViewModel::class.java
}