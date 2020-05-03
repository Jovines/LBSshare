package com.jovines.lbsshare.ui

import android.content.Context
import android.graphics.Rect
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.ItemDecoration
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.jovines.lbsshare.R
import com.jovines.lbsshare.adapter.ViewHolder
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

    fun foundDetailDialog(context: Context): BottomSheetDialog {
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
        dialog.setContentView(
            LayoutInflater.from(context).inflate(
                R.layout.dialog_main_bottom_sheet_detailed_message,
                dialog.window?.decorView as ViewGroup,
                false
            )
        )
        dialog.dialog_detail_mapView.apply {
            map.uiSettings.apply {
                setLogoBottomMargin(-100)
                isScaleControlsEnabled = false
                isScrollGesturesEnabled = false
                isZoomControlsEnabled = false
                isZoomGesturesEnabled = false
            }
        }
        dialog.dialog_detail_recycler_view.adapter = object : RecyclerView.Adapter<ViewHolder>() {
            override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) = ViewHolder(
                LayoutInflater.from(parent.context)
                    .inflate(R.layout.recycle_detail_message, parent, false)
            )

            override fun getItemCount() = 5
            override fun onBindViewHolder(holder: ViewHolder, position: Int) {}
        }
        dialog.dialog_detail_recycler_view.layoutManager =
            LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
        dialog.dialog_detail_recycler_view.addItemDecoration(object : ItemDecoration() {
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
        return dialog
    }

}