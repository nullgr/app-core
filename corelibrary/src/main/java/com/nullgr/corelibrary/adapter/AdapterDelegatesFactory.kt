package com.nullgr.corelibrary.adapter

import com.nullgr.corelibrary.adapter.items.ListItem

/**
 * author a.komarovskyi
 */
interface AdapterDelegatesFactory {
    fun createDelegate(clazz: Class<ListItem>): AdapterDelegate
}