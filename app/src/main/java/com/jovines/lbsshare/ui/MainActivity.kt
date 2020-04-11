package com.jovines.lbsshare.ui

import android.os.Bundle
import com.jovines.lbsshare.R
import com.jovines.lbsshare.base.BaseViewModelActivity
import com.jovines.lbsshare.viewmodel.MainViewModel

class MainActivity : BaseViewModelActivity<MainViewModel>() {
    override val viewModelClass = MainViewModel::class.java

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
    }

}
