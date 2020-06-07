package com.jovines.lbsshare.adapter

import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.jovines.lbsshare.ui.main.fragment.foud.ArticleFragment
import com.jovines.lbsshare.ui.main.fragment.foud.ActivityFragment

class DiscoveryViewPagerAdapter(fragment: Fragment) : FragmentStateAdapter(fragment) {
    override fun getItemCount() = 2

    override fun createFragment(position: Int) = when (position) {
        0 -> ArticleFragment()
        else -> ActivityFragment()
    }
}