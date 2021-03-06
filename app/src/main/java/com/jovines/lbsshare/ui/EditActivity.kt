package com.jovines.lbsshare.ui

import android.content.Intent
import android.os.Bundle
import android.widget.LinearLayout
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import com.jovines.lbsshare.R
import com.jovines.lbsshare.adapter.EditPictureSelectionAdapter
import com.jovines.lbsshare.base.BaseViewModelActivity
import com.jovines.lbsshare.databinding.ActivityEditBindingImpl
import com.jovines.lbsshare.ui.login.afterTextChanged
import com.jovines.lbsshare.utils.extensions.getStatusBarHeight
import com.jovines.lbsshare.viewmodel.EditViewModel
import kotlinx.android.synthetic.main.activity_edit.*
import org.jetbrains.anko.topPadding


class EditActivity : BaseViewModelActivity<EditViewModel>() {

    companion object {
        const val PICTURE_SELECTION = 0
    }

    private lateinit var editPictureSelectionAdapter: EditPictureSelectionAdapter


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val bindingImpl =
            DataBindingUtil.setContentView<ActivityEditBindingImpl>(this, R.layout.activity_edit)
        bindingImpl.viewmodel = viewModel
        initActivity()
    }

    private fun initActivity() {
        editPictureSelectionAdapter = EditPictureSelectionAdapter(viewModel.imageUriList)
        recycle_picture_selection.adapter = editPictureSelectionAdapter
        (ll_edit_toolbar.layoutParams as LinearLayout.LayoutParams).apply {
            ll_edit_toolbar.topPadding += getStatusBarHeight()
            height += getStatusBarHeight()
        }

        tv_post_button.setOnClickListener {
            val title = viewModel.title.get()
            val content = viewModel.content.get()
            if (title == null || title.isBlank()) {
                Toast.makeText(this, "您还未输入标题", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }
            if (content == null || content.isBlank()) {
                Toast.makeText(this, "您还未输入正文", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }
            viewModel.publishAnArticle(this, whether_the_location_information_contains.isChecked)
        }

        unpublish.setOnClickListener { finish() }

        et_text.afterTextChanged {
            if (viewModel.content.get()?.length ?: 0 == 200) {
                viewModel.toastEvent.value = "字数已经到达上限"
            }
        }

        whether_the_location_information_contains.setOnClickListener {
            whether_the_location_information_contains.toggle()
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        when (requestCode) {
            PICTURE_SELECTION -> {
                data?.data?.let { viewModel.imageUriList.add(it) }
                editPictureSelectionAdapter.notifyItemInserted(viewModel.imageUriList.size - 1)
            }
        }
    }

    override val viewModelClass = EditViewModel::class.java
}
