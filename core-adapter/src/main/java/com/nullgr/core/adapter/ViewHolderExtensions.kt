package com.nullgr.core.adapter

import android.support.v7.widget.*
import com.nullgr.core.adapter.items.*

/**
 * Extension function that allows to get items from adapter via ViewHolder.
 */
inline fun RecyclerView.ViewHolder.items(): List<ListItem>? {
    val parent = itemView.parent as? RecyclerView ?: return null
    val adapter = parent.adapter as? DynamicAdapter ?: return null
    return adapter.items
}

/**
 * Extension function that invoke given [block] if position not equals [RecyclerView.NO_POSITION],
 * list of items that returned by [items] extension not null and if position in [items] bounds.
 */
inline fun RecyclerView.ViewHolder.withAdapterPosition(block: (items: List<ListItem>, item: ListItem, position: Int) -> Unit) {
    with(adapterPosition) {
        if (this != RecyclerView.NO_POSITION) {
            val items = items()
            if (items != null && this >= 0 && this < items.size) {
                block.invoke(items, items[this], this)
            }
        }
    }
}