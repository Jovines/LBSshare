package com.jovines.lbsshare.ui

import android.content.Context
import android.graphics.BitmapFactory
import android.graphics.Rect
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.databinding.ObservableInt
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.ItemDecoration
import com.amap.api.maps.CameraUpdateFactory
import com.amap.api.maps.model.BitmapDescriptorFactory
import com.amap.api.maps.model.CameraPosition
import com.amap.api.maps.model.LatLng
import com.amap.api.maps.model.MarkerOptions
import com.bumptech.glide.Glide
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.jovines.lbsshare.App
import com.jovines.lbsshare.R
import com.jovines.lbsshare.adapter.ViewHolder
import com.jovines.lbsshare.bean.CardMessageReturn
import com.jovines.lbsshare.databinding.DialogMainBottomSheetDetailedMessageBinding
import com.jovines.lbsshare.network.Api.BASE_PICTURE_URI
import com.jovines.lbsshare.utils.LatLonUtil.getDistance
import kotlinx.android.synthetic.main.dialog_main_bottom_sheet_detailed_message.*
import org.jetbrains.anko.dip
import java.text.DecimalFormat


/**
 * @author Jovines
 * @create 2020-04-30 10:10 AM
 *
 * description:
 *
 */
object DialogHelper {

    fun foundDetailDialog(
        context: Context,
        messageReturn: CardMessageReturn
    ): BottomSheetDialog {
        //由于要使用地图，所以这采用了重写的方式
        val dialog = object : BottomSheetDialog(context, R.style.BottomSheetDialogTheme) {
            override fun show() {
                dialog_detail_mapView.onCreate(null)
                super.show()
            }

            override fun dismiss() {
                dialog_detail_mapView.onDestroy()
                super.dismiss()
            }
        }
        //对dialog中的控件进行初始化
        dialog.apply {
            val binding = DataBindingUtil.inflate<DialogMainBottomSheetDetailedMessageBinding>(
                LayoutInflater.from(context), R.layout.dialog_main_bottom_sheet_detailed_message,
                window?.decorView as ViewGroup,
                false
            )
            setContentView(binding.root)
            val observableInt = ObservableInt(View.GONE)
            binding.positioningDisplay = observableInt
            dialog_detail_mapView.apply {
                map.uiSettings.apply {
                    setLogoBottomMargin(-100)
                    isScaleControlsEnabled = false
                    isScrollGesturesEnabled = false
                    isZoomControlsEnabled = false
                    isZoomGesturesEnabled = false
                }
            }
            dialog_detail_recycler_view.adapter = object : RecyclerView.Adapter<ViewHolder>() {
                override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) = ViewHolder(
                    LayoutInflater.from(parent.context)
                        .inflate(R.layout.recycle_detail_message, parent, false)
                )

                override fun getItemCount() = 5
                override fun onBindViewHolder(holder: ViewHolder, position: Int) {}
            }
            dialog_detail_recycler_view.layoutManager =
                LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
            dialog_detail_recycler_view.addItemDecoration(object : ItemDecoration() {
                override fun getItemOffsets(
                    outRect: Rect,
                    view: View,
                    parent: RecyclerView,
                    state: RecyclerView.State
                ) {
                    super.getItemOffsets(outRect, view, parent, state)
                    outRect.right = -view.dip(10)
                }
            })
            detailed_user_name.text = messageReturn.nickname
            user_description.text = messageReturn.description
            if (messageReturn.avatar?.isNotBlank() == true)
                Glide.with(context).load("${BASE_PICTURE_URI}/${messageReturn.avatar}")
                    .into(iv_detail_message_user)
            details_title.text = messageReturn.title
            details_content.text = messageReturn.content
            val dis = getDistance(
                App.user.lon!!,
                App.user.lat!!,
                messageReturn.lon!!,
                messageReturn.lat!!
            ).toInt()
            //设置这条消息距离当前用户多少米
            details_distance.text = if (dis < 1000) "${dis}m"
            else
                DecimalFormat("#.#E0").format(dis).replace(Regex("E.+"), "km")
            //如果该消息有位置信息
            if (messageReturn.lat != null && messageReturn.lon != null) {
                observableInt.set(View.VISIBLE)
                val mCameraUpdate = CameraUpdateFactory.newCameraPosition(
                    CameraPosition(LatLng(messageReturn.lat!!, messageReturn.lon!!), 13f, 0f, 0f)
                )
                dialog_detail_mapView.map.moveCamera(mCameraUpdate)
                dialog_detail_mapView.map.addMarker(MarkerOptions().apply {
                    icon(
                        BitmapDescriptorFactory.fromBitmap(
                            BitmapFactory.decodeResource(
                                context.resources,
                                R.drawable.location_of_details
                            )
                        )
                    )
                    position(
                        LatLng(App.user.lat!!, App.user.lon!!)
                    )
                })
            }
        }
        return dialog
    }

}