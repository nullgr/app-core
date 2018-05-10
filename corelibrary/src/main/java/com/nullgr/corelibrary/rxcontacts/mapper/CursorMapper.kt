package com.nullgr.corelibrary.rxcontacts.mapper

import android.database.Cursor

/**
 * Created by Grishko Nikita on 01.02.18.
 */
interface CursorMapper<T> {

    fun map(cursor: Cursor?, vararg arguments: String): T
}