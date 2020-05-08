package com.jovines.lbsshare.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.lifecycle.MutableLiveData
import androidx.recyclerview.widget.RecyclerView
import com.jovines.lbsshare.R
import com.jovines.lbsshare.bean.PremiumUsersReturn

/**
 * @author Jovines
 * @create 2020-04-30 11:16 AM
 *
 * description:
 *
 */
class MainHighQualityUsersAdapter(private val liveData: MutableLiveData<List<PremiumUsersReturn>>) :
    RecyclerView.Adapter<ViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) =
        ViewHolder(
            LayoutInflater.from(parent.context)
                .inflate(R.layout.recycle_high_quality_users_item, parent, false)
        )

    override fun getItemCount() = liveData.value?.size?:0

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

    }
}