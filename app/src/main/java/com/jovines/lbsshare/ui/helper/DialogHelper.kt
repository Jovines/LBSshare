package com.jovines.lbsshare.ui.helper

import android.content.Context
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Rect
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import androidx.databinding.DataBindingUtil
import androidx.databinding.ObservableInt
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.ItemDecoration
import com.afollestad.materialdialogs.MaterialDialog
import com.amap.api.maps.CameraUpdateFactory
import com.amap.api.maps.model.BitmapDescriptorFactory
import com.amap.api.maps.model.CameraPosition
import com.amap.api.maps.model.LatLng
import com.amap.api.maps.model.MarkerOptions
import com.bumptech.glide.Glide
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.jovines.lbsshare.App
import com.jovines.lbsshare.R
import com.jovines.lbsshare.adapter.DetailedInformationAdapter
import com.jovines.lbsshare.adapter.NewsActiveUsersAdapter
import com.jovines.lbsshare.adapter.PictureViewAdapter
import com.jovines.lbsshare.adapter.ViewHolder
import com.jovines.lbsshare.bean.CardMessageReturn
import com.jovines.lbsshare.databinding.DialogMainBottomSheetDetailedMessageBinding
import com.jovines.lbsshare.network.Api.BASE_PICTURE_URI
import com.jovines.lbsshare.network.ApiGenerator
import com.jovines.lbsshare.network.UserApiService
import com.jovines.lbsshare.utils.LatLonUtil
import com.jovines.lbsshare.utils.extensions.gone
import com.jovines.lbsshare.utils.extensions.setSchedulers
import com.jovines.lbsshare.utils.extensions.visible
import com.jovines.lbsshare.utils.kmDecimalFormatString
import kotlinx.android.synthetic.main.dialog_main_bottom_sheet_detailed_message.*
import org.jetbrains.anko.dip


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
            val observableInt1 = ObservableInt(View.GONE)
            binding.isPictureShow = observableInt1

            dialog_detail_mapView.apply {
                map.uiSettings.apply {
                    setLogoBottomMargin(-100)
                    isScaleControlsEnabled = false
                    isScrollGesturesEnabled = false
                    isZoomControlsEnabled = false
                    isZoomGesturesEnabled = false
                }
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
            user_name_bottom.text = messageReturn.nickname
            detailed_user_name.text = messageReturn.nickname
            user_description.text = messageReturn.description
            details_recycling_label.setOnClickListener {
                dialog.dismiss()
            }
            if (messageReturn.images?.isNotBlank() == true) {
                recycle_detailed_information.visible()
                val dataList = Gson().fromJson<List<String>>(
                    messageReturn.images ?: "[]",
                    object : TypeToken<List<String>>() {}.type
                )
                vp2_picture_display.adapter = PictureViewAdapter(dataList)

                recycle_detailed_information.adapter = DetailedInformationAdapter(
                    dataList,
                    vp2_picture_display,
                    observableInt1
                )

            }
            hide_picture.setOnClickListener { observableInt1.set(View.GONE) }
            if (messageReturn.avatar?.isNotBlank() == true)
                Glide.with(context).load("${BASE_PICTURE_URI}/${messageReturn.avatar}")
                    .into(iv_detail_message_user)
            details_title.text = messageReturn.title
            details_content.text = messageReturn.content

            //获取用户活跃数据
            messageReturn.id?.let {
                ApiGenerator.getApiService(UserApiService::class.java).getNewsActiveUsers(
                    it
                ).setSchedulers().subscribe {
                    if (it?.data?.isEmpty() == true) dialog_detail_recycler_view.gone()
                    dialog_detail_recycler_view.adapter = NewsActiveUsersAdapter(it.data)
                }
            }
            val dis = LatLonUtil.getDistanceTwoM(
                App.user.lon!!,
                App.user.lat!!,
                messageReturn.lon!!,
                messageReturn.lat!!
            )
            //设置这条消息距离当前用户多少米
            details_distance.text = kmDecimalFormatString(dis.toInt())
            //如果该消息有位置信息
            if (messageReturn.lat != null && messageReturn.lon != null) {
                observableInt.set(View.VISIBLE)
                val mCameraUpdate = CameraUpdateFactory.newCameraPosition(
                    CameraPosition(LatLng(messageReturn.lat!!, messageReturn.lon!!), 13f, 0f, 0f)
                )
                dialog_detail_mapView.map.moveCamera(mCameraUpdate)
                dialog_detail_mapView.map.addMarker(MarkerOptions().apply {
                    val value = context.resources.getDimension(R.dimen.detail_map_size).toInt()
                    val bitmap = Bitmap.createBitmap(value, value, Bitmap.Config.ARGB_8888)
                    //将view转为bitmap
                    LayoutInflater.from(context)
                        .inflate(R.layout.map_location_of_details, FrameLayout(context), true)
                        .apply {
                            val canvas = Canvas(bitmap)
                            measure(
                                View.MeasureSpec.makeMeasureSpec(value, View.MeasureSpec.EXACTLY),
                                View.MeasureSpec.makeMeasureSpec(
                                    dip(
                                        value
                                    ), View.MeasureSpec.EXACTLY
                                )
                            )
                            layout(0, 0, value, value)
                            draw(canvas)
                        }
                    //设置转换完成的bitmap
                    icon(
                        BitmapDescriptorFactory.fromBitmap(bitmap)
                    )
                    position(
                        LatLng(messageReturn.lat!!, messageReturn.lon!!)
                    )
                })
            }
        }
        return dialog
    }

    fun articleRelease(context: Context): MaterialDialog = MaterialDialog.Builder(context)
        .progress(true, 100)
        .content("文章发布中...")
        .cancelable(false).build()
}