package com.jovines.lbsshare.ui.main

import android.os.Bundle
import android.view.View
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.databinding.DataBindingUtil
import androidx.viewpager2.widget.CompositePageTransformer
import androidx.viewpager2.widget.MarginPageTransformer
import androidx.viewpager2.widget.ViewPager2
import com.amap.api.services.weather.WeatherSearch
import com.jovines.lbsshare.R
import com.jovines.lbsshare.adapter.MainFragmentAdapter
import com.jovines.lbsshare.adapter.RecentNewsAdapter
import com.jovines.lbsshare.base.BaseViewModelActivity
import com.jovines.lbsshare.databinding.ActivityMainBindingImpl
import com.jovines.lbsshare.ui.StartUpActivity
import com.jovines.lbsshare.ui.helper.ScaleInTransformer
import com.jovines.lbsshare.utils.extensions.getStatusBarHeight
import com.jovines.lbsshare.viewmodel.MainViewModel
import kotlinx.android.synthetic.main.activity_main.*
import org.jetbrains.anko.dip
import org.jetbrains.anko.topPadding
import kotlin.math.abs


class MainActivity : BaseViewModelActivity<MainViewModel>() {
    override val viewModelClass = MainViewModel::class.java

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val mainBinding =
            DataBindingUtil.setContentView<ActivityMainBindingImpl>(this, R.layout.activity_main)
        mainBinding.mainViewModel = viewModel
        initActivity()
    }

    private fun initActivity() {
        viewModel.mWeatherSearch = WeatherSearch(this)
        main_view_pager.adapter = MainFragmentAdapter(this)
        main_view_pager.isUserInputEnabled = false
        main_button.setOnClickListener {
            signOut()
            startActivity<StartUpActivity>()
            finish()
        }
        //底部消息设置
        main_view_pager_recent_news.adapter = RecentNewsAdapter(
            listOf(
                "",
                "",
                "",
                "",
                "",
                "",
                "",
                "",
                "",
                "",
                "",
                "",
                "",
                "",
                "",
                ""
            )
        )
        main_view_pager_recent_news.offscreenPageLimit = 2
        val compositePageTransformer = CompositePageTransformer()
        compositePageTransformer.addTransformer(MarginPageTransformer(dip(10)))
        main_view_pager_recent_news.setPageTransformer(compositePageTransformer)

        (found_toolbar.layoutParams as ConstraintLayout.LayoutParams).apply {
            found_toolbar.topPadding += getStatusBarHeight()
            height += getStatusBarHeight()
        }
        viewModel.requestLocation(this)
    }

    override fun onBackPressed() {
        moveTaskToBack(true)
    }
}
