package com.nullgr.corelibrary.adapter

import com.nullgr.corelibrary.adapter.items.ListItem

/**
 * Factory interface that creates [AdapterDelegate]s by class of [ListItem].
 *
 * @author a.komarovskyi
 */
interface AdapterDelegatesFactory {
    fun createDelegate(clazz: Class<ListItem>): AdapterDelegate
}