package com.nullgr.corelibrary.rxcontacts.mapper

import android.database.Cursor

/**
 * Created by Grishko Nikita on 01.02.18.
 */
internal object CursorToContactIdsMapper : CursorMapper<List<String>> {

    override fun map(cursor: Cursor?, vararg arguments: String): List<String> {
        cursor?.let {
            val result = arrayListOf<String>()
            cursor.use { cursor ->
                if (cursor.moveToFirst()) {
                    do {
                        val id = cursor.getString(cursor.getColumnIndex(arguments[0]))
                        if (id !in result) {
                            result.add(id)
                        }
                    } while (cursor.moveToNext())
                }
            }
            return result
        }
        return emptyList()
    }
}