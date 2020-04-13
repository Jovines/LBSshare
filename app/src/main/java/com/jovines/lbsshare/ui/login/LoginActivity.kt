package com.jovines.lbsshare.ui.login

import android.graphics.drawable.GradientDrawable
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.widget.EditText
import androidx.core.content.ContextCompat
import com.afollestad.materialdialogs.MaterialDialog
import com.jovines.lbsshare.R
import com.jovines.lbsshare.base.BaseViewModelActivity
import com.jovines.lbsshare.ui.MainActivity
import com.jovines.lbsshare.viewmodel.LoginViewModel
import kotlinx.android.synthetic.main.activity_login.*

class LoginActivity : BaseViewModelActivity<LoginViewModel>() {

    override val viewModelClass = LoginViewModel::class.java


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
        initActivity()
    }

    private fun initActivity() {
        val materialDialog = MaterialDialog.Builder(this)
            .progress(true, 100)
            .content("登陆中...")
            .autoDismiss(false)
            .cancelable(false)
            .build()
        tv_login_button.setOnClickListener {
            val password = login_password.editableText.toString()
            val phone = login_username.editableText.toString()
            if (!viewModel.checkPhone(phone)) {
                username_content.error = getString(R.string.invalid_phone_number)
                return@setOnClickListener
            } else {
                if (!viewModel.checkPassword(password)) {
                    password_content.error = getString(R.string.password_format_error)
                }
            }
            materialDialog.show()
            viewModel.land(password, phone.toLong(), successCallBack = {
                startActivity<MainActivity>()
                finish()
            }, failedCallback = {
                if (it == 1001) {
                    username_content.error = "手机号未注册"
                } else if (it == 1002) {
                    login_password.editableText.clear()
                    password_content.error = "密码输入错误"

                }
                materialDialog.dismiss()
            })
        }
        val gradientDrawable = tv_login_button.background as GradientDrawable
        login_username.afterTextChanged {
            if (it.length < 11)
                username_content.error = null
            checkHasContent(gradientDrawable)
        }
        login_password.afterTextChanged {
            password_content.error = null
            checkHasContent(gradientDrawable)
        }

        tv_immediate_registration.setOnClickListener {
            startActivity<RegisterActivity>()
            finish()
        }
    }

    private fun checkHasContent(gradientDrawable: GradientDrawable) {
        if (login_password.editableText.isNotEmpty() && login_username.editableText.isNotEmpty())
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

/**
 * Extension function to simplify setting an afterTextChanged action to EditText components.
 */
fun EditText.afterTextChanged(afterTextChanged: (String) -> Unit) {
    this.addTextChangedListener(object : TextWatcher {
        override fun afterTextChanged(editable: Editable?) {
            afterTextChanged.invoke(editable.toString())
        }

        override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {}

        override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {}
    })
}
