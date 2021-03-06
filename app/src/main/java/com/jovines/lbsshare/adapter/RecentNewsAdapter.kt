package com.jovines.lbsshare.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.MutableLiveData
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.jovines.lbsshare.App
import com.jovines.lbsshare.R
import com.jovines.lbsshare.bean.CardMessageReturn
import com.jovines.lbsshare.network.Api.BASE_PICTURE_URI
import com.jovines.lbsshare.network.ApiGenerator
import com.jovines.lbsshare.network.UserApiService
import com.jovines.lbsshare.ui.helper.DialogHelper.foundDetailDialog
import com.jovines.lbsshare.utils.LatLonUtil.getDistanceTwoKm
import com.jovines.lbsshare.utils.LatLonUtil.getDistanceTwoM
import com.jovines.lbsshare.utils.extensions.errorHandler
import com.jovines.lbsshare.utils.extensions.setSchedulers
import com.jovines.lbsshare.utils.kmDecimalFormatString
import kotlinx.android.synthetic.main.viewpager_latest_news_item.view.*
import kotlin.math.roundToInt

/**
 * @author Jovines
 * @create 2020-04-28 7:20 PM
 *
 * description:
 *
 */
class RecentNewsAdapter(private val liveData: MutableLiveData<List<CardMessageReturn>>) :
    RecyclerView.Adapter<ViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) =
        ViewHolder(
            LayoutInflater.from(parent.context)
                .inflate(R.layout.viewpager_latest_news_item, parent, false)
        )

    override fun getItemCount() = liveData.value?.size ?: 0
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.itemView.apply {
            liveData.value?.get(position)?.let { messageReturn ->
                if (messageReturn.avatar != null && messageReturn.avatar!!.isNotBlank())
                    Glide.with(holder.itemView.context)
                        .load("${BASE_PICTURE_URI}/${messageReturn.avatar}")
                        .into(circleImageView)
                else circleImageView.setImageResource(R.drawable.user_default_avatar_black)
                tv_user_name.text = messageReturn.nickname
                tv_user_description.text = messageReturn.description
                tv_article_title.text = messageReturn.title
                tv_article_content.text = messageReturn.content
                if (App.user.lon != null && App.user.lat != null && messageReturn.lon != null && messageReturn.lat != null) {
                    tv_article_distance.visibility = View.VISIBLE
                    val dis = getDistanceTwoM(
                        App.user.lon!!,
                        App.user.lat!!,
                        messageReturn.lon!!,
                        messageReturn.lat!!
                    )
                    tv_article_distance.text = kmDecimalFormatString(dis.roundToInt())
                } else tv_article_distance.visibility = View.GONE
                setOnClickListener {
                    foundDetailDialog(context, messageReturn).show()
                    messageReturn.id?.apply {
                        ApiGenerator.getApiService(UserApiService::class.java)
                            .addTimeVisited(this)
                            .errorHandler()
                            .setSchedulers()
                            .subscribe()
                    }
                }
            }
        }
    }
}