package com.nullgr.androidcore.adapter.items

import com.nullgr.core.adapter.items.ListItem

/**
 * @author vchernyshov.
 */
data class ExampleItem2(
    val icon: String,
    val text1: String,
    val text2: String,
    override val uniqueProperty: Any = text1
) : ListItem