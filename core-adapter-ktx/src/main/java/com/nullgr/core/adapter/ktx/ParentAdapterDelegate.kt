package com.nullgr.core.adapter.ktx

import android.os.Parcelable
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.nullgr.core.adapter.AdapterDelegatesFactory
import com.nullgr.core.adapter.DynamicAdapter
import com.nullgr.core.adapter.bindTo
import com.nullgr.core.adapter.items.ListItem
import com.nullgr.core.adapter.items.ParentItem
import com.nullgr.core.adapter.withAdapterPosition
import io.reactivex.Observable
import io.reactivex.disposables.CompositeDisposable

abstract class ParentAdapterDelegate(
    protected val factory: AdapterDelegatesFactory
) : AdapterDelegate() {

    abstract val itemsViewIds: List<Int>

    protected val adapters = mutableMapOf<Any, DynamicAdapter>()
    protected val disposables = mutableMapOf<Any, CompositeDisposable>()

    private val scrollStates = mutableMapOf<ScrollStateKey, Parcelable>()

    override fun onViewDetachedFromWindow(holder: RecyclerView.ViewHolder) {
        holder.withAdapterPosition<ListItem> { item, _ ->
            disposables[item.getUniqueProperty()]?.clear()
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup): RecyclerView.ViewHolder {
        return super.onCreateViewHolder(parent).apply {
            with(this as ViewHolder) {
                itemsViewIds.forEach { id ->
                    containerView.findViewById<RecyclerView>(id)
                        .addOnScrollListener(object : RecyclerView.OnScrollListener() {

                            override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                                super.onScrollStateChanged(recyclerView, newState)
                                if (itemId != RecyclerView.NO_ID && newState == RecyclerView.SCROLL_STATE_IDLE) {
                                    saveScrollState(recyclerView, itemId)
                                }
                            }
                        })
                }
            }
        }
    }

    override fun onViewRecycled(holder: RecyclerView.ViewHolder) {
        super.onViewRecycled(holder)

        with(holder as ViewHolder) {
            if (itemId != RecyclerView.NO_ID) {
                itemsViewIds.forEach { id ->
                    val itemsView = containerView.findViewById<RecyclerView>(id)
                    saveScrollState(itemsView, itemId)
                }
            }
        }
    }

    protected open fun setItems(holder: RecyclerView.ViewHolder, useDiffUtils: Boolean, parent: ParentItem) {
        with(holder as ViewHolder) {
            itemsViewIds.forEach { id ->
                val adapter = createOrGetAdapter(parent)
                val itemsView = containerView.findViewById<RecyclerView>(id)
                itemsView.adapter = adapter

                if (useDiffUtils) {
                    val disposable = createOrGetCompositeDisposable(parent)
                    Observable.just(parent.items).bindTo(adapter, disposable)
                } else {
                    adapter.setData(parent.items)
                    adapter.notifyDataSetChanged()
                }

                restoreScrollState(itemsView, itemId)
            }
        }
    }

    protected open fun createOrGetAdapter(item: ListItem): DynamicAdapter = adapters[item.getUniqueProperty()]
        ?: DynamicAdapter(factory).also { adapters[item.getUniqueProperty()] = it }

    protected open fun createOrGetCompositeDisposable(item: ListItem): CompositeDisposable =
        disposables[item.getUniqueProperty()]
            ?: CompositeDisposable().also { disposables[item.getUniqueProperty()] = it }

    private fun saveScrollState(recyclerView: RecyclerView, holderId: Long) {
        recyclerView.layoutManager?.onSaveInstanceState()?.let {
            scrollStates[ScrollStateKey(recyclerView.id, holderId)] = it
        }
    }

    private fun restoreScrollState(recyclerView: RecyclerView, holderId: Long) {
        scrollStates[ScrollStateKey(recyclerView.id, holderId)]?.let {
            recyclerView.layoutManager?.onRestoreInstanceState(it)
        }
    }

    private data class ScrollStateKey(
        val itemViewId: Int,
        val holderItemId: Long
    )
}