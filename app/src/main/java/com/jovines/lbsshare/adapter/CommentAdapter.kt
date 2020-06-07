package com.jovines.lbsshare.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.recyclerview.widget.RecyclerView
import com.afollestad.materialdialogs.MaterialDialog
import com.jovines.lbs_server.entity.Comment
import com.jovines.lbsshare.R
import com.jovines.lbsshare.bindingadapter.UserAdapter
import com.jovines.lbsshare.network.ApiGenerator
import com.jovines.lbsshare.network.UserApiService
import com.jovines.lbsshare.utils.extensions.errorHandler
import com.jovines.lbsshare.utils.extensions.setSchedulers
import kotlinx.android.synthetic.main.item_article_comment.view.*
import kotlinx.android.synthetic.main.viewpager_latest_news_item.view.*

/**
 * Created by chenyang
 * on 20-6-7
 */
class CommentAdapter(val datas: LiveData<List<Comment>>) : RecyclerView.Adapter<ViewHolder>() {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) = ViewHolder(
        LayoutInflater.from(parent.context).inflate(R.layout.item_article_comment, parent, false)
    )


    override fun getItemCount() = (datas.value?.size  ?: 0 ) + 1

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        if (position == datas.value?.size?:0 -1) {
            holder.itemView.visibility = View.INVISIBLE
            return
        }else{
            holder.itemView.visibility = View.VISIBLE
        }
        val bean = datas.value?.get(position)
        if (bean != null) {

            holder.itemView.apply {

                tv_article_coment_username.text = bean.userNickName

                UserAdapter.getImageUri(iv_article_comment_avator, bean.avatar)

                tv_article_comment_content.text = bean.content

                tv_article_comment_date.text = bean.time

                if (bean.isSelf) {
                    tv_artical_comment_delete.visibility  = View.VISIBLE
                    tv_artical_comment_delete.setOnClickListener {
                        MaterialDialog.Builder(context)
                            .title("是否确定删除？")
                            .content("删除以后，不可恢复，请谨慎操作")
                            .positiveText("确定")
                            .onPositive { dialog, which ->


                                val service = ApiGenerator.getApiService(UserApiService::class.java)

                                bean.id?.let { it1 ->
                                    service.deleteComment(commentId = it1)
                                        .setSchedulers()
                                        .errorHandler()
                                        .subscribe({notifyItemRemoved(position)}, {})
                                }

                                dialog.dismiss()

                            }
//                            .onNegative { dialog, which ->
//                                dialog.dismiss()
//                            }
                            .negativeText("取消")
                            .show()
                    }
                } else {
                    tv_artical_comment_delete.visibility = View.GONE
                }
            }
        }

    }


}