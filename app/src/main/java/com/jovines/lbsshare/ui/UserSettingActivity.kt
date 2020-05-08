package com.jovines.lbsshare.ui

import android.content.Intent
import android.os.Bundle
import androidx.core.widget.addTextChangedListener
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
        et_nickname_change.addTextChangedListener {
            viewModel.updateUserInformation(appViewModel.nickname.get())
        }
        et_introduction_changes.addTextChangedListener {
            viewModel.updateUserInformation(description = appViewModel.description.get())
        }
        personal_back_button.setOnClickListener { finish() }
        exit_login_button.setOnClickListener {
            signOut()
            startActivity(Intent(this, StartUpActivity::class.java).apply {
                flags = Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK
            })
            finish()
        }
    }


    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        when (requestCode) {
            EditActivity.PICTURE_SELECTION -> {
                data?.data?.let {
                    viewModel.changeHeadImage(it) { userBean ->
                        appViewModel.avatar.set(userBean.avatar)
                        App.user.avatar = userBean.avatar
                    }
                }
            }
        }

    }

    override val viewModelClass = SettingViewModel::class.java
}