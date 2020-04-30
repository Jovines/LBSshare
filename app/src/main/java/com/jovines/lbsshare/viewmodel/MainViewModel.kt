package com.jovines.lbsshare.viewmodel

import android.Manifest
import android.widget.Toast
import androidx.databinding.ObservableField
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.MutableLiveData
import com.amap.api.location.AMapLocation
import com.amap.api.location.AMapLocationClient
import com.amap.api.location.AMapLocationClientOption
import com.amap.api.services.weather.*
import com.jovines.lbsshare.App
import com.jovines.lbsshare.base.viewmodel.BaseViewModel
import com.jovines.lbsshare.utils.extensions.doPermissionAction


/**
 * @author Jovines
 * @create 2020-04-11 4:27 PM
 *
 * 描述:
 *   MainActivity的ViewModel
 */

class MainViewModel : BaseViewModel() {

    lateinit var mWeatherSearch: WeatherSearch

    val placeData = ObservableField("生活圈")
    val weatherData = ObservableField("天气就像心情一样")

    val aMapLocation = MutableLiveData<AMapLocation>()

    val weatherLive = MutableLiveData<LocalWeatherLive>()


    private fun requestWeather() {
        mWeatherSearch.setOnWeatherSearchListener(object : WeatherSearch.OnWeatherSearchListener {
            override fun onWeatherLiveSearched(liveResult: LocalWeatherLiveResult?, p1: Int) {
                liveResult?.liveResult?.apply {
                    weatherLive.value = this
                    placeData.set("${aMapLocation.value?.district}")
                    weatherData.set("$weather ${temperature}℃")
                }
            }

            override fun onWeatherForecastSearched(p0: LocalWeatherForecastResult?, p1: Int) {
            }
        })
        mWeatherSearch.query = WeatherSearchQuery(
            "${aMapLocation.value?.district}",
            WeatherSearchQuery.WEATHER_TYPE_LIVE
        )
        mWeatherSearch.searchWeatherAsyn() //异步搜索
    }

    fun requestLocation(activity: FragmentActivity) {
        activity.doPermissionAction(
            Manifest.permission.ACCESS_COARSE_LOCATION,
            Manifest.permission.ACCESS_FINE_LOCATION,
            Manifest.permission.WRITE_EXTERNAL_STORAGE,
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.READ_PHONE_STATE
        ) {
            reason = "生活圈是基于定位功能，所以我们必须取得您的定位权限"
            doAfterGranted {
                //初始化定位
                val locationClient = AMapLocationClient(App.context)
                //设置定位回调监听
                locationClient.setLocationListener {
                    it?.apply {
                        aMapLocation.value = this
                        requestWeather()
                    }
                }
                locationClient.setLocationOption(AMapLocationClientOption().apply {
                    locationPurpose = AMapLocationClientOption.AMapLocationPurpose.SignIn
                    this.isOnceLocation = true
                })
                //设置场景模式后最好调用一次stop，再调用start以保证场景模式生效
                locationClient.stopLocation()
                locationClient.startLocation()
            }
            doAfterRefused {
                Toast.makeText(activity, "抱歉，没有取得相关权限，某些功能可能残缺", Toast.LENGTH_SHORT).show()
            }
        }

    }
}