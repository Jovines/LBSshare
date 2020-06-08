package com.jovines.lbsshare.adapter

import android.content.Intent
import android.util.Log
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.ViewGroup
import androidx.lifecycle.MutableLiveData
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.jovines.lbsshare.R
import com.jovines.lbsshare.bean.CardMessageReturn
import com.jovines.lbsshare.network.Api.BASE_PICTURE_URI
import com.jovines.lbsshare.network.ApiGenerator
import com.jovines.lbsshare.network.UserApiService
import com.jovines.lbsshare.ui.ArticleDetailsActivity
import com.jovines.lbsshare.utils.extensions.errorHandler
import com.jovines.lbsshare.utils.extensions.setSchedulers
import kotlinx.android.synthetic.main.viewpager_latest_news_item.view.*

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
                tv_article_title.text = messageReturn.title
                tv_time.text = messageReturn.time?.substringAfter("-")
                tv_comment_num.text = messageReturn.commentCount.toString()
                number_of_personal_message_accesses.text =
                    (messageReturn.checkCount ?: 0).toString()
                tv_article_content.text = messageReturn.content
                personal_picture_display.adapter = PersonalPicturesAdapter(
                    Gson().fromJson(
                        messageReturn.images ?: "[]",
                        object : TypeToken<List<String>>() {}.type
                    )
                )
                setOnClickListener {
                    context.startActivity(
                        Intent(
                            context,
                            ArticleDetailsActivity::class.java
                        ).apply {
                            this.putExtra(ArticleDetailsActivity.DETAILED_DATA, messageReturn)
                        })
//                    foundDetailDialog(context, messageReturn).show()
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