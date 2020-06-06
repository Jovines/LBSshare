package com.jovines.lbsshare.ui.main

import android.os.Bundle
import android.view.View
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.viewpager2.widget.CompositePageTransformer
import androidx.viewpager2.widget.MarginPageTransformer
import com.amap.api.services.weather.WeatherSearch
import com.jovines.lbsshare.App
import com.jovines.lbsshare.AppViewModel
import com.jovines.lbsshare.R
import com.jovines.lbsshare.adapter.MainHighQualityUsersAdapter
import com.jovines.lbsshare.adapter.MainViewPagerFragmentAdapter
import com.jovines.lbsshare.adapter.RecentNewsAdapter
import com.jovines.lbsshare.base.BaseViewModelActivity
import com.jovines.lbsshare.bean.CardMessageReturn
import com.jovines.lbsshare.databinding.ActivityMainBindingImpl
import com.jovines.lbsshare.ui.EditActivity
import com.jovines.lbsshare.ui.MineActivity
import com.jovines.lbsshare.ui.StartUpActivity
import com.jovines.lbsshare.utils.extensions.getStatusBarHeight
import com.jovines.lbsshare.utils.extensions.gone
import com.jovines.lbsshare.utils.extensions.visible
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
        mainBinding.appViewModel = App.getAppViewModelProvider(this)[AppViewModel::class.java]
        initActivity()
    }

    private fun initActivity() {
//        viewModel.mWeatherSearch = WeatherSearch(this)
//        main_button.setOnClickListener {
//            signOut()
//            startActivity<StartUpActivity>()
//            finish()
//        }
//        //底部消息设置
//        val recentNewsAdapter = RecentNewsAdapter(viewModel.latestNewsFromNearby)
//        main_view_pager_recent_news.adapter = recentNewsAdapter
//        main_view_pager_recent_news.offscreenPageLimit = 2
//        val compositePageTransformer = CompositePageTransformer()
//        compositePageTransformer.addTransformer(MarginPageTransformer(dip(10)))
//        main_view_pager_recent_news.setPageTransformer(compositePageTransformer)
//
//        val mainHighQualityUsersAdapter = MainHighQualityUsersAdapter(viewModel.highQualityUsers)
//        recycle_high_quality_account.adapter = mainHighQualityUsersAdapter
//        viewModel.highQualityUsers.observe(this, Observer {
//            if (it.isNotEmpty()) {
//                mainHighQualityUsersAdapter.notifyDataSetChanged()
//                recycle_high_quality_account.visible()
//            } else {
//                recycle_high_quality_account.gone()
//            }
//            more_quality_users.visibility = if (it.size > 5) View.VISIBLE else View.GONE
//        })
        nav_view.setOnNavigationItemSelectedListener { item ->
            when (item.itemId) {
                R.id.navigation_story -> main_viewpager.setCurrentItem(0,false)
                R.id.navigation_notifications -> main_viewpager.setCurrentItem(1,false)
                R.id.navigation_mine -> main_viewpager.setCurrentItem(2,false)
            }
            false
        }
        main_viewpager.adapter = MainViewPagerFragmentAdapter(this)

//        main_viewpager.
//        viewModel.getPremiumUsers()
//        (found_toolbar.layoutParams as ConstraintLayout.LayoutParams).apply {
//            found_toolbar.topPadding += getStatusBarHeight()
//            height += getStatusBarHeight()
//        }
//        iv_edit.setOnClickListener { startActivity<EditActivity>() }
//
//        user_profile.setOnClickListener {
//            startActivity<MineActivity>()
//        }


//        var lastTimeDataList = viewModel.latestNewsFromNearby.value
//        viewModel.latestNewsFromNearby.observe(
//            this,
//            Observer { cardList: List<CardMessageReturn>? ->
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
//                } else recentNewsAdapter.notifyDataSetChanged()
//                lastTimeDataList = viewModel.latestNewsFromNearby.value
//                prompt_in_loading.gone()
//            })
//        viewModel.requestLocation()
    }

    override fun onBackPressed() {
        moveTaskToBack(true)
    }
}
