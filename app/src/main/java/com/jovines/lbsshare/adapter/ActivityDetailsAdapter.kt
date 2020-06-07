package com.jovines.lbsshare.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.lifecycle.ComputableLiveData
import androidx.lifecycle.LiveData
import androidx.recyclerview.widget.RecyclerView
import com.jovines.lbsshare.R
import com.jovines.lbsshare.bean.ActivityBean
import com.jovines.lbsshare.bindingadapter.UserAdapter
import kotlinx.android.synthetic.main.recycle_discount_item.view.*
import java.text.SimpleDateFormat
import java.util.*

/**
 * Created by Jovines on 2020/06/06 19:47
 * description :
 */
class ActivityDetailsAdapter(val liveData: LiveData<List<ActivityBean>>): RecyclerView.Adapter<ViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.recycle_discount_item,parent,false))
    }


    override fun getItemCount() = liveData.value?.size ?: 0

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.itemView.apply {
            liveData.value?.get(position)?.let { activityBean ->
                UserAdapter.getImageUri(iv_discount_front,activityBean.frontCover)
                tv_discount_title.text = activityBean.activityName
                tv_discount_create_at.text = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.CHINA).format(activityBean.time!!)
            }
        }
    }
}