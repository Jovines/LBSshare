package com.jovines.lbsshare.ui.main.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import com.jovines.lbsshare.App
import com.jovines.lbsshare.AppViewModel
import com.jovines.lbsshare.R
import com.jovines.lbsshare.adapter.PersonalMessageAdapter
import com.jovines.lbsshare.base.BaseViewModelFragment
import com.jovines.lbsshare.databinding.FragmentMineBinding
import com.jovines.lbsshare.event.PrivateMessageChanges
import com.jovines.lbsshare.ui.UserSettingActivity
import com.jovines.lbsshare.utils.extensions.gone
import com.jovines.lbsshare.utils.extensions.visible
import com.jovines.lbsshare.viewmodel.MineViewModel
import kotlinx.android.synthetic.main.fragment_mine.*
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode
import org.jetbrains.anko.support.v4.startActivity

class MineFragment : BaseViewModelFragment<MineViewModel>() {

    lateinit var appViewModel: AppViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val bindingImpl = DataBindingUtil.inflate<FragmentMineBinding>(
            inflater,
            R.layout.fragment_mine,
            container,
            false
        )
        bindingImpl.user = App.user
        appViewModel = App.getAppViewModelProvider(this)[AppViewModel::class.java]
        bindingImpl.appViewModel = appViewModel
        // Inflate the layout for this fragment
        return bindingImpl.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initActivity()
    }

    private fun initActivity() {
        val personalMessageAdapter = PersonalMessageAdapter(viewModel.personalPublish)
        recy_personal_message.adapter = personalMessageAdapter
        linearLayout.setOnClickListener { startActivity<UserSettingActivity>() }
        viewModel.personalPublish.observe(viewLifecycleOwner, androidx.lifecycle.Observer {
            personalMessageAdapter.notifyDataSetChanged()
            number_of_messages_sent.text = it.size.toString()
            if (it.isEmpty()) no_track_of_life.visible() else no_track_of_life.gone()
        })
        viewModel.queryMessage()
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    fun sendAMessageYourself(changes: PrivateMessageChanges) {
        viewModel.queryMessage()
    }

    override val viewModelClass = MineViewModel::class.java

}
