package com.jovines.lbsshare.ui.main.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.jovines.lbsshare.R
import com.jovines.lbsshare.databinding.FragmentSquareBindingImpl
import com.jovines.lbsshare.utils.extensions.getStatusBarHeight
import com.jovines.lbsshare.viewmodel.MainViewModel
import kotlinx.android.synthetic.main.fragment_found.*
import kotlinx.android.synthetic.main.fragment_square.*
import org.jetbrains.anko.topPadding

class SquareFragment : Fragment() {

    lateinit var mainViewModel: MainViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        mainViewModel = ViewModelProvider(requireActivity())[MainViewModel::class.java]
        // Inflate the layout for this fragment
        val bindingImpl = DataBindingUtil.inflate<FragmentSquareBindingImpl>(
            inflater,
            R.layout.fragment_square,
            container,
            false
        )
        bindingImpl.mainViewModel = mainViewModel
        return bindingImpl.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        (found_toolbar.layoutParams as ConstraintLayout.LayoutParams).apply {
            found_toolbar.topPadding += requireActivity().getStatusBarHeight()
            height += requireActivity().getStatusBarHeight()
        }
    }

}
