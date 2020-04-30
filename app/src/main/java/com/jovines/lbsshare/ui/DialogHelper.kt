package com.jovines.lbsshare.ui

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.jovines.lbsshare.R
import kotlinx.android.synthetic.main.dialog_main_bottom_sheet_detailed_message.*

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
        return dialog
    }

}