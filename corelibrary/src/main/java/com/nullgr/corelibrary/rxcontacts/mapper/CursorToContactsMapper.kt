package com.nullgr.corelibrary.rxcontacts.mapper

import android.database.Cursor
import android.net.Uri
import android.provider.ContactsContract
import com.nullgr.corelibrary.rxcontacts.domain.Contact
import java.util.*


/**
 * Created by Grishko Nikita on 01.02.18.
 */
internal object CursorToContactsMapper {

    fun map(cursor: Cursor?): List<Contact> {
        cursor?.let {
            val contactsResult = arrayListOf<Contact>()
            cursor.use { cursor ->
                if (cursor.moveToFirst()) {
                    do {
                        val contactId = getId(cursor)

                        if (!(contactsResult has contactId)) {

                            contactsResult.add(Contact(contactId.toLong(),
                                    getLookUpKey(cursor),
                                    getDisplayName(cursor),
                                    getStarred(cursor),
                                    getPhoto(cursor),
                                    getThumbnail(cursor),
                                    getHasPhones(cursor),
                                    null,
                                    null))
                        }
                    } while (cursor.moveToNext())
                }
            }
            return contactsResult
        }
        return Collections.emptyList()
    }

    private fun getId(cursor: Cursor): Int {
        return cursor.getInt(cursor.getColumnIndex(ContactsContract.Contacts._ID))
    }

    private fun getStarred(cursor: Cursor): Boolean {
        return cursor.getInt(cursor.getColumnIndex(ContactsContract.Contacts.STARRED)) != 0
    }

    private fun getLookUpKey(cursor: Cursor): String? {
        return cursor.getString(cursor.getColumnIndex(ContactsContract.Contacts.LOOKUP_KEY))
    }

    private fun getDisplayName(cursor: Cursor): String? {
        return cursor.getString(cursor.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME_PRIMARY))
    }

    private fun getPhoto(cursor: Cursor): Uri? {
        val uri = cursor.getString(cursor.getColumnIndex(ContactsContract.Contacts.PHOTO_URI))
        if (uri != null && !uri.isEmpty()) {
            return Uri.parse(uri)
        }
        return null
    }

    private fun getThumbnail(cursor: Cursor): Uri? {
        val uri = cursor.getString(cursor.getColumnIndex(ContactsContract.Contacts.PHOTO_THUMBNAIL_URI))
        if (uri != null && !uri.isEmpty()) {
            return Uri.parse(uri)
        }
        return null
    }

    private fun getHasPhones(cursor: Cursor): Boolean {
        return cursor.getInt(cursor.getColumnIndex(ContactsContract.Contacts.HAS_PHONE_NUMBER)) != 0
    }

    private infix fun List<Contact>.has(id: Int): Boolean {
        return this.any { it.id == id.toLong() }
    }
}