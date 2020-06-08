package com.jovines.lbsshare.ui.main

import android.os.Bundle
import androidx.databinding.DataBindingUtil
import com.jovines.lbsshare.App
import com.jovines.lbsshare.AppViewModel
import com.jovines.lbsshare.R
import com.jovines.lbsshare.adapter.MainViewPagerFragmentAdapter
import com.jovines.lbsshare.base.BaseViewModelActivity
import com.jovines.lbsshare.databinding.ActivityMainBindingImpl
import com.jovines.lbsshare.event.ViewPagerPositionEvent
import com.jovines.lbsshare.viewmodel.MainViewModel
import kotlinx.android.synthetic.main.activity_main.*
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode


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
        nav_view.setOnNavigationItemSelectedListener { item ->
            when (item.itemId) {
                R.id.navigation_story -> main_viewpager.setCurrentItem(0, false)
                R.id.navigation_notifications -> main_viewpager.setCurrentItem(1, false)
                R.id.navigation_mine -> main_viewpager.setCurrentItem(2, false)
            }
            item.isChecked = true
            false
        }
        main_viewpager.adapter = MainViewPagerFragmentAdapter(this)
//        main_viewpager.isUserInputEnabled = false
//        main_viewpager.setOnTouchListener { v, event -> false }
    }

    override fun onBackPressed() {
        moveTaskToBack(true)
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    fun acceptViewPagerPosition(viewPagerPositionEvent: ViewPagerPositionEvent) {
        nav_view.selectedItemId = R.id.navigation_mine
    }
}
