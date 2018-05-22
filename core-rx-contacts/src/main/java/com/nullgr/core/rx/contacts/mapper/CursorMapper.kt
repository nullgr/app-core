package com.nullgr.core.rx.contacts.mapper

import android.database.Cursor

/**
 * Base interface for inner mappers
 *
 * @author Grishko Nikita
 */
interface CursorMapper<T> {

    /**
     * Map cursor to any object
     * @param cursor [Cursor]
     * @param arguments optional arguments which can be used while mapping
     */
    fun map(cursor: Cursor?, vararg arguments: String): T
}