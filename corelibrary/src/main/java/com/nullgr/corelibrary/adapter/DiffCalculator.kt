package com.nullgr.corelibrary.adapter

import com.nullgr.corelibrary.adapter.items.ListItem

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
}