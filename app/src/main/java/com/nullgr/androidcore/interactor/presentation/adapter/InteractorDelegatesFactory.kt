package com.nullgr.androidcore.interactor.presentation.adapter

import com.nullgr.androidcore.interactor.presentation.adapter.delegates.InteractorDelegate
import com.nullgr.androidcore.interactor.presentation.adapter.items.InteractorItem
import com.nullgr.corelibrary.adapter.AdapterDelegate
import com.nullgr.corelibrary.adapter.AdapterDelegatesFactory
import com.nullgr.corelibrary.adapter.items.ListItem
import com.nullgr.corelibrary.rx.RxBus

class InteractorDelegatesFactory(private val bus: RxBus) : AdapterDelegatesFactory {

    override fun createDelegate(clazz: Class<ListItem>): AdapterDelegate =
            when (clazz) {
                InteractorItem::class.java -> InteractorDelegate(bus)
                else -> throw IllegalArgumentException("No delegate defined for ${clazz.simpleName}")
            }
}