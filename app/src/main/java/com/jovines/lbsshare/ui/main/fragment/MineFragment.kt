package com.jovines.lbsshare.ui.main.fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.constraintlayout.widget.ConstraintLayout

import com.jovines.lbsshare.R
import com.jovines.lbsshare.utils.extensions.getStatusBarHeight
import kotlinx.android.synthetic.main.fragment_found.*
import org.jetbrains.anko.topPadding

class MineFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_mine, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        (found_toolbar.layoutParams as ConstraintLayout.LayoutParams).apply {
            found_toolbar.topPadding += requireActivity().getStatusBarHeight()
            height += requireActivity().getStatusBarHeight()
        }
    }
}
