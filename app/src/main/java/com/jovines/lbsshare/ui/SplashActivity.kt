package com.jovines.lbsshare.ui

import android.os.Bundle
import com.jovines.lbsshare.App
import com.jovines.lbsshare.R
import com.jovines.lbsshare.base.BaseActivity
import com.jovines.lbsshare.ui.login.LoginActivity
import com.jovines.lbsshare.ui.main.MainActivity

class SplashActivity : BaseActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)
        if (App.isLogin()) {
            startActivity<MainActivity>()
        } else {
            startActivity<LoginActivity>()
        }
        finish()
    }
}
