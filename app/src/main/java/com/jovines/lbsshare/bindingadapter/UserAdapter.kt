package com.jovines.lbsshare.bindingadapter

import android.widget.ImageView
import androidx.databinding.BindingAdapter
import com.bumptech.glide.Glide
import com.jovines.lbsshare.network.Api

/**
 * @author jon
 * @create 2020-05-01 9:21 AM
 *
 * 描述:
 *
 */
object UserAdapter {

    @JvmStatic
    @BindingAdapter(value = ["imageUri"])
    fun getImageUri(imageView: ImageView, uri: String?) {
        if (uri != null && uri.isNotEmpty())
            Glide.with(imageView).load("${Api.BASE_PICTURE_URI}/$uri").into(imageView)
    }
}