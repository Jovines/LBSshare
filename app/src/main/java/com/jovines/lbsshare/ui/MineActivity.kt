package com.jovines.lbsshare.ui

import android.os.Bundle
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import com.jovines.lbsshare.App
import com.jovines.lbsshare.AppViewModel
import com.jovines.lbsshare.R
import com.jovines.lbsshare.adapter.PersonalMessageAdapter
import com.jovines.lbsshare.base.BaseViewModelActivity
import com.jovines.lbsshare.databinding.ActivityMineBindingImpl
import com.jovines.lbsshare.utils.extensions.gone
import com.jovines.lbsshare.utils.extensions.visible
import com.jovines.lbsshare.viewmodel.MineViewModel
import kotlinx.android.synthetic.main.activity_mine.*

class MineActivity : BaseViewModelActivity<MineViewModel>() {

    lateinit var appViewModel: AppViewModel


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val bindingImpl =
            DataBindingUtil.setContentView<ActivityMineBindingImpl>(this, R.layout.activity_mine)
        bindingImpl.user = App.user
        appViewModel = App.getAppViewModelProvider(this)[AppViewModel::class.java]
        bindingImpl.appViewModel = appViewModel
        initActivity()
    }

    private fun initActivity() {
        val personalMessageAdapter = PersonalMessageAdapter(viewModel.personalPublish)
        recy_personal_message.adapter = personalMessageAdapter
        personal_back_button.setOnClickListener { finish() }
        set_button.setOnClickListener { startActivity<UserSettingActivity>() }
        viewModel.personalPublish.observe(this, Observer {
            personalMessageAdapter.notifyDataSetChanged()
            number_of_messages_sent.text = it?.size?.toString() ?: ""
            if (it?.isEmpty() != false) no_track_of_life.visible() else no_track_of_life.gone()
        })
        viewModel.queryMessage()
    }

    override val viewModelClass = MineViewModel::class.java
}
