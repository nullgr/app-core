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
        private val PROJECTION_CONTACT = arrayOf(ContactsContract.Contacts._ID,
                ContactsContract.Contacts.LOOKUP_KEY,
                ContactsContract.Contacts.DISPLAY_NAME_PRIMARY,
                ContactsContract.Contacts.STARRED,
                ContactsContract.Contacts.PHOTO_URI,
                ContactsContract.Contacts.PHOTO_THUMBNAIL_URI,
                ContactsContract.Contacts.HAS_PHONE_NUMBER)

        private val PROJECTION_PHONES = arrayOf(
                ContactsContract.CommonDataKinds.Phone._ID,
                ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME,
                ContactsContract.CommonDataKinds.Phone.NUMBER,
                ContactsContract.CommonDataKinds.Phone.NORMALIZED_NUMBER,
                ContactsContract.CommonDataKinds.Phone.CONTACT_ID
        )

        private val PROJECTION_EMAILS = arrayOf(
                ContactsContract.CommonDataKinds.Email._ID,
                ContactsContract.CommonDataKinds.Email.DISPLAY_NAME,
                ContactsContract.CommonDataKinds.Email.DATA,
                ContactsContract.CommonDataKinds.Email.CONTACT_ID
        )
    }

    fun buildContactsCursorBySpecificUri(uri: Uri): Cursor {
        return contentResolver.query(
                uri, PROJECTION_CONTACT,
                null, null,
                ContactsContract.Contacts._ID
        )
    }

    fun buildContactsCursor(selection: String? = null, selectionArgs: Array<out String>? = null): Cursor {
        return contentResolver.query(
                ContactsContract.Contacts.CONTENT_URI, PROJECTION_CONTACT,
                buildSelectionString(selection, selectionArgs), null,
                ContactsContract.Contacts._ID
        )
    }

    fun buildDataKindCursorBySpecificUri(uri: Uri, supportedDataKinds: SupportedDataKinds): Cursor {
        return contentResolver.query(
                uri, supportedDataKinds.projection,
                null, null, null
        )
    }

    fun buildDataKindsCursorForContactId(dataKind: SupportedDataKinds, contactId: String): Cursor {
        return buildGeneralDataKindsCursor(dataKind, dataKind.contactIdSelection, arrayOf(contactId))
    }

    fun buildGeneralDataKindsCursor(dataKind: SupportedDataKinds,
                                    selection: String? = null,
                                    selectionArgs: Array<out String>? = null): Cursor {
        return contentResolver.query(dataKind.uri,
                dataKind.projection,
                buildSelectionString(selection, selectionArgs), null,
                null)
    }

    private fun buildSelectionString(selection: String? = null,
                                     selectionArgs: Array<out String>? = null): String? {
        if (selection.isNullOrEmpty() || selectionArgs.isNullOrEmpty()) return null
        if (selectionArgs?.size == 1) return "$selection = '${selectionArgs[0]}'"
        return "$selection ${selectionArgs?.joinToString(separator = ",",
                prefix = "IN (",
                postfix = ")",
                transform = { "'$it'" })}"
    }

    enum class SupportedDataKinds(val uri: Uri, val projection: Array<String>, val contactIdSelection: String) {
        PHONE(ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
                PROJECTION_PHONES,
                ContactsContract.CommonDataKinds.Phone.CONTACT_ID),
        EMAIL(ContactsContract.CommonDataKinds.Email.CONTENT_URI,
                PROJECTION_EMAILS,
                ContactsContract.CommonDataKinds.Email.CONTACT_ID)
    }
}