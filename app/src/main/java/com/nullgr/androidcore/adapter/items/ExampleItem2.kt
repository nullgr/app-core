package com.nullgr.androidcore.adapter.items

import com.nullgr.corelibrary.adapter.items.ListItem

/**
 * @author vchernyshov.
 */
data class ExampleItem2(val icon: String, val text1: String, val text2: String) : ListItem {
    override fun getUniqueProperty(): Any = text1
}