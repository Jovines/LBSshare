package com.jovines.lbsshare.ui.main.fragment.foud

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import com.jovines.lbsshare.R
import com.jovines.lbsshare.adapter.ActivityDetailsAdapter
import com.jovines.lbsshare.adapter.GlideImageLoader
import com.jovines.lbsshare.base.BaseFragment
import com.jovines.lbsshare.network.Api
import com.jovines.lbsshare.viewmodel.FoundViewModel
import com.youth.banner.BannerConfig
import com.youth.banner.Transformer
import kotlinx.android.synthetic.main.fragment_vp_discount.*

/**
 * 活动页面，包括最新和最热两个方面
 */

class ActivityFragment : BaseFragment() {


    companion object {
        const val newest = "最新"
        const val hottest = "最热"
    }

    lateinit var viewModel:FoundViewModel


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_vp_discount, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel = ViewModelProvider(requireActivity())[FoundViewModel::class.java]

        initFragment()
    }

    private fun initFragment() {
        bn_discount.apply {
            setImageLoader(GlideImageLoader())
            setBannerAnimation(Transformer.DepthPage)
            viewModel.getBannerPics {
                val tmp = it.map { "${Api.BASE_PICTURE_URI}/${it.frontcover}" }
                setImages(tmp)
                start()
            }
            setIndicatorGravity(BannerConfig.CENTER)
        }
        rv_activity_page.adapter = ActivityDetailsAdapter(viewModel.eventsList)
    }

}