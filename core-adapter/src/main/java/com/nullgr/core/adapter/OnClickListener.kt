package com.nullgr.core.adapter

import android.support.v7.widget.RecyclerView
import android.view.View

/**
 * Implementation of [View.OnClickListener] that used [RecyclerView.ViewHolder] to determinate
 * [ListItem] related to clicked view and invoke given [handler] with view, item and position.
 *
 * @author chernyshov.
 */
class OnClickListener(
        private val holder: RecyclerView.ViewHolder,
        private val handler: OnClickHandler)
    : View.OnClickListener {
    override fun onClick(view: View) {
        val items = holder.items()
        holder.withAdapterPosition(items, { item, position ->
            handler.handle(view, item, position)
        })
    }
}