package com.nullgr.corelibrary.rxcontacts.mapper

import android.database.Cursor
import android.provider.ContactsContract
import com.nullgr.corelibrary.rxcontacts.domain.ContactPhone
import com.nullgr.corelibrary.rxcontacts.extensions.has

/**
 * Created by Grishko Nikita on 01.02.18.
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
                            val phone = cursor.getString(
                                    cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER))
                            val normalizedPhone = cursor.getString(
                                    cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NORMALIZED_NUMBER))
                            val contactPhone = ContactPhone(id.toLong(), displayName, phone, normalizedPhone)
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