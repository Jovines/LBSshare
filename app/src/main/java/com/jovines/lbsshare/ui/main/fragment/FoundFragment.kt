package com.jovines.lbsshare.ui.main.fragment

import android.Manifest
import android.graphics.BitmapFactory
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.amap.api.maps.AMap
import com.amap.api.maps.CameraUpdateFactory
import com.amap.api.maps.model.BitmapDescriptorFactory
import com.amap.api.maps.model.MyLocationStyle
import com.jovines.lbsshare.R
import com.jovines.lbsshare.databinding.FragmentFoundBindingImpl
import com.jovines.lbsshare.utils.extensions.doPermissionAction
import com.jovines.lbsshare.viewmodel.MainViewModel
import kotlinx.android.synthetic.main.fragment_found.*


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
        requireActivity().doPermissionAction(
            Manifest.permission.ACCESS_COARSE_LOCATION,
            Manifest.permission.ACCESS_FINE_LOCATION,
            Manifest.permission.WRITE_EXTERNAL_STORAGE,
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.READ_PHONE_STATE
        ) {
            reason = "生活圈是基于定位功能，所以我们必须取得您的定位权限"
            doAfterGranted {
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
                aMap.moveCamera(CameraUpdateFactory.zoomTo(12.5F))
                mainViewModel.aMapLocation.observe(viewLifecycleOwner, Observer {
                    it?.apply {

                    }
                })
                mainViewModel.requestLocation(aMap)
            }

            doAfterRefused {
                Toast.makeText(requireContext(), "抱歉，没有取得相关权限，某些功能可能残缺", Toast.LENGTH_SHORT).show()
            }
        }
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
