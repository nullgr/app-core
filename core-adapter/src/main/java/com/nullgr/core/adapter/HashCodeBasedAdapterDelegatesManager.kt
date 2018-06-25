package com.nullgr.core.adapter

import android.support.v7.widget.RecyclerView
import android.view.ViewGroup
import com.nullgr.core.adapter.exceptions.DelegateNotFoundException
import com.nullgr.core.adapter.items.ListItem

/**
 * Responded for delegates management process.
 * Used hash of Item::class.java.name to generate viewType.
 * Delegates stores in [HashMap].
 *
 * @author vchernyshov
 */
class HashCodeBasedAdapterDelegatesManager(private val delegatesFactory: AdapterDelegatesFactory) : AdapterDelegatesManager {

    private var delegates = hashMapOf<Int, AdapterDelegate>()

    override fun setDelegates(items: List<ListItem>) {
        items.forEach { item ->
            val viewType = viewTypeFrom(item)
            val delegate = delegates[viewType]
            if (delegate == null) {
                delegates[viewType] = delegatesFactory.createDelegate(item.javaClass)
            }
        }
    }

    override fun getItemViewType(items: List<ListItem>, position: Int): Int {
        return viewTypeFrom(items[position])
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return getDelegateOrThrowException(viewType).onCreateViewHolder(parent)
    }

    override fun onBindViewHolder(items: List<ListItem>, position: Int, vh: RecyclerView.ViewHolder) {
        getDelegateOrThrowException(vh.itemViewType).onBindViewHolder(items, position, vh)
    }

    override fun onBindViewHolder(items: List<ListItem>, position: Int, vh: RecyclerView.ViewHolder, payloads: List<Any>) {
        getDelegateOrThrowException(vh.itemViewType).onBindViewHolder(items, position, vh, payloads)
    }

    override fun onViewRecycled(vh: RecyclerView.ViewHolder) {
        getDelegateOrThrowException(vh.itemViewType).onViewRecycled(vh)
    }

    override fun onFailedToRecycleView(vh: RecyclerView.ViewHolder): Boolean {
        return getDelegateOrThrowException(vh.itemViewType).onFailedToRecycleView(vh)
    }

    override fun onViewAttachedToWindow(vh: RecyclerView.ViewHolder) {
        getDelegateOrThrowException(vh.itemViewType).onViewAttachedToWindow(vh)
    }

    override fun onViewDetachedFromWindow(vh: RecyclerView.ViewHolder) {
        getDelegateOrThrowException(vh.itemViewType).onViewDetachedFromWindow(vh)
    }

    private fun getDelegateOrThrowException(viewType: Int): AdapterDelegate {
        return delegates[viewType] ?: throw DelegateNotFoundException()
    }

    private fun viewTypeFrom(item: ListItem): Int {
        return item::class.java.name.hashCode()
    }
}
