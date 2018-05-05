package com.nullgr.androidcore.adapter.items

import com.nullgr.corelibrary.adapter.items.ListItem

/**
 * @author vchernyshov.
 */
data class ExampleItem1(val icon: String, val text: String): ListItem {
    override fun getUniqueProperty(): Any = text
}