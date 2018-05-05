package com.nullgr.androidcore.adapter

import com.nullgr.androidcore.adapter.delegates.ExampleDelegate1
import com.nullgr.androidcore.adapter.delegates.ExampleDelegate2
import com.nullgr.androidcore.adapter.delegates.ExampleDelegate3
import com.nullgr.androidcore.adapter.items.ExampleItem1
import com.nullgr.androidcore.adapter.items.ExampleItem2
import com.nullgr.androidcore.adapter.items.ExampleItem3
import com.nullgr.corelibrary.adapter.AdapterDelegate
import com.nullgr.corelibrary.adapter.AdapterDelegatesFactory
import com.nullgr.corelibrary.adapter.items.ListItem

class ExampleDelegatesFactory : AdapterDelegatesFactory {

    override fun createDelegate(clazz: Class<ListItem>): AdapterDelegate =
            when (clazz) {
                ExampleItem1::class.java -> ExampleDelegate1()
                ExampleItem2::class.java -> ExampleDelegate2()
                ExampleItem3::class.java -> ExampleDelegate3()
                else -> throw IllegalArgumentException("No delegate defined for ${clazz.simpleName}")
            }
}