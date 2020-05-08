package com.jovines.lbsshare.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.jovines.lbsshare.R
import com.jovines.lbsshare.network.Api
import kotlinx.android.synthetic.main.recycle_personal_picture_display.view.*

/**
 * @author jon
 * @create 2020-05-07 6:36 PM
 *
 * 描述:
 *
 */
class PersonalPicturesAdapter(private val dataList: List<String>) :
    RecyclerView.Adapter<ViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) = ViewHolder(
        LayoutInflater.from(parent.context)
            .inflate(R.layout.recycle_personal_picture_display, parent, false)
    )

    override fun getItemCount() = dataList.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.itemView.apply {
            Glide.with(this).load("${Api.BASE_PICTURE_URI}/${dataList[position]}")
                .into(iv_personal_pictures)
        }
    }
}