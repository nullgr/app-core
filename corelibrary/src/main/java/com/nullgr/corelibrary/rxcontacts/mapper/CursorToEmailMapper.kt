package com.nullgr.corelibrary.rxcontacts.mapper

import android.database.Cursor
import android.provider.ContactsContract
import com.nullgr.corelibrary.rxcontacts.domain.ContactEmail
import com.nullgr.corelibrary.rxcontacts.extensions.has

/**
 * Map given cursor to collection of unique [ContactEmail]
 *
 * @author Grishko Nikita
 */
internal object CursorToEmailMapper : CursorMapper<List<ContactEmail>> {

    override fun map(cursor: Cursor?, vararg arguments: String): List<ContactEmail> {
        cursor?.let {
            val emailResult = arrayListOf<ContactEmail>()
            cursor.use { cursor ->
                if (cursor.moveToFirst()) {
                    do {
                        val id = cursor.getInt(
                                cursor.getColumnIndex(ContactsContract.CommonDataKinds.Email._ID))
                        if (!(emailResult has id)) {
                            val displayName = cursor.getString(
                                    cursor.getColumnIndex(ContactsContract.Data.DISPLAY_NAME))
                            val isStarred = cursor.getInt(
                                    cursor.getColumnIndex(ContactsContract.CommonDataKinds.Email.STARRED)) != 0
                            val email = cursor.getString(
                                    cursor.getColumnIndex(ContactsContract.CommonDataKinds.Email.DATA1))

                            val contactEmail = ContactEmail(id.toLong(),
                                    displayName,
                                    isStarred,
                                    email)

                            emailResult.add(contactEmail)
                        }
                    } while (cursor.moveToNext())
                }
            }
            return emailResult
        }
        return emptyList()
    }
}