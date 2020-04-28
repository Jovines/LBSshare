package com.jovines.lbsshare.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.jovines.lbsshare.R

/**
 * @author Jovines
 * @create 2020-04-28 7:20 PM
 *
 * description:
 *
 */
class FoundRecycleAdapter(val dataList: List<String>) : RecyclerView.Adapter<ViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) =
        if (viewType == Type.HEAD.ordinal)
            ViewHolder(View(parent.context).apply {
                val dimension =
                    parent.context.resources.getDimension(R.dimen.gradient_height).toInt()
                layoutParams =
                    ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, dimension / 3)
            })
        else ViewHolder(
            LayoutInflater.from(parent.context).inflate(R.layout.recycle_foud_item, parent, false)
        )

    override fun getItemCount() = dataList.size + 1
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
    }

    override fun getItemViewType(position: Int) =
        if (position == 0) {
            Type.HEAD.ordinal
        } else Type.CONTENT.ordinal

    enum class Type {
        HEAD, CONTENT
    }
}