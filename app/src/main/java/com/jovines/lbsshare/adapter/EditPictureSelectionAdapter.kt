package com.jovines.lbsshare.adapter

import android.app.Activity
import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.afollestad.materialdialogs.MaterialDialog
import com.bumptech.glide.Glide
import com.jovines.lbsshare.R
import com.jovines.lbsshare.ui.EditActivity
import kotlinx.android.synthetic.main.recycle_edit_picture_selection_item.view.*

/**
 * @author jon
 * @create 2020-04-30 2:01 PM
 *
 * 描述:
 *
 */
class EditPictureSelectionAdapter(private val dataList: MutableList<String>) :
    RecyclerView.Adapter<ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) = ViewHolder(
        LayoutInflater.from(parent.context)
            .inflate(R.layout.recycle_edit_picture_selection_item, parent, false)
    )

    override fun getItemCount() = dataList.size + 1

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        if (dataList.size != position) {
            holder.itemView.apply {
                Glide.with(context).load(dataList[position]).into(choose_a_picture)
                setOnLongClickListener {
                    val dialog = MaterialDialog.Builder(context)
                        .title("提示")
                        .content("是否删除此图片")
                        .positiveText("是的").onPositive { dialog, which ->
                            dataList.remove(dataList[position])
                            notifyItemRemoved(position)
                        }.show()
                    true
                }
            }
        } else {
            holder.itemView.setOnClickListener {
                val intent = Intent()
                intent.type = "image/*"
                intent.action = "android.intent.action.PICK"
                intent.addCategory("android.intent.category.DEFAULT")
                (it.context as Activity).startActivityForResult(intent, EditActivity.PICTURE_SELECTION)
            }
        }
    }

}