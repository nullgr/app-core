package com.nullgr.core.adapter

import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListUpdateCallback
import com.nullgr.core.adapter.items.ListItem

/**
 * Implementation of [DiffCalculator] that calculates DiffResult and dispatches it to adapter.
 * Executes on MainThread.
 *
 * @author vchernyshov
 */
@Deprecated(
    message = "Use functions from DiffUtilsExtensions instead.",
    replaceWith = ReplaceWith("DiffUtilsExtensions.calculate")
)
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