package com.jovines.lbsshare.widget

import android.content.Context
import android.graphics.Canvas
import android.graphics.Paint
import android.util.AttributeSet
import android.view.View
import org.jetbrains.anko.dip

/**
 * @author Jovines
 * @create 2020-05-06 4:03 PM
 *
 *
 * description:
 */
class DottedLineView : View {
    constructor(context: Context?) : super(context)
    constructor(context: Context?, attrs: AttributeSet?) : super(
        context,
        attrs
    )

    constructor(
        context: Context?,
        attrs: AttributeSet?,
        defStyleAttr: Int
    ) : super(context, attrs, defStyleAttr)

    private val paint = Paint().apply {
        color = 0xff669999.toInt()
        strokeWidth = dip(1).toFloat()
    }

    private val lineLength = dip(8)
    private val interval = dip(4)

    override fun onDraw(canvas: Canvas) {
        var drawHeight = 0F
        while (drawHeight < height) {
//            canvas.drawLine(width / 2f, drawHeight, width / 2f, drawHeight + lineLength, paint)
            canvas.drawCircle(width / 2f, drawHeight + lineLength / 2, dip(2f).toFloat(), paint)
            drawHeight += (lineLength + interval)
        }
    }
}