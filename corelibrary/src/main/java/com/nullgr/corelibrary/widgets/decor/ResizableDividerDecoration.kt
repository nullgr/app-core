package com.nullgr.corelibrary.widgets.decor

import android.content.Context
import android.content.res.Resources
import android.graphics.Rect
import android.support.annotation.DimenRes
import android.support.v7.widget.RecyclerView
import android.view.View


/**
 * author a.komarovskyi
 */
class ResizableDividerDecoration(
        context: Context,
        @DimenRes private val itemWidth: Int,
        @DimenRes private val marginStartDimen: Int,
        @DimenRes private val marginEndDimen: Int) : RecyclerView.ItemDecoration() {

    private var marginStart: Int
    private var marginEnd: Int

    var offset = 0
        private set

    init {
        marginStart = getPixelSize(context.resources, marginStartDimen)
        marginEnd = getPixelSize(context.resources, marginEndDimen)
    }

    private fun getPixelSize(resources: Resources, @DimenRes margin: Int): Int {
        return if (margin == 0) 0 else resources.getDimensionPixelSize(margin)
    }

    override fun getItemOffsets(outRect: Rect, view: View, parent: RecyclerView, state: RecyclerView.State) {
        val layoutManager = parent.layoutManager
        val cleanWidth = layoutManager.width - layoutManager.paddingLeft - layoutManager.paddingRight
        val viewWidth =  parent.context.resources.getDimensionPixelSize(itemWidth)

        if (viewWidth != 0 && cleanWidth % viewWidth != 0) {
            val numOfViewAtScreen = cleanWidth / viewWidth
            val span = cleanWidth - numOfViewAtScreen * viewWidth
            offset = span / numOfViewAtScreen
            offset /= 2
        }

        val position = parent.getChildAdapterPosition(view)
        val last = (state.itemCount).minus(1)
        when (position) {
            0 -> {
                outRect.left = marginStart
                outRect.right = offset
            }
            last -> {
                outRect.left = offset
                outRect.right = marginEnd
            }
            else -> {
                outRect.left = offset
                outRect.right = offset
            }
        }
    }
}