package com.nullgr.corelibrary.widgets.decor

import android.content.Context
import android.content.res.Resources
import android.graphics.Rect
import android.support.annotation.DimenRes
import android.support.v7.widget.RecyclerView
import android.view.View

class VerticalMarginItemDecoration(
        context: Context,
        @DimenRes
        private val marginTopDimen: Int,
        @DimenRes
        private val marginBottomDimen: Int,
        @DimenRes
        private var marginBetweenDimen: Int
) : RecyclerView.ItemDecoration() {

    private var marginTop: Int
    private var marginBottom: Int
    private var marginBetween: Int

    init {
        marginTop = getPixelSize(context.resources, marginTopDimen)
        marginBottom = getPixelSize(context.resources, marginBottomDimen)
        marginBetween = getPixelSize(context.resources, marginBetweenDimen)
        marginBetween = if (marginBetween != 0) marginBetween / 2 else 0
    }

    override fun getItemOffsets(outRect: Rect?, view: View?, parent: RecyclerView?, state: RecyclerView.State?) {
        val position = parent?.getChildAdapterPosition(view)
        val last = state?.itemCount?.minus(1)
        when (position) {
            0 -> {
                outRect?.top = marginTop
                outRect?.bottom = marginBetween
            }
            last -> {
                outRect?.top = marginBetween
                outRect?.bottom = marginBottom
            }
            else -> {
                outRect?.top = marginBetween
                outRect?.bottom = marginBetween
            }
        }
    }

    private fun getPixelSize(resources: Resources, @DimenRes margin: Int): Int {
        return if (margin == 0) 0 else resources.getDimensionPixelSize(margin)
    }
}