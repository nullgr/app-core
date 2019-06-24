package com.nullgr.core.adapter.items

interface ParentItem : ListItem {
    val items: List<ListItem>
}