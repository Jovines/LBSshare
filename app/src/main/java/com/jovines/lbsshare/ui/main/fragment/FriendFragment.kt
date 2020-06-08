package com.jovines.lbsshare.ui.main.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.jovines.lbsshare.App
import com.jovines.lbsshare.AppViewModel
import com.jovines.lbsshare.R
import com.jovines.lbsshare.adapter.RecentNewsAdapter
import com.jovines.lbsshare.base.BaseViewModelFragment
import com.jovines.lbsshare.databinding.FragmentFriendBinding
import com.jovines.lbsshare.event.PrivateMessageChanges
import com.jovines.lbsshare.event.ViewPagerPositionEvent
import com.jovines.lbsshare.ui.EditActivity
import com.jovines.lbsshare.viewmodel.MainViewModel
import kotlinx.android.synthetic.main.fragment_friend.*
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode
import org.jetbrains.anko.support.v4.startActivity

class FriendFragment : BaseViewModelFragment<MainViewModel>() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return DataBindingUtil.inflate<FragmentFriendBinding>(
            inflater,
            R.layout.fragment_friend,
            container,
            false
        ).apply {
            appViewModel =
                App.getAppViewModelProvider(this@FriendFragment)[AppViewModel::class.java]
        }.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val recentNewsAdapter = RecentNewsAdapter(viewModel.latestNewsFromNearby)

        friend_rec.adapter = recentNewsAdapter
        friend_rec.layoutManager = LinearLayoutManager(requireContext())

        iv_edit.setOnClickListener { startActivity<EditActivity>() }

        circleImageView.setOnClickListener { EventBus.getDefault().post(ViewPagerPositionEvent(1)) }

        swipe_refresh.setOnRefreshListener {
            viewModel.searchForNewsNearby()
        }

        viewModel.latestNewsFromNearby.observe(
            viewLifecycleOwner,
            Observer {
                swipe_refresh.isRefreshing = false
                recentNewsAdapter.notifyDataSetChanged()
            })
        viewModel.searchForNewsNearby()
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    fun sendAMessageYourself(changes: PrivateMessageChanges) {
        viewModel.searchForNewsNearby()
    }

    override val viewModelClass = MainViewModel::class.java
}
