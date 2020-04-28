package com.jovines.lbsshare.ui

import android.os.Bundle
import com.jovines.lbsshare.APP
import com.jovines.lbsshare.R
import com.jovines.lbsshare.base.BaseActivity
import com.jovines.lbsshare.ui.main.MainActivity

class SplashActivity : BaseActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)
        Thread {
            Thread.sleep(2000)
            if (APP.isLogin()) {
                startActivity<MainActivity>()
            } else {
                startActivity<StartUpActivity>()
            }
            finish()
        }.start()
    }
}
