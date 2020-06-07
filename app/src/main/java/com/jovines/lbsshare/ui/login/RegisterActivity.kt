package com.jovines.lbsshare.ui.login

import android.graphics.drawable.GradientDrawable
import android.os.Bundle
import android.view.inputmethod.EditorInfo
import androidx.core.content.ContextCompat
import com.afollestad.materialdialogs.MaterialDialog
import com.jovines.lbsshare.R
import com.jovines.lbsshare.base.BaseViewModelActivity
import com.jovines.lbsshare.ui.main.MainActivity
import com.jovines.lbsshare.viewmodel.LoginViewModel
import kotlinx.android.synthetic.main.activity_register.*
import org.jetbrains.anko.toast

class RegisterActivity : BaseViewModelActivity<LoginViewModel>() {

    override val viewModelClass = LoginViewModel::class.java

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)
        initActivity()
    }

    private fun initActivity() {
        register_button.setOnClickListener {
            loginCheck()
        }
        val gradientDrawable = register_button.background as GradientDrawable
        tv_immediate_land.setOnClickListener {
            startActivity<LoginActivity>()
            finish()
        }
        et_register_nickname.afterTextChanged {
            register_nickname_content.error = null
            checkHasContent(gradientDrawable)
        }
        et_register_phone.afterTextChanged {
            cell_phone_number_content.error = null
            checkHasContent(gradientDrawable)
        }
        et_register_password.afterTextChanged {
            register_password_content.error = null
            checkHasContent(gradientDrawable)
        }
        et_register_password.setOnEditorActionListener { v, actionId, event ->
            if (actionId == EditorInfo.IME_ACTION_DONE) {
                loginCheck()
            }
            true
        }
    }

    private fun loginCheck() {
        val materialDialog = MaterialDialog.Builder(this)
            .progress(true, 100)
            .content("注册中...")
            .autoDismiss(false)
            .cancelable(false)
            .build()
        val nickname = et_register_nickname.editableText.toString()
        if (!viewModel.checkNickname(nickname)) {
            register_nickname_content.error = getString(R.string.bad_nickname_format)
            return
        }
        val phone = et_register_phone.editableText.toString()
        if (!viewModel.checkPhone(phone)) {
            cell_phone_number_content.error = getString(R.string.invalid_phone_number)
            et_register_password.editableText.clear()
            return
        }
        val password = et_register_password.editableText.toString()
        if (!viewModel.checkPassword(password)) {
            et_register_password.error = getString(R.string.password_format_error)
            return
        }
        materialDialog.show()
        viewModel.register(nickname, phone.toLong(), password, successCallBack = {
            startActivity<MainActivity>()
            if (materialDialog != null && materialDialog.isShowing) {
                materialDialog.dismiss()
            }
            finish()
        }, failedCallback = {
            when (it) {
                1001 -> {
                    cell_phone_number_content.error =
                        getString(R.string.mobile_number_has_been_registered)
                }
            }
            materialDialog.dismiss()
        })
    }

    private fun checkHasContent(gradientDrawable: GradientDrawable) {
        if (et_register_nickname.editableText.isNotEmpty() && et_register_phone.editableText.isNotEmpty() && et_register_password.editableText.isNotEmpty())
            gradientDrawable.setColor(ContextCompat.getColor(this, R.color.login_button_activated))
        else
            gradientDrawable.setColor(
                ContextCompat.getColor(
                    this,
                    R.color.login_button_not_activated
                )
            )
    }
}
