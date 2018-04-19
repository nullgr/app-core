package com.nullgr.corelibrary.rxcontacts.mapper

import android.database.Cursor
import android.provider.ContactsContract

/**
 * Created by Grishko Nikita on 01.02.18.
 */
internal object CursorToDataKindsMapper {

    fun mapPhones(cursor: Cursor?): List<String> {
        cursor?.let {
            val phonesResult = arrayListOf<String>()
            cursor.use { cursor ->
                if (cursor.moveToFirst()) {
                    do {
                        val phone = cursor.getString(cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER))
                        if (phone !in phonesResult) {
                            phonesResult.add(phone)
                        }
                    } while (cursor.moveToNext())
                }
            }
            return phonesResult
        }
        return emptyList()
    }

    fun mapEmails(cursor: Cursor?): List<String> {
        cursor?.let {
            val emailsResult = arrayListOf<String>()
            cursor.use { cursor ->
                if (cursor.moveToFirst()) {
                    do {
                        val email = cursor.getString(cursor.getColumnIndex(ContactsContract.CommonDataKinds.Email.DATA))
                        if (email !in emailsResult) {
                            emailsResult.add(email)
                        }
                    } while (cursor.moveToNext())
                }
            }
            return emailsResult
        }
        return emptyList()
    }
}