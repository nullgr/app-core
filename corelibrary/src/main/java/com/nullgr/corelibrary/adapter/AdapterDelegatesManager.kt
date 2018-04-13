package com.nullgr.corelibrary.adapter

import android.support.v4.util.SparseArrayCompat
import android.support.v7.widget.RecyclerView
import android.view.ViewGroup
import com.nullgr.corelibrary.adapter.exceptions.DelegateNotFoundException
import com.nullgr.corelibrary.adapter.items.ListItem


class AdapterDelegatesManager(
        private val delegatesFactory: AdapterDelegatesFactory) {

    companion object {
        internal val FALLBACK_DELEGATE_VIEW_TYPE = Integer.MAX_VALUE - 1
    }

    private var delegates = SparseArrayCompat<AdapterDelegate>()

    fun setDelegates(items: List<ListItem>) {
        for (i in items.indices) {
            val delegate = getDelegateForPosition(items, i)
            if (delegate == null) {
                val clazz = items[i].javaClass
                val newDelegate: AdapterDelegate = delegatesFactory.createDelegate(clazz)
                addDelegate(newDelegate)
            }
        }
    }

    fun addDelegateForViewType(clazz: Class<ListItem>) =
        addDelegate(delegatesFactory.createDelegate(clazz))

    fun getItemViewType(items: List<ListItem>, position: Int): Int {
        (0 until delegates.size()).forEach { i ->
            val delegate = delegates.valueAt(i)
            if (delegate.isForViewType(items, position)) {
                return delegates.keyAt(i)
            }
        }
        throw IllegalArgumentException("No AdapterDelegate that matches position $position")
    }

    fun getItemViewType(delegate: AdapterDelegate): Int {
        var index = 0
        for (i in 0 until delegates.size()) {
            if (delegates.valueAt(i).javaClass.isAssignableFrom(delegate.javaClass)) {
                index = i
                break
            } else {
                index = -1
            }
        }
        return if (index == -1) -1 else delegates.keyAt(index)
    }

    fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder =
            getDelegateForViewTypeOrThrowException(viewType).onCreateViewHolder(parent)

    fun onBindViewHolder(items: List<ListItem>, position: Int, vh: RecyclerView.ViewHolder) =
            getDelegateForViewTypeOrThrowException(vh.itemViewType).onBindViewHolder(items, position, vh)

    fun onBindViewHolder(items: List<ListItem>, position: Int, vh: RecyclerView.ViewHolder,
                         payloads: List<Any>?) {
        if (payloads != null && !payloads.isEmpty()) {
            getDelegateForViewTypeOrThrowException(vh.itemViewType)
                    .onBindViewHolder(items, position, vh, payloads)
        }
    }

    fun onViewRecycled(vh: RecyclerView.ViewHolder) =
            getDelegateForViewTypeOrThrowException(vh.itemViewType).onViewRecycled(vh)

    fun onFailedToRecycleView(vh: RecyclerView.ViewHolder): Boolean =
            getDelegateForViewTypeOrThrowException(vh.itemViewType).onFailedToRecycleView(vh)

    fun onViewAttachedToWindow(vh: RecyclerView.ViewHolder) =
            getDelegateForViewTypeOrThrowException(vh.itemViewType).onViewAttachedToWindow(vh)

    fun onViewDetachedFromWindow(vh: RecyclerView.ViewHolder) =
            getDelegateForViewTypeOrThrowException(vh.itemViewType).onViewDetachedFromWindow(vh)

    private fun getDelegateForViewTypeOrThrowException(viewType: Int): AdapterDelegate {
        checkDelegateForViewType(viewType)
        return delegates[viewType]
    }

    private fun checkDelegateForViewType(viewType: Int) {
        if (delegates[viewType] == null) throw DelegateNotFoundException()
    }

    private fun getDelegateForPosition(items: List<ListItem>, position: Int): AdapterDelegate? {
        return (0 until delegates.size())
                .map { delegates[it] }
                .firstOrNull { it.isForViewType(items, position) }
    }

    private fun addDelegate(delegate: AdapterDelegate): AdapterDelegatesManager {
        var viewType = delegates.size()
        while (delegates.get(viewType) != null) {
            viewType++
            if (viewType == FALLBACK_DELEGATE_VIEW_TYPE) {
                throw IllegalArgumentException("Close to Integer.MAX_VALUE")
            }
        }
        return addDelegate(viewType, false, delegate)
    }

    private fun addDelegate(viewType: Int,
                            allowReplacingDelegate: Boolean,
                            delegate: AdapterDelegate): AdapterDelegatesManager {
        when {
            viewType == FALLBACK_DELEGATE_VIEW_TYPE ->
                throw IllegalArgumentException(
                        "View type $FALLBACK_DELEGATE_VIEW_TYPE is reserved for fallback delegate")
            !allowReplacingDelegate && delegates.get(viewType) != null ->
                throw IllegalArgumentException(
                        "AdapterDelegate is already registered for viewType $viewType")
            else -> {
                delegates.put(viewType, delegate)
                return this
            }
        }
    }
}
