package com.jovines.lbsshare.ui.main.fragment.foud

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import com.jovines.lbsshare.R
import com.jovines.lbsshare.adapter.ArticleRecyclerAdapter
import com.jovines.lbsshare.base.BaseFragment
import com.jovines.lbsshare.viewmodel.FoundViewModel
import kotlinx.android.synthetic.main.fragment_vp_ariticle.*

class ArticleFragment : BaseFragment() {

    lateinit var viewModel: FoundViewModel

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_vp_ariticle, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel = ViewModelProvider(requireActivity())[FoundViewModel::class.java]
        initFragment()
    }

    private fun initFragment() {
        val articleRecyclerAdapter = ArticleRecyclerAdapter(viewModel.qualityUserArticles)
        rv_list_article.adapter = articleRecyclerAdapter
        sl_article.setOnRefreshListener {
            upData(articleRecyclerAdapter)
        }
        upData(articleRecyclerAdapter)
        sl_article.isRefreshing = true
    }

    private fun upData(adapter:ArticleRecyclerAdapter){
        viewModel.getQualityUserArticles(){
            sl_article.isRefreshing = false
            adapter.notifyDataSetChanged()
        }
    }
}