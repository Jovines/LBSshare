package com.jovines.lbsshare.adapter

import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.jovines.lbsshare.ui.main.fragment.foud.NewOrHotFragment
import com.jovines.lbsshare.ui.main.fragment.foud.NewOrHotFragment.Companion.Hottest
import com.jovines.lbsshare.ui.main.fragment.foud.NewOrHotFragment.Companion.Newest

/**
 * Created by Jovines on 2020/06/06 18:52
 * description :
 */
class ActivityAdapter(fragment: Fragment) : FragmentStateAdapter(fragment) {

    override fun getItemCount() = 2

    override fun createFragment(position: Int) = when (position) {
        1 -> NewOrHotFragment.newInstance(Hottest)
        else -> NewOrHotFragment.newInstance(Newest)
    }
}