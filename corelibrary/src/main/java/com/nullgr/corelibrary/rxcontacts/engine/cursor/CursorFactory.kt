package com.nullgr.corelibrary.rxcontacts.engine.cursor

import android.content.ContentResolver
import android.database.Cursor
import android.net.Uri
import android.provider.ContactsContract
import com.nullgr.corelibrary.rxcontacts.domain.ContactEmail
import com.nullgr.corelibrary.rxcontacts.domain.ContactPhone
import com.nullgr.corelibrary.rxcontacts.domain.UserContact

/**
 * Created by Grishko Nikita on 01.02.18.
 */
internal object CursorFactory {

    fun getCursor(contentResolver: ContentResolver, clazz: Class<*>, selectionString: String?): Cursor {
        return contentResolver.query(clazzToUri(clazz),
                ProjectionFactory.getProjectionForClazz(clazz),
                selectionString,
                null,
                null)
    }

    fun getCursor(contentResolver: ContentResolver, clazz: Class<*>, uri: Uri): Cursor {
        return contentResolver.query(uri,
                ProjectionFactory.getProjectionForClazz(clazz),
                null,
                null,
                null)
    }

    private fun clazzToUri(clazz: Class<*>): Uri = when (clazz) {
        UserContact::class.java -> ContactsContract.Contacts.CONTENT_URI
        ContactPhone::class.java -> ContactsContract.CommonDataKinds.Phone.CONTENT_URI
        ContactEmail::class.java -> ContactsContract.CommonDataKinds.Email.CONTENT_URI
        else -> throw IllegalArgumentException("Can't pick uri for unknown class $clazz")
    }
}