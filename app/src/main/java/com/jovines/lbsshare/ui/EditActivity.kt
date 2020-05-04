package com.jovines.lbsshare.ui

import android.content.ContentResolver
import android.content.Intent
import android.database.Cursor
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.widget.LinearLayout
import com.jovines.lbsshare.R
import com.jovines.lbsshare.adapter.EditPictureSelectionAdapter
import com.jovines.lbsshare.base.BaseViewModelActivity
import com.jovines.lbsshare.utils.extensions.getStatusBarHeight
import com.jovines.lbsshare.utils.getUriPath
import com.jovines.lbsshare.viewmodel.EditViewModel
import kotlinx.android.synthetic.main.activity_edit.*
import org.jetbrains.anko.topPadding
import java.io.File


class EditActivity : BaseViewModelActivity<EditViewModel>() {

    companion object {
        const val PICTURE_SELECTION = 0
    }

    private lateinit var editPictureSelectionAdapter: EditPictureSelectionAdapter


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_edit)
        initActivity()
    }

    private fun initActivity() {
        editPictureSelectionAdapter = EditPictureSelectionAdapter(viewModel.imageUriList)
        recycle_picture_selection.adapter = editPictureSelectionAdapter
        (ll_edit_toolbar.layoutParams as LinearLayout.LayoutParams).apply {
            ll_edit_toolbar.topPadding += getStatusBarHeight()
            height += getStatusBarHeight()
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        when (requestCode) {
            PICTURE_SELECTION -> {
                val element = data?.data.toString()
                viewModel.imageUriList.add(element)
                val fdd = File(getUriPath(data?.data) ?: "")
                val s = fdd.exists()
                editPictureSelectionAdapter.notifyItemInserted(viewModel.imageUriList.size - 1)
            }
        }
    }

    override val viewModelClass = EditViewModel::class.java
}
