package com.jovines.lbsshare.ui.main.fragment.foud

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.jovines.lbsshare.R
import com.jovines.lbsshare.base.BaseFragment
import kotlinx.android.synthetic.main.fragment_in_discount_fragment.*

class NewOrHotFragment : BaseFragment() {


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_in_discount_fragment, container, false)
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        rv_list_discount
    }

    companion object {

        private const val FRAGMENT_TYPE = "FragmentType"

        const val Hottest="Hot"
        const val Newest="new"

        fun newInstance(type: String): NewOrHotFragment {

            val bundle = Bundle().apply {
                putString(FRAGMENT_TYPE, type)
            }
            return NewOrHotFragment().apply {
                arguments = bundle

            }
        }
    }

}