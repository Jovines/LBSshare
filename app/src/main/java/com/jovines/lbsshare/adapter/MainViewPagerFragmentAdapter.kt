package com.jovines.lbsshare.adapter

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentStatePagerAdapter
import com.jovines.lbsshare.ui.main.fragment.FoundFragment
import com.jovines.lbsshare.ui.main.fragment.FriendFragment
import com.jovines.lbsshare.ui.main.fragment.MineFragment

/**
 * Created by Jovines on 2020/06/05 21:20
 * description :
 */
class MainViewPagerFragmentAdapter(fm:FragmentManager) : FragmentStatePagerAdapter(fm,
    BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT
) {

    override fun getItem(position: Int): Fragment {
        return when (position) {
            0 -> FriendFragment()
            1 -> FoundFragment()
            else -> MineFragment()
        }
    }

    override fun getCount() = 3
}