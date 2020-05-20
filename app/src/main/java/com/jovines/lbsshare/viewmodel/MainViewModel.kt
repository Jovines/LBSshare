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
import com.jovines.lbsshare.bean.UserBean
import com.jovines.lbsshare.App
import com.jovines.lbsshare.R
import com.jovines.lbsshare.base.viewmodel.BaseViewModel
import com.jovines.lbsshare.bean.CardMessageReturn
import com.jovines.lbsshare.bean.PremiumUsersReturn
import com.jovines.lbsshare.bean.StatusWarp
import com.jovines.lbsshare.network.Api
import com.jovines.lbsshare.network.ApiGenerator
import com.jovines.lbsshare.network.UserApiService
import com.jovines.lbsshare.utils.extensions.errorHandler
import com.jovines.lbsshare.utils.extensions.setSchedulers
import io.reactivex.Observable
import io.reactivex.ObservableSource
import io.reactivex.Observer
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.map_user_locator.view.*
import java.util.concurrent.TimeUnit


/**
 * @author Jovines
 * @create 2020-04-11 4:27 PM
 *
 * 描述:
 *   MainActivity的ViewModel
 */

class MainViewModel : BaseViewModel() {

    lateinit var aMap: AMap


    lateinit var mWeatherSearch: WeatherSearch

    val placeData = ObservableField("生活圈")

    val weatherData = ObservableField("天气就像心情一样")

    //位置
    val aMapLocation = MutableLiveData<AMapLocation>()

    //天气
    val weatherLive = MutableLiveData<LocalWeatherLive>()

    val latestNewsFromNearby = MutableLiveData<List<CardMessageReturn>>()


    val highQualityUsers = MutableLiveData<List<PremiumUsersReturn>>()


    private val userApiService: UserApiService =
        ApiGenerator.getApiService(UserApiService::class.java)


    val listMarkerOptions = mutableListOf<Pair<UserBean, MarkerOptions>>()

    /**
     * 请求现在的天气
     */
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

    /**
     * 请求现在的位置
     */
    fun requestLocation() {
        //初始化定位
        val locationClient = AMapLocationClient(App.context)
        //设置定位回调监听
        locationClient.setLocationListener {
            it?.apply {
                //成功获得当前位置，并进行下一步请求
                aMapLocation.value = this
                //更新用户当前位置
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


    fun getPremiumUsers() {
        userApiService.getPremiumUsers()
            .setSchedulers()
            .errorHandler()
            .subscribe({
                highQualityUsers.value = it.data
            }, {}).isDisposed
    }

    private fun addMarkerToMap(
        bean: UserBean,
        viewConversionBitmap: Bitmap
    ) {
        val latLong = LatLng(bean.lat!!, bean.lon!!)
        val markerOption = MarkerOptions()
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
        listMarkerOptions.add(Pair(bean, markerOption))
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


    var disposable: Disposable? = null

    fun updateLifeCircleNews() {
        disposable = Observable.concatArray(ObservableSource<Long> {
            //一进app就安排一次事件发送
            it.onNext(-1)
            it.onComplete()
        }, Observable.interval(2, TimeUnit.SECONDS))
            .subscribeOn(Schedulers.io())
            .flatMap { //初始化定位
                Observable.create<AMapLocation> { emitter ->
                    val locationClient = AMapLocationClient(App.context)
                    //设置定位回调监听
                    locationClient.setLocationListener {
                        it?.apply {
                            //成功获得当前位置，并进行下一步请求
                            aMapLocation.value = this
                            App.user.lat = latitude
                            App.user.lon = longitude
                            emitter.onNext(it)
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
            }
            .observeOn(Schedulers.io())
            .flatMap {
                Observable.merge(
                    //上传位置
                    userApiService.updateLocation(it.latitude, it.longitude),
                    //寻找附近的人并更新显示
                    findActiveUsers(),
                    searchForNewsNearby()
                )
            }
            .observeOn(AndroidSchedulers.mainThread())
            .errorHandler()
            .subscribe({
            }, {})
    }

    private fun searchForNewsNearby() = userApiService.findLatestNewsNearby()
        .setSchedulers()
        .doOnNext {
            latestNewsFromNearby.value = it.data
        }


    private fun findActiveUsers() = userApiService.findNearby()
        .subscribeOn(Schedulers.io())
        .observeOn(AndroidSchedulers.mainThread())
        //删除离开了生活圈或者信息发生变化的
        .doOnNext { statusWarp: StatusWarp<List<UserBean>>? ->
            val filterNot = listMarkerOptions
                .filterNot { pair: Pair<UserBean, MarkerOptions> ->
                    val find = statusWarp?.data?.find { it.phone == pair.first.phone }
                    find != null && find.avatar == pair.first.avatar && pair.first.nickname == find.nickname
                }
            val list = filterNot.map { it.second }
            list.forEach { options: MarkerOptions ->
                aMap.mapScreenMarkers.find { it.options == options }?.apply {
                    remove()
                }
            }
            listMarkerOptions.removeAll(filterNot)
        }
        .observeOn(Schedulers.io())
        //将消息再逐个发送出去
        .flatMap { statusWarp: StatusWarp<List<UserBean>> ->
            ObservableSource { observer: Observer<in UserBean> ->
                statusWarp.data.forEach {
                    observer.onNext(it)
                }
                observer.onComplete()
            }
        }
        .observeOn(AndroidSchedulers.mainThread())
        //挑选出符合添加要求的数据
        .filter { bean ->
            val find =
                listMarkerOptions.find { pair -> bean.phone == pair.first.phone }
            //并将需要更新位置的更新位置
            find?.apply {
                aMap.mapScreenMarkers.find { it.options == second }
                    ?.apply {
                        if (position.latitude != bean.lat || position.longitude != bean.lon) {
                            position = LatLng(bean.lat!!, bean.lon!!)
                        }
                    }
            }
            bean.lat != null && bean.lon != null && find == null
        }
        .observeOn(Schedulers.io())
        .map { userBean: UserBean ->
            val submit = if (userBean.avatar?.isNotEmpty() == true) {
                Glide.with(App.context)
                    .asBitmap()
                    .load("${Api.BASE_PICTURE_URI}/${userBean.avatar}")
                    .submit().get()
            } else null
            Pair(userBean, submit)
        }
        .observeOn(AndroidSchedulers.mainThread())
        .doOnNext { pair ->
            val bean = pair.first
            val viewConversionBitmap = viewConversionBitmap {
                tv_map_user_nickname.text = bean.nickname
                pair.second?.let {
                    iv_map_user_portrait.setImageBitmap(it)
                }
            }
            addMarkerToMap(bean, viewConversionBitmap)

        }
}