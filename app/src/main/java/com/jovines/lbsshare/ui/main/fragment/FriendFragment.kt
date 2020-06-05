package com.jovines.lbsshare.ui.main.fragment

import android.Manifest
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.jovines.lbsshare.R
import com.jovines.lbsshare.adapter.RecentNewsAdapter
import com.jovines.lbsshare.base.BaseViewModelFragment
import com.jovines.lbsshare.bean.CardMessageReturn
import com.jovines.lbsshare.utils.extensions.doPermissionAction
import com.jovines.lbsshare.viewmodel.MainViewModel
import kotlinx.android.synthetic.main.fragment_friend.*

class FriendFragment : BaseViewModelFragment<MainViewModel>() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_friend, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val recentNewsAdapter = RecentNewsAdapter(viewModel.latestNewsFromNearby)

        friend_rec.adapter = recentNewsAdapter
        friend_rec.layoutManager = LinearLayoutManager(requireContext())

        var lastTimeDataList = viewModel.latestNewsFromNearby.value
        viewModel.latestNewsFromNearby.observe(
            viewLifecycleOwner,
            Observer { cardList: List<CardMessageReturn>? ->
                if (lastTimeDataList != null) {
                    val cardListIdMap = cardList?.map { it.id }
                    val map = lastTimeDataList?.map { it.id }
                    //处理更改的
                    lastTimeDataList?.filter { messageReturn ->
                        val find = cardList?.find { it.id == messageReturn.id }
                        if (find != null)
                            find.avatar != messageReturn.avatar || find.description != messageReturn.description || find.nickname != messageReturn.nickname
                        else false
                    }?.forEach {
                        recentNewsAdapter.notifyItemChanged(lastTimeDataList!!.indexOf(it))
                    }
                    //去掉消失的
                    lastTimeDataList?.filterNot { cardListIdMap?.contains(it.id) ?: false }
                        ?.forEach {
                            recentNewsAdapter.notifyItemRemoved(lastTimeDataList!!.indexOf(it))
                        }
                    //添加新增的
                    cardList?.filterNot { map?.contains(it.id) ?: false }?.apply {
                        recentNewsAdapter.notifyItemRangeInserted(0, size)
                    }
                } else recentNewsAdapter.notifyDataSetChanged()
                lastTimeDataList = viewModel.latestNewsFromNearby.value
            })
        viewModel.requestLocation()
    }

    override fun onStart() {
        super.onStart()
        this.doPermissionAction(
            Manifest.permission.ACCESS_COARSE_LOCATION,
            Manifest.permission.ACCESS_FINE_LOCATION,
            Manifest.permission.WRITE_EXTERNAL_STORAGE,
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.READ_PHONE_STATE
        ) {
            doAfterGranted {
                viewModel.updateLifeCircleNews()
            }
        }
    }


    override fun onStop() {
        super.onStop()
        viewModel.disposable?.apply {
            if (!isDisposed) dispose()
        }
    }

    override val viewModelClass = MainViewModel::class.java
}
