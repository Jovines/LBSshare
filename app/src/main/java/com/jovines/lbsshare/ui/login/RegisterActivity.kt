package com.jovines.lbsshare.ui.login

import android.graphics.drawable.GradientDrawable
import android.os.Bundle
import androidx.core.content.ContextCompat
import com.jovines.lbsshare.R
import com.jovines.lbsshare.base.BaseViewModelActivity
import com.jovines.lbsshare.ui.MainActivity
import com.jovines.lbsshare.viewmodel.LoginViewModel
import kotlinx.android.synthetic.main.activity_login.*
import kotlinx.android.synthetic.main.activity_register.*

class RegisterActivity : BaseViewModelActivity<LoginViewModel>() {

    override val viewModelClass = LoginViewModel::class.java

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)
        initActivity()
    }

    private fun initActivity() {
        register_button.setOnClickListener {
            val nickname = et_register_nickname.editableText.toString()
            if (!viewModel.checkNickname(nickname)) {
                register_nickname_content.error = getString(R.string.bad_nickname_format)
                return@setOnClickListener
            }
            val password = et_register_password.editableText.toString()
            if (!viewModel.checkPassword(password)) {
                password_content.error = getString(R.string.password_format_error)
                return@setOnClickListener
            }
            val phone = et_register_phone.editableText.toString()
            if (!viewModel.checkPhone(phone)) {
                cell_phone_number_content.error = getString(R.string.invalid_phone_number)
                et_register_password.editableText.clear()
                return@setOnClickListener
            }
            viewModel.register(nickname, phone.toLong(), password, successCallBack = {
                startActivity<MainActivity>()
            }, failedCallback = {
                when (it) {
                    1001 -> cell_phone_number_content.error =
                        getString(R.string.mobile_number_has_been_registered)
                }
            })
        }
        val gradientDrawable = register_button.background as GradientDrawable
        tv_immediate_land.setOnClickListener {
            startActivity<LoginActivity>()
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
