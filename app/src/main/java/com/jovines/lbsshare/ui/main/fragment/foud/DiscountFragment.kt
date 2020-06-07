package com.jovines.lbsshare.ui.main.fragment.foud

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import com.google.android.material.tabs.TabLayoutMediator
import com.jovines.lbsshare.R
import com.jovines.lbsshare.adapter.ActivityAdapter
import com.jovines.lbsshare.adapter.ActivityDetailsAdapter
import com.jovines.lbsshare.adapter.GlideImageLoader
import com.jovines.lbsshare.base.BaseFragment
import com.jovines.lbsshare.viewmodel.FoundViewModel
import com.youth.banner.BannerConfig
import com.youth.banner.Transformer
import kotlinx.android.synthetic.main.fragment_vp_discount.*

class DiscountFragment : BaseFragment() {


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
            //先模拟数据
            setImages(
                listOf<String>(
                    "https://pic4.zhimg.com/v2-14a66ba1f82f784a19e55d6b8b6d59e5_1200x500.jpg"
                    ,
                    "https://pic2.zhimg.com/80/v2-b476507f26b533c0029753f7e5d681bd_hd.png"
                    ,
                    "https://pic3.zhimg.com/80/v2-49b142b4e32d484b9e47527acaafe0fa_hd.jpg"
                    ,
                    "https://pic3.zhimg.com/80/e806d9a78ccf50a6ac4409932a70c67d_hd.jpg"
                )
            )
            setIndicatorGravity(BannerConfig.CENTER)
            start()
        }
        recy_offer_page.adapter = ActivityDetailsAdapter(viewModel.eventsList)
    }

}