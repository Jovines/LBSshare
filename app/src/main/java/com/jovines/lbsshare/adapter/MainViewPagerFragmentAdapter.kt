package com.jovines.lbsshare.adapter

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.jovines.lbsshare.ui.main.fragment.FoundFragment
import com.jovines.lbsshare.ui.main.fragment.FriendFragment
import com.jovines.lbsshare.ui.main.fragment.MineFragment

/**
 * Created by Jovines on 2020/06/05 21:20
 * description :
 */
class MainViewPagerFragmentAdapter(activity: FragmentActivity) : FragmentStateAdapter(activity) {

    override fun getItemCount() = 3

    override fun createFragment(position: Int): Fragment {
        return when (position) {
            0 -> FriendFragment()
            1 -> FoundFragment()
            else -> MineFragment()
        }
    }
}