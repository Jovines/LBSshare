package com.jovines.lbsshare.ui

import android.graphics.Rect
import android.os.Bundle
import android.view.View
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.jovines.lbsshare.R
import com.jovines.lbsshare.adapter.CommentAdapter
import com.jovines.lbsshare.adapter.DetailedInformationAdapter
import com.jovines.lbsshare.adapter.NewsActiveUsersAdapter
import com.jovines.lbsshare.adapter.PictureViewAdapter
import com.jovines.lbsshare.base.BaseViewModelActivity
import com.jovines.lbsshare.bean.CardMessageReturn
import com.jovines.lbsshare.databinding.ActivityArticleDetailsBindingImpl
import com.jovines.lbsshare.network.ApiGenerator
import com.jovines.lbsshare.network.UserApiService
import com.jovines.lbsshare.utils.extensions.errorHandler
import com.jovines.lbsshare.utils.extensions.gone
import com.jovines.lbsshare.utils.extensions.setSchedulers
import com.jovines.lbsshare.utils.extensions.visible
import com.jovines.lbsshare.viewmodel.ArticleDetailsViewModel
import kotlinx.android.synthetic.main.activity_article_details.*
import org.jetbrains.anko.dip
import org.jetbrains.anko.toast

class ArticleDetailsActivity : BaseViewModelActivity<ArticleDetailsViewModel>() {


    companion object {
        const val DETAILED_DATA = "DETAILED_DATA"

    }

    lateinit var binding: ActivityArticleDetailsBindingImpl

    private val adapter: CommentAdapter by lazy { CommentAdapter(viewModel.comments) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(
            this,
            R.layout.activity_article_details
        )
        binding.isPictureShow = viewModel.isPictureShow
        initActivity()
    }


    private fun initActivity() {
        val messageReturn = intent.getSerializableExtra(DETAILED_DATA) as CardMessageReturn
        binding.messageReturn = messageReturn
        dialog_detail_recycler_view.layoutManager =
            LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)
        dialog_detail_recycler_view.addItemDecoration(object : RecyclerView.ItemDecoration() {
            override fun getItemOffsets(
                outRect: Rect,
                view: View,
                parent: RecyclerView,
                state: RecyclerView.State
            ) {
                super.getItemOffsets(outRect, view, parent, state)
                outRect.right = -view.dip(10)
            }
        })

        if (messageReturn.images?.isNotBlank() == true) {
            recycle_detailed_information.visible()
            val dataList = Gson().fromJson<List<String>>(
                messageReturn.images ?: "[]",
                object : TypeToken<List<String>>() {}.type
            )
            vp2_picture_display.adapter = PictureViewAdapter(dataList)

            recycle_detailed_information.adapter = DetailedInformationAdapter(
                dataList,
                vp2_picture_display,
                viewModel.isPictureShow
            )
        }

        //获取用户活跃数据
        messageReturn.id?.let {
            ApiGenerator.getApiService(UserApiService::class.java).getNewsActiveUsers(
                it
            ).setSchedulers()
                .errorHandler().subscribe {
                    if (it?.data?.isEmpty() == true) dialog_detail_recycler_view.gone()
                    dialog_detail_recycler_view.adapter = NewsActiveUsersAdapter(it.data)
                }
        }

        rv_artical_comment.adapter = adapter
        viewModel.comments.observe(this, Observer {
            adapter.notifyDataSetChanged()
        })
        messageReturn.id?.let { viewModel.queryComments(it) }


        btn_send_comment.setOnClickListener {
            val msg = et_comment.text.toString()
            if (msg.isNotBlank()) {
                viewModel.addComment(msg, messageReturn.id ?: 0, { code ->
                    if (code != 1000) {
                        toast("添加评论失败！")
                    } else {
                        messageReturn.id?.let { it1 -> viewModel.queryComments(it1) }
                        et_comment.setText("")
                    }
                })
            }
        }


    }

    override val viewModelClass: Class<ArticleDetailsViewModel>
        get() = ArticleDetailsViewModel::class.java
}
