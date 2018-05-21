package com.nullgr.corelibrary.widgets.decor

import android.content.Context
import android.graphics.Canvas
import android.graphics.drawable.Drawable
import android.support.annotation.DrawableRes
import android.support.v4.content.ContextCompat
import android.support.v7.widget.RecyclerView
import android.view.View
import com.nullgr.core.adapter.DynamicAdapter
import com.nullgr.core.adapter.items.ListItem

/**
 * Created by Grishko Nikita on 01.02.18.
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