package com.nullgr.core.adapter

import com.nullgr.core.adapter.items.ListItem

/**
 * Factory interface that creates [AdapterDelegate]s by class of [ListItem].
 *
 * @author a.komarovskyi
 */
interface AdapterDelegatesFactory {
    fun createDelegate(clazz: Class<ListItem>): AdapterDelegate
}