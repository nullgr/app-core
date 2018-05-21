package com.nullgr.corelibrary.ui.decor

import android.content.Context
import android.graphics.Canvas
import android.graphics.Rect
import android.graphics.drawable.Drawable
import android.support.annotation.DrawableRes
import android.support.v4.content.ContextCompat
import android.support.v7.widget.RecyclerView
import android.view.View


/**
 * DividerItemDecoration is a {@link RecyclerView.ItemDecoration} that can be used as a divider
 * between items of a {@link LinearLayoutManager}.
 * It supports [android.support.v7.widget.LinearLayoutManager.VERTICAL] orientation.
 * Can be used with custom divider drawable.
 *
 * @author il_mov.
 */
class DividerItemDecoration : RecyclerView.ItemDecoration {

    companion object {
        private val ATTRS = intArrayOf(android.R.attr.listDivider)
    }

    private var divider: Drawable? = null
    private var needToDrawLast = false

    /**
     * Default divider will be used, or divider provided by [android.R.attr.listDivider] in styles
     * @param context [Context]
     * @param drawLast [Boolean] flag that indicates if need to draw divider for last element
     */
    constructor(context: Context, drawLast: Boolean = false) {
        val styledAttributes = context.obtainStyledAttributes(ATTRS)
        divider = styledAttributes.getDrawable(0)
        styledAttributes.recycle()
        needToDrawLast = drawLast
    }

    /**
     * Custom divider will be used
     * @param context [Context]
     * @param resId Id of drawable res that will be used as divider
     * @param drawLast [Boolean] flag that indicates if need to draw divider for last element
     */
    constructor(context: Context, @DrawableRes resId: Int, drawLast: Boolean = true) {
        divider = ContextCompat.getDrawable(context, resId)
        needToDrawLast = drawLast
    }

    override fun onDraw(c: Canvas, parent: RecyclerView, state: RecyclerView.State) {
        val left = parent.paddingLeft
        val right = parent.width - parent.paddingRight

        for (i in 0 until getChildCount(parent)) {
            val child = parent.getChildAt(i)

            val params = child.layoutParams as RecyclerView.LayoutParams

            val top = child.bottom + params.bottomMargin
            val bottom = top + divider!!.intrinsicHeight

            divider?.setBounds(left, top, right, bottom)
            divider?.draw(c)
        }
    }

    override fun getItemOffsets(outRect: Rect, view: View, parent: RecyclerView,
                                state: RecyclerView.State?) {
        if (divider == null) {
            outRect.set(0, 0, 0, 0)
            return
        }
        outRect.set(0, 0, 0, divider?.intrinsicHeight ?: 0)
    }

    private fun getChildCount(parent: RecyclerView) =
            if (needToDrawLast) parent.childCount
            else parent.childCount - 1
}