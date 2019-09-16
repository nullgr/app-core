package com.nullgr.core.rx.contacts.engine.cursor

import android.content.ContentResolver
import android.database.Cursor
import android.net.Uri
import android.provider.ContactsContract
import com.nullgr.core.rx.contacts.domain.ContactEmail
import com.nullgr.core.rx.contacts.domain.ContactPhone
import com.nullgr.core.rx.contacts.domain.UserContact

/**
 * Inner factory class that provides [Cursor] for different cases
 * @author Grishko Nikita
 */
internal object CursorFactory {

    /**
     * Provides cursor for given [clazz] with specific [selectionString]
     * @param contentResolver instance of [ContentResolver]
     * @param clazz one of [ContactEmail], [ContactPhone] or [UserContact].
     * @param selectionString custom WHERE string
     * @return instance of [Cursor]
     */
    fun getCursor(contentResolver: ContentResolver, clazz: Class<*>, selectionString: String?): Cursor {
        return contentResolver.query(clazzToUri(clazz),
            ProjectionFactory.getProjectionForClazz(clazz),
            selectionString,
            null,
            null)!!
    }

    /**
     * Provides cursor for given [uri] with projection specified by [clazz]
     * @param contentResolver instance of [ContentResolver]
     * @param clazz one of [ContactEmail], [ContactPhone] or [UserContact].
     * @param uri uri to fetch data from [ContentResolver]
     * @return instance of [Cursor]
     */
    fun getCursor(contentResolver: ContentResolver, clazz: Class<*>, uri: Uri): Cursor {
        return contentResolver.query(uri,
            ProjectionFactory.getProjectionForClazz(clazz),
            null,
            null,
            null)!!
    }

    /**
     * Map given [clazz] to specific [Uri]
     * @param clazz one of [ContactEmail], [ContactPhone] or [UserContact].
     * @throws IllegalArgumentException if any other class will be passed as param
     * @return [Uri] from [ContactsContract]
     */
    fun clazzToUri(clazz: Class<*>): Uri = when (clazz) {
        UserContact::class.java -> ContactsContract.Contacts.CONTENT_URI
        ContactPhone::class.java -> ContactsContract.CommonDataKinds.Phone.CONTENT_URI
        ContactEmail::class.java -> ContactsContract.CommonDataKinds.Email.CONTENT_URI
        else -> throw IllegalArgumentException("Can't pick uri for unknown class $clazz")
    }
}