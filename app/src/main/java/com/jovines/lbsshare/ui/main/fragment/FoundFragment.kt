package com.jovines.lbsshare.ui.main.fragment

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Canvas
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.amap.api.maps.AMap
import com.amap.api.maps.AMap.OnMarkerClickListener
import com.amap.api.maps.CameraUpdateFactory
import com.amap.api.maps.model.BitmapDescriptorFactory
import com.amap.api.maps.model.LatLng
import com.amap.api.maps.model.MarkerOptions
import com.amap.api.maps.model.MyLocationStyle
import com.jovines.lbsshare.R
import com.jovines.lbsshare.databinding.FragmentFoundBindingImpl
import com.jovines.lbsshare.viewmodel.MainViewModel
import kotlinx.android.synthetic.main.fragment_found.*
import kotlinx.android.synthetic.main.map_user_locator.view.*


class FoundFragment : Fragment() {

    lateinit var mainViewModel: MainViewModel

    private lateinit var aMap: AMap

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        mainViewModel = ViewModelProvider(requireActivity())[MainViewModel::class.java]
        val bindingImpl: FragmentFoundBindingImpl =
            DataBindingUtil.inflate(inflater, R.layout.fragment_found, container, false)
        bindingImpl.mainViewModel = mainViewModel
        return bindingImpl.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        map.onCreate(savedInstanceState)
        initFragment()
    }

    private fun initFragment() {
        aMap = map.map
        aMap.uiSettings.setLogoBottomMargin(-100)
        aMap.uiSettings.isZoomControlsEnabled = false
        aMap.uiSettings.isMyLocationButtonEnabled = true

        //设置定位蓝点的Style
        aMap.myLocationStyle = MyLocationStyle().apply {
            myLocationType(MyLocationStyle.LOCATION_TYPE_LOCATE)//只定位一次。
            val fromResource = BitmapDescriptorFactory.fromBitmap(
                BitmapFactory.decodeResource(
                    resources,
                    R.drawable.ic_user_own_locator
                )
            )
            myLocationIcon(fromResource)
            strokeColor(0x00000000)
            radiusFillColor(0x00000000)
        }
        //定位自己的位置
        aMap.isMyLocationEnabled = true
        aMap.moveCamera(CameraUpdateFactory.zoomTo(14F))
        mainViewModel.aMapLocation.observe(viewLifecycleOwner, Observer {
            it?.apply {
                val latLong = LatLng(latitude, longitude)
                val markerOption = MarkerOptions()
                val viewConversionBitmap = viewConversionBitmap {
                    tv_map_user_nickname.text = "你好"
                }
                markerOption.icon(BitmapDescriptorFactory.fromBitmap(viewConversionBitmap))
                markerOption.position(latLong)
                aMap.addMarker(markerOption)
                // 定义 Marker 点击事件监听
                val markerClickListener =
                    OnMarkerClickListener { marker ->
                        // marker 对象被点击时回调的接口
                        // 返回 true 则表示接口已响应事件，否则返回false
                        // if (marker.title == "公司") ToastHelper.ShowToast("哈哈", context)
                        false
                    }
                // 绑定 Marker 被点击事件
                aMap.setOnMarkerClickListener(markerClickListener)
            }
        })
    }

    /**
     * view转bitmap
     */
    private fun viewConversionBitmap(function: View.() -> Unit): Bitmap {
        val view = FrameLayout(requireContext())
        LayoutInflater.from(requireContext()).inflate(R.layout.map_user_locator, view)
        view.apply(function)
        val width = resources.getDimension(R.dimen.user_mark_width).toInt()
        val height = resources.getDimension(R.dimen.user_mark_height).toInt()
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

    override fun onDestroyView() {
        super.onDestroyView()
        map.onDestroy()
    }

    override fun onResume() {
        super.onResume()
        map.onResume()
    }

    override fun onPause() {
        super.onPause()
        map.onPause()
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        map.onSaveInstanceState(outState)
    }
}
