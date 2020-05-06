package com.jovines.lbsshare.adapter

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.lifecycle.MutableLiveData
import androidx.recyclerview.widget.RecyclerView
import com.amap.api.services.core.LatLonPoint
import com.amap.api.services.geocoder.GeocodeResult
import com.amap.api.services.geocoder.GeocodeSearch
import com.amap.api.services.geocoder.RegeocodeQuery
import com.amap.api.services.geocoder.RegeocodeResult
import com.jovines.lbsshare.R
import com.jovines.lbsshare.bean.PersonalMessageDetailsBean
import kotlinx.android.synthetic.main.recycle_personal_content_item.view.*
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
            data.value?.get(position)?.let {
                personal_message_date.text =
                    if (it.time == null) "" else SimpleDateFormat("yyyy/MM/dd").format(it.time!!)
                geographic_location_of_personal_message.text = it.position
                personal_message_title.text = it.title?:""
                personal_message_content.text = it.content?:""
                number_of_personal_message_accesses.text = it.viewUserCount?.toString()?:""
                if (it.lat != null && it.lon != null && it.position.isBlank()) {
                    val search = GeocodeSearch(context)
                    search.setOnGeocodeSearchListener(object :
                        GeocodeSearch.OnGeocodeSearchListener {
                        override fun onRegeocodeSearched(p0: RegeocodeResult?, p1: Int) {
                            val way = p0?.regeocodeAddress?.district ?: ""
                            geographic_location_of_personal_message.text = way
                            it.position = way
                        }

                        override fun onGeocodeSearched(p0: GeocodeResult?, p1: Int) {
                        }
                    })
                    val query =
                        RegeocodeQuery(LatLonPoint(it.lat!!, it.lon!!), 200f, GeocodeSearch.AMAP)
                    search.getFromLocationAsyn(query)
                }

            }

        }
    }
}