package com.jovines.lbsshare.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.MutableLiveData
import androidx.recyclerview.widget.RecyclerView
import com.jovines.lbsshare.R
import com.jovines.lbsshare.bean.PremiumUsersReturn
import com.jovines.lbsshare.bindingadapter.UserAdapter
import com.jovines.lbsshare.ui.helper.DialogHelper
import kotlinx.android.synthetic.main.recycle_high_quality_users_item.view.*
import java.util.*

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

    override fun getItemCount() = liveData.value?.size ?: 0

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.itemView.apply {
            liveData.value?.get(position)?.let {
                UserAdapter.getImageUri(high_quality_user_image, it.avatar)
                high_quality_user_name.text = it.nickname
                new_high_quality_user_logo.visibility =
                    if (it.jointime.time > Calendar.getInstance()
                            .apply { add(Calendar.DAY_OF_YEAR, -10) }.timeInMillis
                    ) View.VISIBLE else View.GONE
                setOnClickListener { view ->
                    if (it.messages != null && it.messages!!.isNotEmpty()) {
                        val get = it.messages!!.get(0)
                        DialogHelper.foundDetailDialog(context, get).show()
                    } else {
                        Toast.makeText(context, "该推荐用户还未发布过消息", Toast.LENGTH_SHORT).show()
                    }
                }
            }
        }
    }
}