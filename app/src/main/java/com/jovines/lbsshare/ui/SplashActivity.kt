package com.jovines.lbsshare.ui

import android.os.Bundle
import android.view.View
import androidx.constraintlayout.widget.ConstraintSet
import com.jovines.lbsshare.APP
import com.jovines.lbsshare.R
import com.jovines.lbsshare.base.BaseActivity
import com.jovines.lbsshare.ui.login.LoginActivity
import com.jovines.lbsshare.ui.login.RegisterActivity
import kotlinx.android.synthetic.main.activity_splash.*

class SplashActivity : BaseActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)
        initActivity()
    }

    private fun initActivity() {
        if (APP.isLogin()) {
            tv_register.visibility = View.GONE
            tv_land.visibility = View.GONE
            val constraintSet = ConstraintSet().apply{ clone(content)}
            constraintSet.setVerticalBias(R.id.tip_word, 0f)
            constraintSet.applyTo(content)
        }else{
            tv_register.setOnClickListener {
                startActivity<RegisterActivity>()
            }
            tv_land.setOnClickListener {
                startActivity<LoginActivity>()
            }
        }
    }

}
