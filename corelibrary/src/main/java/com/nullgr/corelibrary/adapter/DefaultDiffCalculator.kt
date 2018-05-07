package com.nullgr.corelibrary.adapter

import android.support.v7.util.DiffUtil
import android.support.v7.util.ListUpdateCallback
import com.nullgr.corelibrary.adapter.items.ListItem

/**
 * Implementation of [DiffCalculator] that calculates DiffResult and dispatches it to adapter.
 * Executes on MainThread.
 *
 * @author vchernyshov
 */
class DefaultDiffCalculator : DiffCalculator {

    override fun calculateDiff(adapter: DynamicAdapter, before: List<ListItem>, after: List<ListItem>, detectMoves: Boolean) {
        val callback = Callback(before, after)
        val diffResult = DiffUtil.calculateDiff(callback, detectMoves)
        adapter.setData(after)
        diffResult.dispatchUpdatesTo(adapter)
    }

    override fun calculateDiff(updateCallback: ListUpdateCallback, before: List<ListItem>, after: List<ListItem>, detectMoves: Boolean) {
        val callback = Callback(before, after)
        val diffResult = DiffUtil.calculateDiff(callback, detectMoves)
        diffResult.dispatchUpdatesTo(updateCallback)
    }
}