package com.nullgr.corelibrary.rxcontacts.engine.cursor

import android.provider.ContactsContract
import android.support.annotation.VisibleForTesting
import com.nullgr.corelibrary.rxcontacts.domain.ContactEmail
import com.nullgr.corelibrary.rxcontacts.domain.ContactPhone
import com.nullgr.corelibrary.rxcontacts.domain.UserContact

/**
 * Created by Grishko Nikita on 01.02.18.
 */
@VisibleForTesting
internal object ProjectionFactory {

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
            ContactsContract.CommonDataKinds.Phone.STARRED,
            ContactsContract.CommonDataKinds.Phone.NORMALIZED_NUMBER,
            ContactsContract.CommonDataKinds.Phone.CONTACT_ID
    )

    private val PROJECTION_EMAILS = arrayOf(
            ContactsContract.CommonDataKinds.Email._ID,
            ContactsContract.Data.DISPLAY_NAME,
            ContactsContract.CommonDataKinds.Email.STARRED,
            ContactsContract.CommonDataKinds.Email.DATA,
            ContactsContract.CommonDataKinds.Email.CONTACT_ID
    )

    fun getProjectionForClazz(clazz: Class<*>): Array<String> =
            when (clazz) {
                UserContact::class.java -> PROJECTION_CONTACT
                ContactPhone::class.java -> PROJECTION_PHONES
                ContactEmail::class.java -> PROJECTION_EMAILS
                else -> throw IllegalArgumentException("Cannot find projection for class: $clazz")
            }
}