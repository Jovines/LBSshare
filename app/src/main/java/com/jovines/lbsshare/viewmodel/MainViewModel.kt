package com.jovines.lbsshare.viewmodel

import androidx.databinding.ObservableField
import com.amap.api.services.weather.LocalWeatherForecastResult
import com.amap.api.services.weather.LocalWeatherLiveResult
import com.amap.api.services.weather.WeatherSearch
import com.amap.api.services.weather.WeatherSearchQuery
import com.jovines.lbsshare.base.viewmodel.BaseViewModel

/**
 * @author Jovines
 * @create 2020-04-11 4:27 PM
 *
 * 描述:
 *   MainActivity的ViewModel
 */

class MainViewModel : BaseViewModel() {

    lateinit var mWeatherSearch: WeatherSearch

    val placeData = ObservableField("")
    val weatherData = ObservableField("")

    fun requestWeather() {
        mWeatherSearch.setOnWeatherSearchListener(object : WeatherSearch.OnWeatherSearchListener {
            override fun onWeatherLiveSearched(liveResult: LocalWeatherLiveResult?, p1: Int) {
                liveResult?.liveResult?.apply {
                    placeData.set("重庆")
                    weatherData.set("$weather ${temperature}℃")
                }
            }

            override fun onWeatherForecastSearched(p0: LocalWeatherForecastResult?, p1: Int) {
            }
        })
        mWeatherSearch.query = WeatherSearchQuery("重庆", WeatherSearchQuery.WEATHER_TYPE_LIVE)
        mWeatherSearch.searchWeatherAsyn() //异步搜索
    }

}