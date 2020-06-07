package com.jovines.lbsshare.ui.main.fragment

import android.Manifest
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
import com.jovines.lbsshare.bean.CardMessageReturn
import com.jovines.lbsshare.databinding.FragmentFriendBinding
import com.jovines.lbsshare.event.PrivateMessageChanges
import com.jovines.lbsshare.event.ViewPagerPositionEvent
import com.jovines.lbsshare.ui.EditActivity
import com.jovines.lbsshare.utils.extensions.doPermissionAction
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
            appViewModel = App.getAppViewModelProvider(this@FriendFragment)[AppViewModel::class.java]
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
//            doPermissionAction(
//                Manifest.permission.ACCESS_COARSE_LOCATION,
//                Manifest.permission.ACCESS_FINE_LOCATION,
//                Manifest.permission.WRITE_EXTERNAL_STORAGE,
//                Manifest.permission.READ_EXTERNAL_STORAGE,
//                Manifest.permission.READ_PHONE_STATE
//            ) {
//                doAfterGranted {
//                    viewModel.updateLifeCircleNews()
//                }
//            }
            viewModel.searchForNewsNearby()
        }

        var lastTimeDataList = viewModel.latestNewsFromNearby.value
        viewModel.latestNewsFromNearby.observe(
            viewLifecycleOwner,
            Observer { cardList: List<CardMessageReturn>? ->
                swipe_refresh.isRefreshing = false
//                if (lastTimeDataList != null) {
//                    val cardListIdMap = cardList?.map { it.id }
//                    val map = lastTimeDataList?.map { it.id }
//                    //处理更改的
//                    lastTimeDataList?.filter { messageReturn ->
//                        val find = cardList?.find { it.id == messageReturn.id }
//                        if (find != null)
//                            find.avatar != messageReturn.avatar || find.description != messageReturn.description || find.nickname != messageReturn.nickname
//                        else false
//                    }?.forEach {
//                        recentNewsAdapter.notifyItemChanged(lastTimeDataList!!.indexOf(it))
//                    }
//                    //去掉消失的
//                    lastTimeDataList?.filterNot { cardListIdMap?.contains(it.id) ?: false }
//                        ?.forEach {
//                            recentNewsAdapter.notifyItemRemoved(lastTimeDataList!!.indexOf(it))
//                        }
//                    //添加新增的
//                    cardList?.filterNot { map?.contains(it.id) ?: false }?.apply {
//                        recentNewsAdapter.notifyItemRangeInserted(0, size)
//                    }
//                } else
                    recentNewsAdapter.notifyDataSetChanged()
                lastTimeDataList = viewModel.latestNewsFromNearby.value
            })
//        viewModel.requestLocation()
        viewModel.searchForNewsNearby()
    }

    override fun onStart() {
        super.onStart()
//        doPermissionAction(
//            Manifest.permission.ACCESS_COARSE_LOCATION,
//            Manifest.permission.ACCESS_FINE_LOCATION,
//            Manifest.permission.WRITE_EXTERNAL_STORAGE,
//            Manifest.permission.READ_EXTERNAL_STORAGE,
//            Manifest.permission.READ_PHONE_STATE
//        ) {
//            doAfterGranted {
//                viewModel.updateLifeCircleNews()
//            }
//        }
//        swipe_refresh.isRefreshing = true
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    fun sendAMessageYourself(changes: PrivateMessageChanges) {
        viewModel.searchForNewsNearby()
    }

    override val viewModelClass = MainViewModel::class.java
}
