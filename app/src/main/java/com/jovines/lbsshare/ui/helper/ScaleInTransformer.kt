package com.jovines.lbsshare.ui.helper

import android.view.View
import androidx.viewpager2.widget.ViewPager2
import kotlin.math.abs

/**
 * @author Jovines
 * @create 2020-04-30 1:02 AM
 *
 * description:主页附近信息推荐ViewPager，缩放
 *
 */
class ScaleInTransformer : ViewPager2.PageTransformer {
    override fun transformPage(page: View, position: Float) {
        val pageHeight: Int = page.height
        val abs = abs(position)
        page.translationY = -(pageHeight / 7f) * (1 - abs)
    }
}