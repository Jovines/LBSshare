package com.jovines.lbsshare.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.jovines.lbsshare.R
import com.jovines.lbsshare.network.Api

/**
 * @author jon
 * @create 2020-05-07 7:56 PM
 *
 * 描述:
 *
 */
class PictureViewAdapter(val dataList: List<String>) : RecyclerView.Adapter<ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) = ViewHolder(
        LayoutInflater.from(parent.context).inflate(R.layout.viewpager_picture_view,parent,false)
    )

    override fun getItemCount() = dataList.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        Glide.with(holder.itemView).load("${Api.BASE_PICTURE_URI}/${dataList[position]}")
            .into(holder.itemView as ImageView)
    }
}