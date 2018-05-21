package com.nullgr.core.adapter

import android.support.v7.widget.RecyclerView
import com.nullgr.core.adapter.items.ListItem

/**
 * Extension function that allows to get items from adapter via ViewHolder.
 */
inline fun RecyclerView.ViewHolder.items(): List<ListItem>? {
    val parent = itemView.parent as? RecyclerView ?: return null
    val adapter = parent.adapter as? DynamicAdapter ?: return null
    return adapter.items
}

/**
 * Extension function that invoke given [block] with [ListItem] and adapterPosition
 * if position not equals [RecyclerView.NO_POSITION], passed [items] not null
 * and position in [items] bounds.
 */
inline fun RecyclerView.ViewHolder.withAdapterPosition(items: List<ListItem>?, block: (item: ListItem, position: Int) -> Unit) {
    with(adapterPosition) {
        if (this != RecyclerView.NO_POSITION && items != null && this in 0 until items.size) {
            block.invoke(items[this], this)
        }
    }
}