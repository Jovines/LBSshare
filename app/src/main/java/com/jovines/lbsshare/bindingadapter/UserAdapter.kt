package com.jovines.lbsshare.bindingadapter

import android.widget.ImageView
import android.widget.TextView
import androidx.databinding.BindingAdapter
import com.bumptech.glide.Glide
import com.jovines.lbsshare.R
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
        if (uri != null && uri.isNotBlank())
            Glide.with(imageView).load("${Api.BASE_PICTURE_URI}/$uri").into(imageView)
        else imageView.setImageResource(R.drawable.user_default_avatar_black)
    }

    @JvmStatic
    @BindingAdapter(value = ["android:description"])
    fun mineDescription(textView:TextView,description:String?){
        textView.text = if (description!=null&&description.isNotBlank()) description else "暂无描述"
    }
}