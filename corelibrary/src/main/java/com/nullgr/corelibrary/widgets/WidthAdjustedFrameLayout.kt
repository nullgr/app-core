package com.nullgr.corelibrary.widgets

import android.content.Context
import android.graphics.Point
import android.util.AttributeSet
import android.view.View
import android.view.WindowManager
import android.widget.FrameLayout
import com.nullgr.corelibrary.R

class WidthAdjustedFrameLayout : FrameLayout {

    private var leftOffset: Int = 0
    private var rightOffset: Int = 0
    private var screenWidth: Int = 0

    constructor(context: Context) : this(context, null)

    constructor(context: Context, attrs: AttributeSet?) : this(context, attrs, 0)

    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(context, attrs, defStyleAttr) {

        val attributes = context.theme.obtainStyledAttributes(attrs, R.styleable.WidthAdjustedFrameLayout, defStyleAttr, 0)

        with(attributes) {
            leftOffset = getDimensionPixelSize(R.styleable.WidthAdjustedFrameLayout_leftOffset, 0)
            rightOffset = getDimensionPixelSize(R.styleable.WidthAdjustedFrameLayout_rightOffset, 0)

            val wm = context.getSystemService(Context.WINDOW_SERVICE) as WindowManager
            val display = wm.defaultDisplay
            val screenSize = Point()
            display.getSize(screenSize)
            screenWidth = screenSize.x
        }
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(View.MeasureSpec.makeMeasureSpec(screenWidth - leftOffset - rightOffset, View.MeasureSpec.EXACTLY), heightMeasureSpec)
    }
}