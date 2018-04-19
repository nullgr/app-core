package com.nullgr.corelibrary.rxcontacts.validator

import android.net.Uri
import android.provider.ContactsContract

/**
 * Created by Grishko Nikita on 01.02.18.
 */
object UriToMethodValidator {

    @Throws(IllegalArgumentException::class)
    fun validateUriToPickContact(uri: Uri) {
        if (ContactsContract.Contacts.CONTENT_URI.lastPathSegment !in uri.pathSegments)
            throw IllegalArgumentException("Invalid uri: $uri to pick contact data. " +
                    "Should by uri specified by {${ContactsContract.Contacts.CONTENT_URI}}")
    }


    @Throws(IllegalArgumentException::class)
    fun validateUriToPickPhoneOrEmailData(uri: Uri) {
        if (ContactsContract.Data.CONTENT_URI.lastPathSegment !in uri.pathSegments)
            throw IllegalArgumentException("Invalid uri: $uri to pick contact data. " +
                    "Should by uri specified by {${ContactsContract.Data.CONTENT_URI}}")
    }
}