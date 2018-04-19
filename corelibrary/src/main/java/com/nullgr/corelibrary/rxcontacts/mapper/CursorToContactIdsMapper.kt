package com.nullgr.corelibrary.rxcontacts.mapper

import android.database.Cursor

/**
 * Created by Grishko Nikita on 01.02.18.
 */
internal object CursorToContactIdsMapper {

    fun map(cursor: Cursor?, contactIdRowName: String): List<String> {
        cursor?.let {
            val result = arrayListOf<String>()
            cursor.use { cursor ->
                if (cursor.moveToFirst()) {
                    do {
                        val id = cursor.getString(cursor.getColumnIndex(contactIdRowName))
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