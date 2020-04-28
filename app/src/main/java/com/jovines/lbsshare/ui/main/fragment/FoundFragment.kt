package com.jovines.lbsshare.ui.main.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.view.marginTop
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.jovines.lbsshare.R
import com.jovines.lbsshare.adapter.FoundRecycleAdapter
import com.jovines.lbsshare.databinding.FragmentFoundBindingImpl
import com.jovines.lbsshare.utils.extensions.getStatusBarHeight
import com.jovines.lbsshare.viewmodel.MainViewModel
import kotlinx.android.synthetic.main.fragment_found.*
import org.jetbrains.anko.topPadding


class FoundFragment : Fragment() {

    lateinit var mainViewModel:MainViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        mainViewModel = ViewModelProvider(requireActivity())[MainViewModel::class.java]
        val bindingImpl:FragmentFoundBindingImpl = DataBindingUtil.inflate(inflater,R.layout.fragment_found, container, false)
        bindingImpl.mainViewModel = mainViewModel
        return bindingImpl.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        map.onCreate(savedInstanceState)
        initFragment()
    }

    private fun initFragment() {
        (found_toolbar.layoutParams as ConstraintLayout.LayoutParams).apply {
            found_toolbar.topPadding += requireActivity().getStatusBarHeight()
            height += requireActivity().getStatusBarHeight()
        }
        found_recycle.layoutManager = LinearLayoutManager(requireContext())
        found_recycle.adapter = FoundRecycleAdapter(listOf("","","","","","","","","","","","","","",""))
        val map = map.map
    }

    override fun onDestroyView() {
        super.onDestroyView()
        map.onDestroy()
    }

    override fun onResume() {
        super.onResume()
        map.onResume()
    }

    override fun onPause() {
        super.onPause()
        map.onPause()
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        map.onSaveInstanceState(outState)
    }
}
