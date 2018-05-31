package com.nullgr.corelibrary.adapter

import android.support.v7.widget.RecyclerView
import android.view.ViewGroup
import com.nullgr.corelibrary.adapter.items.ListItem

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

    /**
     * Place your bind logic here.
     *
     * @param items List of items.
     * @param position Position of item in [items].
     * @param holder ViewHolder for this item type.
     */
    open fun onBindViewHolder(items: List<ListItem>,
                              position: Int,
                              holder: RecyclerView.ViewHolder) {

    }

    /**
     * Called when item changed with payloads.
     *
     * More that one property of item can be changed during particular update.
     * In this case element in [payloads] can be Collection with nested payloads,
     * by default this method iterate over each payload in [payloads]
     * and call [onBindViewHolder] with nested payload
     * or with [payloads] element if it is not Collection.
     *
     * @param items List of items.
     * @param position Position of item in [items].
     * @param holder ViewHolder for this item type.
     * @param payloads List of payloads objects.
     */
    open fun onBindViewHolder(items: List<ListItem>,
                              position: Int,
                              holder: RecyclerView.ViewHolder,
                              payloads: List<Any>) {
        payloads.forEach { payload ->
            when (payload) {
                is Collection<*> -> payload.forEach { nestedPayload ->
                    nestedPayload?.let {
                        onBindViewHolder(items, position, holder, nestedPayload)
                    }
                }
                else -> onBindViewHolder(items, position, holder, payload)
            }
        }
    }

    /**
     * Extended version of [onBindViewHolder] with payloads.
     *
     * @param items List of items.
     * @param position Position of item in items.
     * @param holder ViewHolder for this item type.
     * @param payload Nested payload.
     */
    open fun onBindViewHolder(items: List<ListItem>,
                              position: Int,
                              holder: RecyclerView.ViewHolder,
                              payload: Any) {
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