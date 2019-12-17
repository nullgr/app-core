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

    abstract val itemsViewId: Int

    protected val adapters = mutableMapOf<Any, DynamicAdapter>()
    protected val disposables = mutableMapOf<Any, CompositeDisposable>()
    protected val scrollStates = mutableMapOf<Long, Parcelable>()

    override fun onViewDetachedFromWindow(holder: RecyclerView.ViewHolder) {
        holder.withAdapterPosition<ListItem> { item, _ ->
            disposables[item.getUniqueProperty()]?.clear()
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup): RecyclerView.ViewHolder {
        return super.onCreateViewHolder(parent).apply {
            parent.findViewById<RecyclerView>(itemsViewId).addOnScrollListener(object : RecyclerView.OnScrollListener() {

                override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                    super.onScrollStateChanged(recyclerView, newState)
                    if (newState == RecyclerView.SCROLL_STATE_IDLE) {
                        recyclerView.layoutManager?.onSaveInstanceState()?.let {
                            scrollStates[itemId] = it
                        }
                    }
                }
            })
        }
    }

    override fun onViewRecycled(holder: RecyclerView.ViewHolder) {
        super.onViewRecycled(holder)

        with(holder as ViewHolder) {
            itemView.findViewById<RecyclerView>(itemsViewId).layoutManager?.onSaveInstanceState()?.let {
                scrollStates[itemId] = it
            }
        }
    }

    protected open fun setItems(holder: RecyclerView.ViewHolder, useDiffUtils: Boolean, parent: ParentItem) {
        with(holder as ViewHolder) {
            val adapter = createOrGetAdapter(parent)
            val itemsView = itemView.findViewById<RecyclerView>(itemsViewId)
            itemsView.adapter = adapter

            if (useDiffUtils) {
                val disposable = createOrGetCompositeDisposable(parent)
                Observable.just(parent.items).bindTo(adapter, disposable)
            } else {
                adapter.setData(parent.items)
                adapter.notifyDataSetChanged()
            }

            itemsView.layoutManager?.onRestoreInstanceState(scrollStates[itemId])
        }
    }

    protected open fun createOrGetAdapter(item: ListItem): DynamicAdapter = adapters[item.getUniqueProperty()]
        ?: DynamicAdapter(factory).also { adapters[item.getUniqueProperty()] = it }

    protected open fun createOrGetCompositeDisposable(item: ListItem): CompositeDisposable =
        disposables[item.getUniqueProperty()]
            ?: CompositeDisposable().also { disposables[item.getUniqueProperty()] = it }
}