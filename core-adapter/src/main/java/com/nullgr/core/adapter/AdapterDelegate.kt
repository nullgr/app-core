package com.nullgr.core.adapter

import android.support.v7.widget.RecyclerView
import android.view.ViewGroup
import com.nullgr.core.adapter.items.ListItem

/**
 * Delegate of specific [ListItem] that responses for creating and binds view for item.
 *
 * @author vchernyshov
 * @author a.komarovskyi
 */
abstract class AdapterDelegate {

    /**
     * Id of layout resource that represents item.
     */
    abstract val layoutResource: Int

    /**
     * Should be class of item. For example: MyItem::class.
     */
    abstract val itemType: Any

    /**
     * Checks is this delegate for item on given position.
     *
     * @param items List of items.
     * @param position Position of item.
     *
     * @return True if class of item at [position] equals [itemType], false otherwise.
     */
    fun isForViewType(items: List<ListItem>, position: Int): Boolean =
            items[position]::class == itemType

    /**
     * Creates ViewHolder for delegate. By default creates [BaseViewHolder].
     * If you using Java create your own ViewHolder for each AdapterDelegate.
     */
    open fun onCreateViewHolder(parent: ViewGroup): RecyclerView.ViewHolder =
            BaseViewHolder(parent, layoutResource)

    open fun onBindViewHolder(items: List<ListItem>,
                              position: Int,
                              holder: RecyclerView.ViewHolder) {

    }

    open fun onBindViewHolder(items: List<ListItem>,
                              position: Int,
                              holder: RecyclerView.ViewHolder,
                              payloads: List<Any>) {
    }

    /**
     * @see [RecyclerView.Adapter.onViewRecycled]
     */
    open fun onViewRecycled(holder: RecyclerView.ViewHolder) {}

    /**
     * @see [RecyclerView.Adapter.onFailedToRecycleView]
     */
    open fun onFailedToRecycleView(holder: RecyclerView.ViewHolder): Boolean {
        return false
    }

    /**
     * @see [RecyclerView.Adapter.onViewAttachedToWindow]
     */
    open fun onViewAttachedToWindow(holder: RecyclerView.ViewHolder) {}

    /**
     * @see [RecyclerView.Adapter.onViewDetachedFromWindow]
     */
    open fun onViewDetachedFromWindow(holder: RecyclerView.ViewHolder) {}
}