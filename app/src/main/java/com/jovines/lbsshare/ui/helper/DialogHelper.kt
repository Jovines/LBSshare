package com.jovines.lbsshare.ui.helper

import android.content.Context
import com.afollestad.materialdialogs.MaterialDialog


/**
 * @author Jovines
 * @create 2020-04-30 10:10 AM
 *
 * description:
 *
 */
object DialogHelper {


    fun articleRelease(context: Context): MaterialDialog = MaterialDialog.Builder(context)
        .progress(true, 100)
        .content("文章发布中...")
        .cancelable(false).build()
}