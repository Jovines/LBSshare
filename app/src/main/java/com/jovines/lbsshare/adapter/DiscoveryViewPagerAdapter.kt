package com.jovines.lbsshare.adapter

import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentStatePagerAdapter
import com.jovines.lbsshare.ui.main.fragment.foud.ActivityFragment
import com.jovines.lbsshare.ui.main.fragment.foud.ArticleFragment

class DiscoveryViewPagerAdapter(fm: FragmentManager) :
    FragmentStatePagerAdapter(fm, BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT) {

    override fun getItem(position: Int) = when (position) {
        0 -> ArticleFragment()
        else -> ActivityFragment()
    }

    override fun getCount() = 2

    override fun getPageTitle(position: Int) = when (position) {
        0 -> "精品文章"
        else -> "活动"
    }
}