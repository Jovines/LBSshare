package com.jovines.lbsshare.ui

import android.os.Bundle
import com.jovines.lbsshare.R
import com.jovines.lbsshare.base.BaseViewModelActivity
import com.jovines.lbsshare.viewmodel.MainViewModel
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : BaseViewModelActivity<MainViewModel>() {
    override val viewModelClass = MainViewModel::class.java

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        initActivity()
    }

    private fun initActivity() {
        main_button.setOnClickListener {
            signOut()
            startActivity<StartUpActivity>()
            finish()
        }
    }

    override fun onBackPressed() {
        moveTaskToBack(true)
    }
}
