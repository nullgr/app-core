package com.nullgr.core.adapter

import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.nullgr.core.adapter.items.ListItem

/**
 * Adapter for [ListItem]s based on Hannes Dorfmann AdapterDelegates(https://github.com/sockeqwe/AdapterDelegates)
 * but without necessity to create all AdapterDelegates with adapter creation.
 * Delegates creates by necessity via factory depends on set of [ListItem] added to adapter.
 *
 * @author vchernyshov
 * @author a.komarovskyi
 */
open class DynamicAdapter(factory: AdapterDelegatesFactory) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private val manager: AdapterDelegatesManager = HashCodeBasedAdapterDelegatesManager(factory)

    var items = arrayListOf<ListItem>()

    override fun getItemViewType(position: Int): Int {
        return manager.getItemViewType(items, position)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder =
        manager.onCreateViewHolder(parent, viewType)

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        manager.onBindViewHolder(items, position, holder)
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int, payloads: List<Any>) {
        if (payloads.isEmpty()) manager.onBindViewHolder(items, position, holder)
        else manager.onBindViewHolder(items, position, holder, payloads)
    }

    override fun onViewAttachedToWindow(holder: RecyclerView.ViewHolder) {
        manager.onViewAttachedToWindow(holder)
    }

    override fun onViewDetachedFromWindow(holder: RecyclerView.ViewHolder) {
        manager.onViewDetachedFromWindow(holder)
    }

    override fun onViewRecycled(holder: RecyclerView.ViewHolder) {
        manager.onViewRecycled(holder)
    }

    override fun getItemCount(): Int = items.size

    fun getItemPosition(listItem: ListItem): Int = items.indexOf(listItem)

    fun setData(newItems: List<ListItem>) {
        this.items.clear()
        this.items.addAll(newItems)
        manager.setDelegates(this.items)
    }

    fun getItem(position: Int): ListItem? {
        return when {
            items.isNotEmpty() && position >= 0 -> items[position]
            else -> null
        }
    }
}