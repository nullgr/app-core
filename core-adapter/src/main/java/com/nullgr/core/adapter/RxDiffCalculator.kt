package com.nullgr.core.adapter

import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListUpdateCallback
import com.nullgr.core.adapter.items.ListItem
import com.nullgr.core.rx.schedulers.ComputationToMainSchedulersFacade
import com.nullgr.core.rx.schedulers.SchedulersFacade
import io.reactivex.Observable
import io.reactivex.functions.Consumer
import io.reactivex.functions.Function3
import java.lang.ref.WeakReference

/**
 * Allows to make difference calculation asynchronous using reactive approach.
 * You can specify schedulers by passing [SchedulersFacade], by default used [ComputationToMainSchedulersFacade].
 * Make attention that DiffResult must be dispatched at same thread that created view.
 *
 * @author vchernyshov
 */
@Deprecated(
    message = "Use functions from DiffUtilsExtensions instead.",
    replaceWith = ReplaceWith("DiffUtilsExtensions.calculate, DiffUtilsExtensions.bindTo")
)
class RxDiffCalculator(
        private val schedulersFacade: SchedulersFacade = ComputationToMainSchedulersFacade()) : DiffCalculator {

    private val calculateDiffFunction = CalculateDiffFunction()

    /**
     * Used to calculate difference asynchronous and dispatch result to [DynamicAdapter].
     * After [android.support.v7.util.DiffUtil.DiffResult] will be calculated new items will be set to adapter via [DynamicAdapter.setData]
     * and DiffResult will be dispatched to adapter that passed as parameter.
     *
     * @param adapter Instance of [DynamicAdapter] that will receive DiffResult.
     * @param before List of items that already set to adapter.
     * @param after New list of items.
     * @param detectMoves True if DiffUtil should try to detect moved items, false otherwise.
     */
    override fun calculateDiff(adapter: DynamicAdapter, before: List<ListItem>, after: List<ListItem>, detectMoves: Boolean) {
        Observable.zip(Observable.just(before), Observable.just(after), Observable.just(detectMoves), calculateDiffFunction)
                .subscribeOn(schedulersFacade.subscribeOn)
                .observeOn(schedulersFacade.observeOn)
                .subscribe(DiffResultConsumer(adapter))
    }

    /**
     * Used to calculate difference asynchronous and dispatch result to [DynamicAdapter].
     * After [android.support.v7.util.DiffUtil.DiffResult] will be calculated
     * DiffResult will be dispatched to updateCallback that passed as parameter.
     *
     * @param updateCallback Instance of [ListUpdateCallback] that will receive DiffResult.
     * @param before List of items that already set.
     * @param after New list of items.
     * @param detectMoves True if DiffUtil should try to detect moved items, false otherwise.
     */
    override fun calculateDiff(updateCallback: ListUpdateCallback, before: List<ListItem>, after: List<ListItem>, detectMoves: Boolean) {
        Observable.zip(Observable.just(before), Observable.just(after), Observable.just(detectMoves), calculateDiffFunction)
                .subscribeOn(schedulersFacade.subscribeOn)
                .observeOn(schedulersFacade.observeOn)
                .subscribe(DiffResultConsumer2(updateCallback))
    }

    /**
     * Implementation of [Function3] that calculates DiffResult.
     */
    private class CalculateDiffFunction : Function3<List<ListItem>, List<ListItem>, Boolean, Pair<List<ListItem>, DiffUtil.DiffResult>> {
        @Throws(Exception::class)
        override fun apply(before: List<ListItem>, after: List<ListItem>, detectMoves: Boolean): Pair<List<ListItem>, DiffUtil.DiffResult> {
            val diffResult = DiffUtil.calculateDiff(Callback(before, after), detectMoves)
            return Pair(after, diffResult)
        }
    }

    /**
     * Implementation of [Consumer] that sets new list of items to adapter and dispatches DiffResults.
     * Instance stores adapter by [WeakReference].
     */
    private class DiffResultConsumer(adapter: DynamicAdapter) : Consumer<Pair<List<ListItem>, DiffUtil.DiffResult>> {

        val reference: WeakReference<DynamicAdapter> = WeakReference(adapter)

        override fun accept(pair: Pair<List<ListItem>, DiffUtil.DiffResult>) {
            val adapter = reference.get()
            if (adapter != null) {
                adapter.setData(pair.first)
                pair.second.dispatchUpdatesTo(adapter)
            }
        }
    }

    /**
     * Implementation of [Consumer] that dispatches DiffResults to [ListUpdateCallback].
     * Instance stores adapter by [WeakReference].
     */
    private class DiffResultConsumer2(updateCallback: ListUpdateCallback) : Consumer<Pair<List<ListItem>, DiffUtil.DiffResult>> {

        val reference: WeakReference<ListUpdateCallback> = WeakReference(updateCallback)

        override fun accept(pair: Pair<List<ListItem>, DiffUtil.DiffResult>) {
            val updateCallback = reference.get()
            if (updateCallback != null) {
                pair.second.dispatchUpdatesTo(updateCallback)
            }
        }
    }
}
