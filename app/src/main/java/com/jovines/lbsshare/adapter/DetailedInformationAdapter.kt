package com.jovines.lbsshare.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.ObservableInt
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager2.widget.ViewPager2
import com.bumptech.glide.Glide
import com.jovines.lbsshare.R
import com.jovines.lbsshare.network.Api
import com.jovines.lbsshare.utils.extensions.visible
import kotlinx.android.synthetic.main.recycle_detailed_information_picture_display.view.*

/**
 * @author Jovines
 * @create 2020-05-05 3:49 PM
 *
 * description:
 *
 */
class DetailedInformationAdapter(
    val dataList: List<String>,
    val viewPager2: ViewPager2,
    val pictureShow: ObservableInt
) : RecyclerView.Adapter<ViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) = ViewHolder(
        LayoutInflater.from(parent.context)
            .inflate(R.layout.recycle_detailed_information_picture_display, parent, false)
    )

    override fun getItemCount() = if (dataList.size > 4) 4 else dataList.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.itemView.apply {
            Glide.with(this).load("${Api.BASE_PICTURE_URI}/${dataList[position]}")
                .into(details_publish_picture)
            if (dataList.size > 4 && position == 3) {
                details_publish_picture_mask.visible()
                mark.visible()
                picture_count.visible()
                val str = "+${dataList.size - 4}"
                picture_count.text = str
            }
            setOnClickListener {
                viewPager2.setCurrentItem(position,false)
                pictureShow.set(View.VISIBLE)
            }
        }
    }
}