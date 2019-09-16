package com.nullgr.core.adapter.decor

import android.content.Context
import android.graphics.Canvas
import android.graphics.Rect
import android.graphics.drawable.Drawable
import androidx.annotation.DrawableRes
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import android.view.View
import com.nullgr.core.adapter.DynamicAdapter
import com.nullgr.core.adapter.items.ListItem

/**
 *
 * ConditionBasedDividerItemDecoration is a [RecyclerView.ItemDecoration] that can be used as a divider
 * between items of a [android.support.v7.widget.LinearLayoutManager] together with [DynamicAdapter].
 * It supports [android.support.v7.widget.LinearLayoutManager.VERTICAL] orientation.
 *
 * Main feature of this decorator is that you can pass [condition] function and provide
 * custom logic that subscribes where divider must be drawn
 * Also Can be used with custom divider drawable
 *
 * @author Grishko Nikita
 */
class ConditionBasedDividerItemDecoration(context: Context, @DrawableRes resId: Int? = null) : RecyclerView.ItemDecoration() {

    companion object {
        private val ATTRS = intArrayOf(android.R.attr.listDivider)
    }

    private var divider: Drawable? = null

    var condition: ((item: ListItem, allItems: List<ListItem>) -> Boolean)? = null

    init {
        divider = if (resId != null) {
            ContextCompat.getDrawable(context, resId)
        } else {
            val styledAttributes = context.obtainStyledAttributes(ATTRS)
            divider = styledAttributes.getDrawable(0)
            styledAttributes.recycle()
            divider
        }
    }

    override fun onDraw(c: Canvas, parent: RecyclerView, state: RecyclerView.State) {
        for (i in 0 until parent.childCount) {
            val view = parent.getChildAt(i)
            val position = parent.getChildAdapterPosition(view)
            val adapter = parent.adapter
            if (adapter is DynamicAdapter) {
                val item = adapter.getItem(position)
                item?.let {
                    if (condition?.invoke(item, adapter.items) != false) {
                        drawDecoration(parent, view, c)
                    }
                }
            }
        }
    }

    override fun getItemOffsets(outRect: Rect, view: View, parent: RecyclerView, state: RecyclerView.State) {
        if (divider == null) {
            outRect.set(0, 0, 0, 0)
            return
        }
        outRect.set(0, 0, 0, divider?.intrinsicHeight ?: 0)
    }

    private fun drawDecoration(parent: RecyclerView, view: View, c: Canvas) {
        divider?.let {
            val left = parent.paddingLeft
            val right = parent.width - parent.paddingRight
            val params = view.layoutParams as RecyclerView.LayoutParams
            val top = view.bottom + params.bottomMargin
            val bottom = top + it.intrinsicHeight
            it.setBounds(left, top, right, bottom)
            it.draw(c)
        }
    }
}