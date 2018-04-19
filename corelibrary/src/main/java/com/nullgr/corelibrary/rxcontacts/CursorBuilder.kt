package com.nullgr.corelibrary.rxcontacts

import android.content.ContentResolver
import android.database.Cursor
import android.net.Uri
import android.provider.ContactsContract
import com.nullgr.corelibrary.collections.isNullOrEmpty

/**
 * Created by Grishko Nikita on 01.02.18.
 */
internal class CursorBuilder(private val contentResolver: ContentResolver) {

    companion object {
        private val PROJECTION = arrayOf(ContactsContract.Contacts._ID,
                ContactsContract.Contacts.LOOKUP_KEY,
                ContactsContract.Contacts.DISPLAY_NAME_PRIMARY,
                ContactsContract.Contacts.STARRED,
                ContactsContract.Contacts.PHOTO_URI,
                ContactsContract.Contacts.PHOTO_THUMBNAIL_URI,
                ContactsContract.Contacts.HAS_PHONE_NUMBER)
    }

    fun buildContactsCursor(selection: String? = null, selectionArgs: Array<out String>? = null): Cursor {
        return contentResolver.query(
                ContactsContract.Contacts.CONTENT_URI, PROJECTION,
                buildSelectionString(selection, selectionArgs), null,
                ContactsContract.Contacts._ID
        )
    }

    fun buildDataKindsCursorForContactId(dataKindsUri: Uri, dataKindsSelectionKey: String, contactId: String): Cursor {
        return contentResolver.query(
                dataKindsUri,
                null,
                "$dataKindsSelectionKey = $contactId", null,
                null)
    }

    private fun buildSelectionString(selection: String? = null, selectionArgs: Array<out String>? = null): String? {
        if (selection.isNullOrEmpty() || selectionArgs.isNullOrEmpty()) return null
        if (selectionArgs?.size == 1) return "$selection = ${selectionArgs[0]}"
        return "$selection ${selectionArgs?.joinToString(separator = ",",
                prefix = "IN (",
                postfix = ")",
                transform = { "'$it'" })}"
    }
}