package com.jovines.lbsshare.widget

import android.content.Context
import android.util.AttributeSet
import android.view.MotionEvent
import com.youth.banner.Banner

/**
 * Created by Jovines on 2020/06/06 20:12
 * description :
 */
internal class MyBanner : Banner {
    constructor(context: Context?) : super(context)
    constructor(context: Context?, attrs: AttributeSet?) : super(
        context,
        attrs
    )

    constructor(
        context: Context?,
        attrs: AttributeSet?,
        defStyle: Int
    ) : super(context, attrs, defStyle)


}