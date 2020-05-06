package com.jovines.lbsshare.adapter

import android.view.Gravity
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.FrameLayout
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.jovines.lbs_server.entity.UserBean
import com.jovines.lbsshare.R
import com.jovines.lbsshare.network.Api
import org.jetbrains.anko.dip


/**
 * @author Jovines
 * @create 2020-05-05 6:58 PM
 *
 * description:
 *
 */
class NewsActiveUsersAdapter(private val dataList: List<UserBean>) :
    RecyclerView.Adapter<ViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) =
        if (viewType == Type.Content.ordinal)
            ViewHolder(
                LayoutInflater.from(parent.context)
                    .inflate(R.layout.recycle_detail_message, parent, false)
            )
        else
            ViewHolder(
                TextView(parent.context).apply {
                    layoutParams = FrameLayout.LayoutParams(FrameLayout.LayoutParams.WRAP_CONTENT,FrameLayout.LayoutParams.MATCH_PARENT).apply {
                        marginStart = dip(20)
                    }
                    gravity = Gravity.CENTER
                    setTextColor(0xff828282.toInt())
                }
            )

    override fun getItemCount() = if (dataList.size > 5) 6 else dataList.size +1

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        if (getItemViewType(position) == Type.Content.ordinal) {
            Glide.with(holder.itemView).load("${Api.BASE_PICTURE_URI}/${dataList[position].avatar}")
                .into(holder.itemView as ImageView)
        } else {
            (holder.itemView as TextView).apply {
                val str = if (dataList.size>5) "+${dataList.size - 5} Friends was here"
                else "was here"
                text = str
            }
        }
    }

    override fun getItemViewType(position: Int): Int {
        return if (dataList.size > 5) {
            if (position in 0..4) Type.Content.ordinal else Type.Tail.ordinal
        } else {
            if (position in dataList.indices) Type.Content.ordinal else Type.Tail.ordinal
        }
    }

    enum class Type {
        Content, Tail
    }
}