package com.nullgr.corelibrary.adapter

import android.support.v7.widget.RecyclerView
import android.view.ViewGroup
import com.nullgr.corelibrary.adapter.items.ListItem
import java.util.concurrent.CopyOnWriteArrayList
import javax.inject.Inject

/**
 * author a.komarovskyi
 */
open class BaseAdapter @Inject constructor(delegatesFactory: AdapterDelegatesFactory,
                                           private val differenceCalculator: RxDifferenceCalculator)
    : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private val manager = AdapterDelegatesManager(delegatesFactory)

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

    fun updateData(newItems: List<ListItem>, enableDiffUtils: Boolean = true) {
        when (enableDiffUtils) {
            true -> differenceCalculator.calculateDiff(this, items, newItems).subscribe()
            else -> {
                setData(newItems)
                notifyDataSetChanged()
            }
        }

    }

    fun setData(newItems: List<ListItem>) {
        manager.setDelegates(newItems)
        this.items = newItems as ArrayList<ListItem>
    }

    fun addHeader(headerItem: ListItem) {
        manager.addDelegateForViewType(headerItem.javaClass)
        updateData(plusToListStart(items, headerItem))
    }

    fun addFooter(footerItem: ListItem) {
        manager.addDelegateForViewType(footerItem.javaClass)
        updateData(plusToList(items, footerItem))
    }

    fun removeItem(clazz: Class<*>) {
        val item = getItemForViewType(clazz, true)
        item?.let {
            updateData(minusFromList(items, item))
        }
    }

    fun notifyItem(clazz: Class<*>, first: Boolean, payload: Any) {
        (0 until itemCount).forEach { i ->
            val item = getItem(i)
            item?.let {
                if (item.javaClass == clazz) {
                    notifyItemChanged(i, payload)
                    if (first) {
                        return
                    }
                }
            }
        }
    }

    private fun getItemForViewType(clazz: Class<*>, first: Boolean): ListItem? {
        (0 until itemCount)
                .map { getItem(it) }
                .filter { it?.javaClass == clazz }
                .forEach {
                    if (first) {
                        return it
                    }
                }
        return null
    }

    fun getItem(position: Int): ListItem? {
        return when {
            !items.isEmpty() && position >= 0 -> items[position]
            else -> null
        }
    }

    private fun plusToListStart(originalList: List<ListItem>, item: ListItem): CopyOnWriteArrayList<ListItem> {
        val newList = CopyOnWriteArrayList<ListItem>()
        newList.add(item)
        newList.addAll(originalList)
        return newList
    }

    private fun plusToList(originalList: List<ListItem>, item: ListItem): CopyOnWriteArrayList<ListItem> {
        val newList = CopyOnWriteArrayList<ListItem>()
        newList.addAll(originalList)
        newList.add(item)
        return newList
    }

    private fun minusFromList(originalList: List<ListItem>, item: ListItem): CopyOnWriteArrayList<ListItem> {
        val newList = CopyOnWriteArrayList<ListItem>()
        newList.addAll(originalList)
        newList.remove(item)
        return newList
    }
}