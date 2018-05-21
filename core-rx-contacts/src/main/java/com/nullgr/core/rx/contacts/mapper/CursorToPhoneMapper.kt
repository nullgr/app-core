package com.nullgr.core.rx.contacts.mapper

import android.database.Cursor
import android.provider.ContactsContract
import com.nullgr.core.rx.contacts.domain.ContactPhone
import com.nullgr.core.rx.contacts.extensions.has

/**
 * Map given cursor to collection of unique [ContactPhone]
 *
 * @author Grishko Nikita
 */
internal object CursorToPhonesMapper : CursorMapper<List<ContactPhone>> {

    override fun map(cursor: Cursor?, vararg arguments: String): List<ContactPhone> {
        cursor?.let {
            val phonesResult = arrayListOf<ContactPhone>()
            cursor.use { cursor ->
                if (cursor.moveToFirst()) {
                    do {
                        val id = cursor.getInt(
                                cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone._ID))
                        if (!(phonesResult has id)) {
                            val displayName = cursor.getString(
                                    cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME))
                            val isStarred = cursor.getInt(
                                    cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.STARRED)) != 0
                            val phone = cursor.getString(
                                    cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER))
                            val normalizedPhone = cursor.getString(
                                    cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NORMALIZED_NUMBER))

                            val contactPhone = ContactPhone(id.toLong(),
                                    displayName,
                                    isStarred,
                                    phone,
                                    normalizedPhone)

                            phonesResult.add(contactPhone)
                        }
                    } while (cursor.moveToNext())
                }
            }
            return phonesResult
        }
        return emptyList()
    }
}