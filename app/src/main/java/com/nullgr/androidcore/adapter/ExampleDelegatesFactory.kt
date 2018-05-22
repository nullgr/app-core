package com.nullgr.androidcore.adapter

import com.nullgr.androidcore.adapter.delegates.ExampleDelegate1
import com.nullgr.androidcore.adapter.delegates.ExampleDelegate2
import com.nullgr.androidcore.adapter.delegates.ExampleDelegate3
import com.nullgr.androidcore.adapter.delegates.ExampleDelegateWithPayloads
import com.nullgr.androidcore.adapter.items.ExampleItem1
import com.nullgr.androidcore.adapter.items.ExampleItem2
import com.nullgr.androidcore.adapter.items.ExampleItem3
import com.nullgr.androidcore.adapter.items.ExampleItemWithPayloads
import com.nullgr.core.adapter.AdapterDelegate
import com.nullgr.core.adapter.AdapterDelegatesFactory
import com.nullgr.core.adapter.items.ListItem
import com.nullgr.core.rx.RxBus

class ExampleDelegatesFactory(val bus: RxBus) : AdapterDelegatesFactory {

    override fun createDelegate(clazz: Class<ListItem>): AdapterDelegate =
            when (clazz) {
                ExampleItem1::class.java -> ExampleDelegate1()
                ExampleItem2::class.java -> ExampleDelegate2()
                ExampleItem3::class.java -> ExampleDelegate3()
                ExampleItemWithPayloads::class.java -> ExampleDelegateWithPayloads(bus)
                else -> throw IllegalArgumentException("No delegate defined for ${clazz.simpleName}")
            }
}