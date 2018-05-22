package com.nullgr.core.adapter

import android.support.v7.util.ListUpdateCallback
import com.nullgr.core.adapter.items.ListItem

/**
 * Interface for diff calculating.
 *
 * @author vchernyshov
 */
interface DiffCalculator {

    /**
     * Used to calculate difference and dispatch result to [DynamicAdapter].
     *
     * @param adapter Instance of [DynamicAdapter] that will receive DiffResult.
     * @param before List of items that already set to adapter.
     * @param after New list of items.
     * @param detectMoves True if DiffUtil should try to detect moved items, false otherwise.
     */
    fun calculateDiff(
            adapter: DynamicAdapter,
            before: List<ListItem>,
            after: List<ListItem>,
            detectMoves: Boolean)

    /**
     * Used to calculate difference and dispatch result to [ListUpdateCallback].
     *
     * @param updateCallback Instance of [ListUpdateCallback] that will receive DiffResult.
     * @param before List of items that already set.
     * @param after New list of items.
     * @param detectMoves True if DiffUtil should try to detect moved items, false otherwise.
     */
    fun calculateDiff(
            updateCallback: ListUpdateCallback,
            before: List<ListItem>,
            after: List<ListItem>,
            detectMoves: Boolean)
}