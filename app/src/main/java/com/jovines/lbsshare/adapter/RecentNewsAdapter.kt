package com.jovines.lbsshare.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.jovines.lbsshare.R
import com.jovines.lbsshare.ui.DialogHelper.foundDetailDialog

/**
 * @author Jovines
 * @create 2020-04-28 7:20 PM
 *
 * description:
 *
 */
class RecentNewsAdapter(val dataList: List<String>) : RecyclerView.Adapter<ViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) =
        ViewHolder(
            LayoutInflater.from(parent.context)
                .inflate(R.layout.viewpager_latest_news_item, parent, false)
        )

    override fun getItemCount() = dataList.size
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.itemView.apply {
            setOnClickListener {
                foundDetailDialog(context).show()
            }
        }
    }
}