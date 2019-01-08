package com.nullgr.core.adapter.ktx

import android.support.v7.widget.RecyclerView
import android.view.ViewGroup
import com.nullgr.core.adapter.AdapterDelegate
import com.nullgr.core.adapter.items.ListItem

/**
 * Delegate of specific [ListItem] that responses for creating and binds view for item.
 * Uses ViewHolder that supports view caching.
 *
 * @author vchernyshov
 */
abstract class AdapterDelegate: AdapterDelegate() {

    override fun onCreateViewHolder(parent: ViewGroup): RecyclerView.ViewHolder {
        return BaseViewHolder(parent, layoutResource)
    }
}