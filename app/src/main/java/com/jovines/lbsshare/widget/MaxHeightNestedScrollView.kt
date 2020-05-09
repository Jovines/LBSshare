package com.jovines.lbsshare.widget

import android.content.Context
import android.util.AttributeSet
import androidx.core.widget.NestedScrollView
import com.jovines.lbsshare.R

/**
 * @author jon
 * @create 2020-05-09 5:26 PM
 *
 *
 * 描述:
 */
class MaxHeightNestedScrollView : NestedScrollView {
    constructor(context: Context) : super(context)
    constructor(
        context: Context,
        attrs: AttributeSet?
    ) : super(context, attrs)

    constructor(
        context: Context,
        attrs: AttributeSet?,
        defStyleAttr: Int
    ) : super(context, attrs, defStyleAttr)


    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)
        val childView = getChildAt(0)
        val maximumHeight = resources.getDimension(R.dimen.details_max_height)
        if (childView.measuredHeight > maximumHeight) {
            setMeasuredDimension(measuredWidth, maximumHeight.toInt())
        }
    }
}