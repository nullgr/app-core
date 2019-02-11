package com.nullgr.core.adapter

import android.support.v7.util.DiffUtil
import android.support.v7.widget.RecyclerView
import android.view.ViewGroup
import com.nullgr.core.adapter.items.ListItem

/**
 * Adapter for [ListItem]s based on Hannes Dorfmann AdapterDelegates(https://github.com/sockeqwe/AdapterDelegates)
 * but without necessity to create all AdapterDelegates with adapter creation.
 * Delegates creates by necessity via factory depends on set of [ListItem] added to adapter.
 *
 * @author vchernyshov
 * @author a.komarovskyi
 */
open class DynamicAdapter constructor(
    private val manager: AdapterDelegatesManager,
    private val diffCalculator: DiffCalculator? = null
) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    constructor(factory: AdapterDelegatesFactory, calculator: DiffCalculator? = null) :
        this(HashCodeBasedAdapterDelegatesManager(factory), calculator)

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

    /**
     * Updates items of adapter.
     *
     * @param newItems New list of items.
     * @param enableDiffUtils True if you want use [DiffUtil] to calculate DiffResult, false otherwise.
     * @param detectMoves True if DiffUtil should try to detect moved items, false otherwise.
     */
    fun updateData(newItems: List<ListItem>, enableDiffUtils: Boolean = true, detectMoves: Boolean = true) {
        when (enableDiffUtils) {
            true -> diffCalculator?.calculateDiff(this, ArrayList(items), ArrayList(newItems), detectMoves)
            else -> {
                setData(newItems)
                notifyDataSetChanged()
            }
        }
    }

    fun setData(newItems: List<ListItem>) {
        this.items.clear()
        this.items.addAll(newItems)
        manager.setDelegates(this.items)
    }

    fun getItem(position: Int): ListItem? {
        return when {
            !items.isEmpty() && position >= 0 -> items[position]
            else -> null
        }
    }
}