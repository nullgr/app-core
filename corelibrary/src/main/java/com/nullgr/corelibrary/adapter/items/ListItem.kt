package com.nullgr.corelibrary.adapter.items

/**
 * author a.komarovskyi
 */
abstract class ListItem {

    open fun areItemsTheSame(other: ListItem): Boolean {
        return this::class == other::class && this.getUniqueProperty() == other.getUniqueProperty()
    }

    open fun areContentsTheSame(other: ListItem): Boolean {
        return this == other
    }

    open fun getChangePayload(other: ListItem): Any {
        return Unit
    }

    open fun getUniqueProperty(): String {
        return this::class.toString()
    }
}