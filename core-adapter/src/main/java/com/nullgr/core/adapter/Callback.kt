package com.nullgr.core.adapter

import android.support.v7.util.DiffUtil
import com.nullgr.core.adapter.items.ListItem

/**
 * Realization of [DiffUtil.Callback] that works with [ListItem].
 */
class Callback(
    private val before: List<ListItem>,
    private val after: List<ListItem>
) : DiffUtil.Callback() {

    override fun getOldListSize(): Int = before.size

    override fun getNewListSize(): Int = after.size

    override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean =
        before[oldItemPosition].areItemsTheSame(after[newItemPosition])


    override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean =
        before[oldItemPosition].areContentsTheSame(after[newItemPosition])

    override fun getChangePayload(oldItemPosition: Int, newItemPosition: Int): Any? =
        before[oldItemPosition].getChangePayload(after[newItemPosition])

}