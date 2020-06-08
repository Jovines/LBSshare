package com.jovines.lbsshare.adapter

import android.annotation.SuppressLint
import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.lifecycle.MutableLiveData
import androidx.recyclerview.widget.RecyclerView
import com.afollestad.materialdialogs.MaterialDialog
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.jovines.lbsshare.R
import com.jovines.lbsshare.bean.CardMessageReturn
import com.jovines.lbsshare.bean.PersonalMessageDetailsBean
import com.jovines.lbsshare.event.PrivateMessageChanges
import com.jovines.lbsshare.network.ApiGenerator
import com.jovines.lbsshare.network.UserApiService
import com.jovines.lbsshare.ui.ArticleDetailsActivity
import com.jovines.lbsshare.utils.extensions.setSchedulers
import kotlinx.android.synthetic.main.recycle_personal_content_item.view.*
import org.greenrobot.eventbus.EventBus
import java.text.SimpleDateFormat


/**
 * @author jon
 * @create 2020-05-06 5:49 PM
 *
 * 描述:
 *
 */
class PersonalMessageAdapter(val data: MutableLiveData<List<PersonalMessageDetailsBean>>) :
    RecyclerView.Adapter<ViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) =
        ViewHolder(
            LayoutInflater.from(parent.context)
                .inflate(R.layout.recycle_personal_content_item, parent, false)
        )

    override fun getItemCount() = data.value?.size ?: 0

    @SuppressLint("SimpleDateFormat")
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.itemView.apply {
            data.value?.get(position)?.let { messageDetailsBean ->
                personal_message_date.text =
                    if (messageDetailsBean.time == null) "" else SimpleDateFormat("yyyy/MM/dd").format(
                        messageDetailsBean.time!!
                    )
//                geographic_location_of_personal_message.text = it.position
                personal_message_title.text = messageDetailsBean.title ?: ""
                personal_message_content.text = messageDetailsBean.content ?: ""
                number_of_personal_message_accesses.text =
                    messageDetailsBean.viewUserCount?.toString() ?: ""
                personal_picture_display.adapter = PersonalPicturesAdapter(
                    Gson().fromJson(
                        messageDetailsBean.images ?: "[]",
                        object : TypeToken<List<String>>() {}.type
                    )
                )

                setOnClickListener {
                    val tmp = CardMessageReturn(
                        images = messageDetailsBean.images,
                        commentCount = 0,
                        id = messageDetailsBean.id,
                        user = messageDetailsBean.user,
                        content = messageDetailsBean.content,
                        title = messageDetailsBean.title,
                        time = SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(messageDetailsBean.time)

                    )
                    context.startActivity(
                        Intent(
                            context,
                            ArticleDetailsActivity::class.java
                        ).apply {
                            this.putExtra(ArticleDetailsActivity.DETAILED_DATA, tmp)
                        })
                }
                setOnLongClickListener {
                    MaterialDialog.Builder(context)
                        .title("是否删除该动态？")
                        .content("删除后数据永久性丢失,无法找回")
                        .positiveText("删除")
                        .onPositive { _, _ ->
                            messageDetailsBean.id?.let { it1 ->
                                ApiGenerator.getApiService(UserApiService::class.java)
                                    .deleteMessage(it1)
                                    .setSchedulers()
                                    .subscribe {
                                        EventBus.getDefault().post(PrivateMessageChanges())
                                    }
                            }
                        }
                        .negativeText("取消").show()
                    true
                }
//                if (it.lat != null && it.lon != null && it.position.isBlank()) {
//                    val search = GeocodeSearch(context)
//                    search.setOnGeocodeSearchListener(object :
//                        GeocodeSearch.OnGeocodeSearchListener {
//                        override fun onRegeocodeSearched(p0: RegeocodeResult?, p1: Int) {
//                            val way = p0?.regeocodeAddress?.district ?: ""
//                            geographic_location_of_personal_message.text = way
//                            it.position = way
//                        }
//
//                        override fun onGeocodeSearched(p0: GeocodeResult?, p1: Int) {
//                        }
//                    })
//                    val query =
//                        RegeocodeQuery(LatLonPoint(it.lat!!, it.lon!!), 200f, GeocodeSearch.AMAP)
//                    search.getFromLocationAsyn(query)
//                }

            }

        }
    }
}