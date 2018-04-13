package com.nullgr.corelibrary.adapter

import android.support.v7.util.DiffUtil
import com.nullgr.corelibrary.adapter.items.ListItem
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.functions.Consumer
import io.reactivex.functions.Function3
import io.reactivex.observers.DisposableObserver
import io.reactivex.schedulers.Schedulers
import java.lang.ref.WeakReference
import javax.inject.Inject
import javax.inject.Singleton

/**
 * author a.komarovskyi
 */
@Singleton
class RxDifferenceCalculator @Inject constructor() {

    private val calculateDiffFunction = CalculateDiffFunction()

    fun calculateDiff(
            adapter: BaseAdapter,
            before: List<ListItem>,
            after: List<ListItem>,
            detectMoves: Boolean = true): Observable<Pair<List<ListItem>, DiffUtil.DiffResult>> {
     return Observable.zip<List<ListItem>,
                List<ListItem>,
                Boolean, Pair<List<ListItem>,
                DiffUtil.DiffResult>>(Observable.just(before),
                Observable.just(after),
                Observable.just(detectMoves), calculateDiffFunction)
                .subscribeOn(Schedulers.computation())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnNext(DiffResultConsumer(adapter))
    }



    private class Callback(val before: List<ListItem>,
                           val after: List<ListItem>) : DiffUtil.Callback() {

        override fun getOldListSize(): Int = before.size

        override fun getNewListSize(): Int = after.size

        override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean =
                before[oldItemPosition].areItemsTheSame(after[newItemPosition])


        override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean =
                before[oldItemPosition].areContentsTheSame(after[newItemPosition])

        override fun getChangePayload(oldItemPosition: Int, newItemPosition: Int): Any? =
                before[oldItemPosition].getChangePayload(after[newItemPosition])

    }

    private class CalculateDiffFunction : Function3<List<ListItem>, List<ListItem>,
            Boolean, Pair<List<ListItem>, DiffUtil.DiffResult>> {
        @Throws(Exception::class)
        override fun apply(before: List<ListItem>, after: List<ListItem>, detectMoves: Boolean): Pair<List<ListItem>, DiffUtil.DiffResult> {
            val diffResult = DiffUtil.calculateDiff(Callback(before, after), detectMoves)
            return Pair(after, diffResult)
        }
    }

    private class DiffResultConsumer(adapter: BaseAdapter)
        : Consumer<Pair<List<ListItem>, DiffUtil.DiffResult>> {

        val reference: WeakReference<BaseAdapter> = WeakReference(adapter)

        override fun accept(pair: Pair<List<ListItem>, DiffUtil.DiffResult>) {
            val adapter = reference.get()
            if (adapter != null) {
                adapter.setData(pair.first)
                pair.second.dispatchUpdatesTo(adapter)
            }
        }
    }

    private class DiffResultObservable(adapter: BaseAdapter)
        : DisposableObserver<Pair<List<ListItem>, DiffUtil.DiffResult>>() {

        private val reference: WeakReference<BaseAdapter> = WeakReference(adapter)

        override fun onNext(pair: Pair<List<ListItem>, DiffUtil.DiffResult>) {
            val adapter = reference.get()
            if (adapter != null) {
                adapter.setData(pair.first)
                pair.second.dispatchUpdatesTo(adapter)
            }
        }

        override fun onError(e: Throwable) {
            reference.clear()
        }

        override fun onComplete() {
            reference.clear()
        }
    }
}
