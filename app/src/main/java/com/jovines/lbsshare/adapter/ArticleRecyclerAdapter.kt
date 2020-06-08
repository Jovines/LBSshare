package com.jovines.lbsshare.adapter

import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.lifecycle.MutableLiveData
import androidx.recyclerview.widget.RecyclerView
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.jovines.lbsshare.R
import com.jovines.lbsshare.bean.CardMessageReturn
import com.jovines.lbsshare.bindingadapter.UserAdapter
import com.jovines.lbsshare.network.ApiGenerator
import com.jovines.lbsshare.network.UserApiService
import com.jovines.lbsshare.ui.ArticleDetailsActivity
import com.jovines.lbsshare.utils.extensions.errorHandler
import com.jovines.lbsshare.utils.extensions.gone
import com.jovines.lbsshare.utils.extensions.setSchedulers
import com.jovines.lbsshare.utils.extensions.visible
import kotlinx.android.synthetic.main.recycle_article_item.view.*

class ArticleRecyclerAdapter(private val liveData: MutableLiveData<List<CardMessageReturn>>) :
    RecyclerView.Adapter<ViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            LayoutInflater.from(parent.context)
                .inflate(R.layout.recycle_article_item, parent, false)
        )
    }

    override fun getItemCount() = liveData.value?.size ?: 0

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.itemView.apply {
            liveData.value?.get(position)?.let { messageReturn ->
                val imageList = Gson().fromJson<List<String>>(
                    messageReturn.images ?: "[]",
                    object : TypeToken<List<String>>() {}.type
                )
                if (imageList.isNotEmpty()) {
                    container_iv_item_pic.visible()
                    UserAdapter.getImageUri(iv_item_pic, imageList[0])
                } else {
                    container_iv_item_pic.gone()
                }
                tv_item_title.text = messageReturn.title
                tv_item_time.text = messageReturn.time
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
