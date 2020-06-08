package com.jovines.lbsshare.ui.main.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import com.jovines.lbsshare.R
import com.jovines.lbsshare.adapter.DiscoveryViewPagerAdapter
import com.jovines.lbsshare.base.BaseFragment
import com.jovines.lbsshare.databinding.FragmentFoundBindingImpl
import com.jovines.lbsshare.viewmodel.MainViewModel
import kotlinx.android.synthetic.main.fragment_found.*


class FoundFragment : BaseFragment() {

    lateinit var mainViewModel: MainViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        mainViewModel = ViewModelProvider(requireActivity())[MainViewModel::class.java]
        val bindingImpl: FragmentFoundBindingImpl =
            DataBindingUtil.inflate(inflater, R.layout.fragment_found, container, false)
        bindingImpl.mainViewModel = mainViewModel
        return bindingImpl.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initFragment()
    }

    private fun initFragment() {
        vp_discovery.adapter = DiscoveryViewPagerAdapter(childFragmentManager)
        tl_discovery.setupWithViewPager(vp_discovery)
    }

}
