package com.jovines.lbsshare.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.jovines.lbsshare.R

/**
 * @author Jovines
 * @create 2020-04-30 11:16 AM
 *
 * description:
 *
 */
class MainHighQualityUsersAdapter(private val dataList: List<String>) :
    RecyclerView.Adapter<ViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) =
        ViewHolder(
            LayoutInflater.from(parent.context)
                .inflate(R.layout.recycle_high_quality_users_item, parent, false)
        )

    override fun getItemCount() = dataList.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
    }
}