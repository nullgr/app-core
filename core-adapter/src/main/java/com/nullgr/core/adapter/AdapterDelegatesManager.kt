package com.nullgr.core.adapter

import androidx.recyclerview.widget.RecyclerView
import android.view.ViewGroup
import com.nullgr.core.adapter.items.ListItem

interface AdapterDelegatesManager {

    fun setDelegates(items: List<ListItem>)

    fun getItemViewType(items: List<ListItem>, position: Int): Int

    fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder

    fun onBindViewHolder(items: List<ListItem>, position: Int, vh: RecyclerView.ViewHolder)

    fun onBindViewHolder(items: List<ListItem>, position: Int, vh: RecyclerView.ViewHolder, payloads: List<Any>)

    fun onViewRecycled(vh: RecyclerView.ViewHolder)

    fun onFailedToRecycleView(vh: RecyclerView.ViewHolder): Boolean

    fun onViewAttachedToWindow(vh: RecyclerView.ViewHolder)

    fun onViewDetachedFromWindow(vh: RecyclerView.ViewHolder)
}