package com.nullgr.corelibrary.widgets.decor

import android.content.Context
import android.graphics.Canvas
import android.graphics.drawable.Drawable
import android.support.v4.content.ContextCompat
import android.support.v7.widget.RecyclerView
import android.view.View
import com.nullgr.core.adapter.DynamicAdapter

/**
 * author a.komarovskyi
 */
class TypeBasedDividerItemDecoration<T>(context: Context, resId: Int? = null, ignoreLast: Boolean = false, clazz: Class<T>) : RecyclerView.ItemDecoration() {

    companion object {
        private val ATTRS = intArrayOf(android.R.attr.listDivider)
    }

    private var divider: Drawable? = null
    private var isIgnoreLastItem: Boolean = ignoreLast
    private var type: Class<T> = clazz

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
                if (item != null && type == item.javaClass) {
                    if (isIgnoreLastItem && position == adapter.itemCount - 1) return
                    else drawDecoration(parent, view, c)
                }
            }
        }
    }

    private fun drawDecoration(parent: RecyclerView, view: View, c: Canvas) {
        val left = parent.paddingLeft
        val right = parent.width - parent.paddingRight
        val params = view.layoutParams as RecyclerView.LayoutParams
        val top = view.bottom + params.bottomMargin
        val bottom = top + divider!!.intrinsicHeight
        divider?.setBounds(left, top, right, bottom)
        divider?.draw(c)
    }
}