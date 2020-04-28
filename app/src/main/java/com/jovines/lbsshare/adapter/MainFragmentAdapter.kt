package com.jovines.lbsshare.adapter

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.jovines.lbsshare.ui.main.fragment.FoundFragment
import com.jovines.lbsshare.ui.main.fragment.MineFragment
import com.jovines.lbsshare.ui.main.fragment.SquareFragment

/**
 * @author Jovines
 * @create 2020-04-28 5:31 PM
 *
 * description:
 *
 */
class MainFragmentAdapter(activity: FragmentActivity) : FragmentStateAdapter(activity) {

    override fun getItemCount() = 3

    override fun createFragment(position: Int): Fragment {
        return when (position) {
            0 -> FoundFragment()
            1 -> SquareFragment()
            else -> MineFragment()
        }
    }
}