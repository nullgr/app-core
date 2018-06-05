package com.nullgr.androidcore.rxcontacts.items

import com.nullgr.core.adapter.items.ListItem

/**
 * Created by Grishko Nikita on 01.02.18.
 */
data class HeaderItem(val title: String) : ListItem {
    override fun getUniqueProperty() = title
}