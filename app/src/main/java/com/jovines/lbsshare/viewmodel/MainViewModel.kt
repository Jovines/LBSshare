package com.jovines.lbsshare.viewmodel

import android.graphics.Bitmap
import android.graphics.Canvas
import android.view.LayoutInflater
import android.view.View
import android.widget.FrameLayout
import androidx.databinding.ObservableField
import androidx.lifecycle.MutableLiveData
import com.amap.api.location.AMapLocation
import com.amap.api.location.AMapLocationClient
import com.amap.api.location.AMapLocationClientOption
import com.amap.api.maps.AMap
import com.amap.api.maps.model.BitmapDescriptorFactory
import com.amap.api.maps.model.LatLng
import com.amap.api.maps.model.MarkerOptions
import com.amap.api.services.weather.*
import com.bumptech.glide.Glide
import com.jovines.lbs_server.entity.UserBean
import com.jovines.lbsshare.App
import com.jovines.lbsshare.R
import com.jovines.lbsshare.base.viewmodel.BaseViewModel
import com.jovines.lbsshare.bean.CardMessageReturn
import com.jovines.lbsshare.network.Api
import com.jovines.lbsshare.network.ApiGenerator
import com.jovines.lbsshare.network.UserApiService
import com.jovines.lbsshare.utils.ExecuteOnceObserver
import com.jovines.lbsshare.utils.extensions.setSchedulers
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.map_user_locator.view.*
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody
import okhttp3.RequestBody.Companion.asRequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import java.io.File


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

    //位置
    val aMapLocation = MutableLiveData<AMapLocation>()

    //天气
    val weatherLive = MutableLiveData<LocalWeatherLive>()

    /**
     * 头像的链接
     */
    val avatar = ObservableField<String>(App.user.avatar)

    val latestNewsFromNearby = MutableLiveData<List<CardMessageReturn>>()

    private val userApiService: UserApiService =
        ApiGenerator.getApiService(UserApiService::class.java)


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

    fun requestLocation(aMap: AMap) {
        //初始化定位
        val locationClient = AMapLocationClient(App.context)
        //设置定位回调监听
        locationClient.setLocationListener {
            it?.apply {
                //成功获得当前位置，并进行下一步请求
                aMapLocation.value = this
                //更新用户当前位置
                updateLocation(latitude, longitude, aMap)
                //更新天气
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

    fun changeHeadImage(file: File) {
        val mapParams = mutableMapOf<String, RequestBody>()
        mapParams["phone"] =
            "${App.user.phone}".toRequestBody("multipart/form-data".toMediaTypeOrNull())
        mapParams["password"] =
            App.user.password.toRequestBody("multipart/form-data".toMediaTypeOrNull())
        val fileBody: RequestBody = file.asRequestBody("image/*".toMediaTypeOrNull())
        val createFormData = MultipartBody.Part.createFormData("image", file.name, fileBody)
        userApiService.changeAvatar(createFormData, mapParams)
            .setSchedulers()
            .subscribe(ExecuteOnceObserver {
                it?.data?.let { userBean ->
                    App.user = userBean
                    avatar.set(userBean.avatar)
                }
            })
    }

    /**
     * 更新当前用户位置
     */
    private fun updateLocation(lat: Double, lon: Double, aMap: AMap) {
        userApiService.updateLocation(lon, lat)
            .setSchedulers()
            .subscribe(ExecuteOnceObserver {
                it?.data?.let { userBean ->
                    App.user = userBean
                    //当前位置更新成功，寻找附近的人,和附近最新消息
                    findNearby(aMap)
                    lookingForNewsNearby()
                }
            })
    }

    fun lookingForNewsNearby() {
        userApiService.findLatestNewsNearby(500000, 24)
            .setSchedulers()
            .subscribe(ExecuteOnceObserver {
                latestNewsFromNearby.value = it.data
            })
    }


    /**
     * 找附近的人并将他们显示到地图上
     */
    private fun findNearby(aMap: AMap) {
        val subscribe = userApiService.findNearby(500000)
            .subscribeOn(Schedulers.io())
            .flatMap { data ->
                Observable.create<Pair<UserBean, Bitmap?>> { observer ->
                    data.data.forEach { userBean ->
                        if (userBean.avatar?.isNotEmpty() == true) {
                            val submit = Glide.with(App.context)
                                .asBitmap()
                                .load("${Api.BASE_PICTURE_URI}/${userBean.avatar}")
                                .submit().get()
                            observer.onNext(Pair(userBean, submit))
                        } else {
                            observer.onNext(Pair(userBean, null))
                        }
                    }
                }
            }
            .filter { it.first.lat != null && it.first.lon != null }
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe {
                val latLong = LatLng(it.first.lat!!, it.first.lon!!)
                val markerOption = MarkerOptions()
                val viewConversionBitmap = viewConversionBitmap {
                    tv_map_user_nickname.text = it.first.nickname
                    it.second?.let {
                        iv_map_user_portrait.setImageBitmap(it)
                    }
                }
                markerOption.icon(BitmapDescriptorFactory.fromBitmap(viewConversionBitmap))
                markerOption.position(latLong)
                aMap.addMarker(markerOption)
                // 定义 Marker 点击事件监听
                val markerClickListener =
                    AMap.OnMarkerClickListener { marker ->
                        // marker 对象被点击时回调的接口
                        // 返回 true 则表示接口已响应事件，否则返回false
                        // if (marker.title == "公司") ToastHelper.ShowToast("哈哈", context)
                        false
                    }
                // 绑定 Marker 被点击事件
                aMap.setOnMarkerClickListener(markerClickListener)
            }
    }

    /**
     * view转bitmap
     */
    private fun viewConversionBitmap(function: View.() -> Unit): Bitmap {
        val view = FrameLayout(App.context)
        LayoutInflater.from(App.context).inflate(R.layout.map_user_locator, view)
        view.apply(function)
        val width = App.context.resources.getDimension(R.dimen.user_mark_width).toInt()
        val height = App.context.resources.getDimension(R.dimen.user_mark_height).toInt()
        val bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888)
        val canvas = Canvas(bitmap)
        view.measure(
            View.MeasureSpec.makeMeasureSpec(width, View.MeasureSpec.EXACTLY),
            View.MeasureSpec.makeMeasureSpec(height, View.MeasureSpec.EXACTLY)
        )
        view.layout(0, 0, width, height)
        view.draw(canvas)
        return bitmap
    }
}