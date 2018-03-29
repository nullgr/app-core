package com.nullgr.corelibrary.adapter

import android.support.v7.widget.RecyclerView
import android.view.ViewGroup
import com.nullgr.corelibrary.adapter.items.ListItem

/**
 * author a.komarovskyi
 */
abstract class AdapterDelegate {

    abstract val layoutResource: Int

    abstract val itemType: Any

    fun isForViewType(items: List<ListItem>, position: Int): Boolean =
            items[position]::class == itemType

    open fun onCreateViewHolder(parent: ViewGroup): RecyclerView.ViewHolder =
            BaseViewHolder(parent, layoutResource)

    abstract fun onBindViewHolder(items: List<ListItem>,
                                  position: Int,
                                  vh: RecyclerView.ViewHolder)

    open fun onBindViewHolder(items: List<ListItem>,
                              position: Int,
                              vh: RecyclerView.ViewHolder,
                              payloads: List<Any>) {
    }

    fun onViewRecycled(holder: RecyclerView.ViewHolder) {}

    fun onFailedToRecycleView(holder: RecyclerView.ViewHolder): Boolean {
        return false
    }

    fun onViewAttachedToWindow(holder: RecyclerView.ViewHolder) {}

    fun onViewDetachedFromWindow(holder: RecyclerView.ViewHolder) {}
}