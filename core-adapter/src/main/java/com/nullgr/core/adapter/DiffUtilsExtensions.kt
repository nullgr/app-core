package com.nullgr.core.adapter

import android.support.v7.util.DiffUtil
import com.nullgr.core.adapter.items.ListItem
import io.reactivex.Observable
import io.reactivex.ObservableTransformer
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.functions.Consumer
import io.reactivex.rxkotlin.addTo
import io.reactivex.schedulers.Schedulers

inline fun calculate(
    adapter: DynamicAdapter,
    strategy: UpdateStrategy = UpdateStrategy.LATEST
): ObservableTransformer<List<ListItem>, Pair<List<ListItem>, DiffUtil.DiffResult>> {
    return ObservableTransformer { s ->
        if (strategy == UpdateStrategy.LATEST) s.switchMap { calculateObservable(adapter, it) }
        else s.concatMap { calculateObservable(adapter, it) }
    }
}

inline fun calculateObservable(
    adapter: DynamicAdapter,
    items: List<ListItem>
): Observable<Pair<List<ListItem>, DiffUtil.DiffResult>> =
    Observable.fromCallable { calculate(adapter, items) }

inline fun calculate(
    adapter: DynamicAdapter,
    items: List<ListItem>
): Pair<List<ListItem>, DiffUtil.DiffResult> {
    val callback = Callback(ArrayList(adapter.items), ArrayList(items))
    val result = DiffUtil.calculateDiff(callback, true)
    return Pair(items, result)
}

val DynamicAdapter.consumer: Consumer<Pair<List<ListItem>, DiffUtil.DiffResult>>
    get() = Consumer { pair ->
        setData(pair.first)
        pair.second.dispatchUpdatesTo(this)
    }

fun Observable<List<ListItem>>.bindTo(
    adapter: DynamicAdapter,
    compositeUnbind: CompositeDisposable,
    strategy: UpdateStrategy = UpdateStrategy.LATEST
) {
    this.observeOn(Schedulers.computation())
        .compose(calculate(adapter, strategy))
        .observeOn(AndroidSchedulers.mainThread())
        .subscribe(adapter.consumer)
        .addTo(compositeUnbind)
}

/**
 * Represents strategy to choose how calculation results
 * should be delivered to [DynamicAdapter]
 */
enum class UpdateStrategy {
    /**
     * Only the last results will be delivered to [DynamicAdapter]
     */
    LATEST,

    /**
     * All results will be delivered to [DynamicAdapter] keeping the queue
     */
    SEQUENCE
}