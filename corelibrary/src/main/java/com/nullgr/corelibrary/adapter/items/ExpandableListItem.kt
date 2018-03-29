package com.nullgr.corelibrary.adapter.items

/**
 * Created by Grishko Nikita on 01.02.18.
 */
interface ExpandableListItem {

    val childElements: ArrayList<ListItem>?

    var isExpanded: Boolean

    fun addChild(child: ListItem) {
        childElements?.add(child)
    }

    fun addAll(list: List<ListItem>) {
        childElements?.addAll(list)
    }

    fun clear() {
        childElements?.clear()
    }

    fun getChildList(): List<ListItem>? {
        return childElements
    }

    fun hasChildElements(): Boolean {
        return size() > 0
    }

    fun size(): Int {
        return getChildList()?.size ?: 0
    }

    enum class Payload {
        EXPAND_STATE_CHANGED
    }
}