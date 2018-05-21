package com.nullgr.androidcore.interactor.presentation.adapter

import com.nullgr.androidcore.interactor.presentation.adapter.delegates.InteractorDelegate
import com.nullgr.androidcore.interactor.presentation.adapter.items.InteractorItem
import com.nullgr.core.adapter.AdapterDelegate
import com.nullgr.core.adapter.AdapterDelegatesFactory
import com.nullgr.core.adapter.items.ListItem
import com.nullgr.core.rx.RxBus

class InteractorDelegatesFactory(private val bus: RxBus) : AdapterDelegatesFactory {

    override fun createDelegate(clazz: Class<ListItem>): AdapterDelegate =
            when (clazz) {
                InteractorItem::class.java -> InteractorDelegate(bus)
                else -> throw IllegalArgumentException("No delegate defined for ${clazz.simpleName}")
            }
}