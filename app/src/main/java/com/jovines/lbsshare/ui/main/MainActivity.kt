package com.jovines.lbsshare.ui.main

import android.os.Bundle
import com.amap.api.services.weather.WeatherSearch
import com.amap.api.services.weather.WeatherSearchQuery
import com.jovines.lbsshare.R
import com.jovines.lbsshare.adapter.MainFragmentAdapter
import com.jovines.lbsshare.base.BaseViewModelActivity
import com.jovines.lbsshare.ui.StartUpActivity
import com.jovines.lbsshare.viewmodel.MainViewModel
import kotlinx.android.synthetic.main.activity_main.*


class MainActivity : BaseViewModelActivity<MainViewModel>() {
    override val viewModelClass = MainViewModel::class.java

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
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
        main_bottom_navigation_view.setOnNavigationItemSelectedListener {
            when (it.itemId){
                R.id.main_find-> main_view_pager.setCurrentItem(0,false)
                R.id.main_square-> main_view_pager.setCurrentItem(1,false)
                R.id.main_mine-> main_view_pager.setCurrentItem(2,false)
            }
            true
        }
        viewModel.requestWeather()
    }

    override fun onBackPressed() {
        moveTaskToBack(true)
    }
}
