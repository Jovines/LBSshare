package com.jovines.lbsshare.ui.main

import android.content.Intent
import android.os.Bundle
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.viewpager2.widget.CompositePageTransformer
import androidx.viewpager2.widget.MarginPageTransformer
import com.amap.api.services.weather.WeatherSearch
import com.jovines.lbsshare.R
import com.jovines.lbsshare.adapter.MainHighQualityUsersAdapter
import com.jovines.lbsshare.adapter.RecentNewsAdapter
import com.jovines.lbsshare.base.BaseViewModelActivity
import com.jovines.lbsshare.databinding.ActivityMainBindingImpl
import com.jovines.lbsshare.ui.EditActivity
import com.jovines.lbsshare.ui.StartUpActivity
import com.jovines.lbsshare.utils.extensions.getStatusBarHeight
import com.jovines.lbsshare.viewmodel.MainViewModel
import kotlinx.android.synthetic.main.activity_main.*
import org.jetbrains.anko.dip
import org.jetbrains.anko.topPadding


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
        main_button.setOnClickListener {
            signOut()
            startActivity<StartUpActivity>()
            finish()
        }
        //底部消息设置
        val recentNewsAdapter = RecentNewsAdapter(viewModel.latestNewsFromNearby)
        main_view_pager_recent_news.adapter = recentNewsAdapter
        main_view_pager_recent_news.offscreenPageLimit = 2
        val compositePageTransformer = CompositePageTransformer()
        compositePageTransformer.addTransformer(MarginPageTransformer(dip(10)))
        main_view_pager_recent_news.setPageTransformer(compositePageTransformer)

        recycle_high_quality_account.adapter = MainHighQualityUsersAdapter(List(10) { "" })

        (found_toolbar.layoutParams as ConstraintLayout.LayoutParams).apply {
            found_toolbar.topPadding += getStatusBarHeight()
            height += getStatusBarHeight()
        }
        iv_edit.setOnClickListener { startActivity<EditActivity>() }

        user_profile.setOnClickListener {
            val intent = Intent()
            intent.type = "image/*"
            intent.action = "android.intent.action.PICK"
            intent.addCategory("android.intent.category.DEFAULT")
            startActivityForResult(intent, EditActivity.PICTURE_SELECTION)
        }

        viewModel.latestNewsFromNearby.observe(this, Observer {
            recentNewsAdapter.notifyDataSetChanged()
        })
    }


    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        when (requestCode) {
            EditActivity.PICTURE_SELECTION -> {
                data?.data?.let { viewModel.changeHeadImage(it) }
            }
        }

    }

    override fun onBackPressed() {
        moveTaskToBack(true)
    }
}
