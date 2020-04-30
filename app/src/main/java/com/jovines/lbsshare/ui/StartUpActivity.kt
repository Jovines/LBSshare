package com.jovines.lbsshare.ui

import android.os.Bundle
import android.view.View
import androidx.constraintlayout.widget.ConstraintSet
import com.jovines.lbsshare.App
import com.jovines.lbsshare.R
import com.jovines.lbsshare.base.BaseActivity
import com.jovines.lbsshare.ui.login.LoginActivity
import com.jovines.lbsshare.ui.login.RegisterActivity
import kotlinx.android.synthetic.main.activity_start.*

class StartUpActivity : BaseActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_start)
        initActivity()
    }

    private fun initActivity() {
        if (App.isLogin()) {
            tv_register.visibility = View.GONE
            tv_land.visibility = View.GONE
            val constraintSet = ConstraintSet().apply { clone(content) }
            constraintSet.setVerticalBias(R.id.tip_word, 0f)
            constraintSet.applyTo(content)
        } else {
            tv_register.setOnClickListener {
                startActivity<RegisterActivity>()
            }
            tv_land.setOnClickListener {
                startActivity<LoginActivity>()
            }
        }
    }

}
