package com.nullgr.androidcore.adapter.items

import com.nullgr.core.adapter.items.ListItem

/**
 * @author vchernyshov.
 */
data class ExampleItem3(val icon1: String, val text: String, val icon2: String) : ListItem {
    override fun getUniqueProperty(): Any = text
}