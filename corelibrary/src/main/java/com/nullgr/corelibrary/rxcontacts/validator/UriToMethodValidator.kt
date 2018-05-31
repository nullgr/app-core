package com.nullgr.corelibrary.rxcontacts.validator

import android.net.Uri
import android.provider.ContactsContract

/**
 * Validator to check if is the correct uri passed to pick specific type of contact data
 * @author Grishko Nikita
 */
internal object UriToMethodValidator {

    @Throws(IllegalArgumentException::class)
    fun validateUriToPickContact(uri: Uri) {
        if (ContactsContract.Contacts.CONTENT_URI.lastPathSegment !in uri.pathSegments)
            throw IllegalArgumentException("Invalid uri: $uri to pick contact data. " +
                    "Should be uri specified by {${ContactsContract.Contacts.CONTENT_URI}}")
    }

    @Throws(IllegalArgumentException::class)
    fun validateUriToPickPhoneOrEmailData(uri: Uri) {
        if (ContactsContract.Data.CONTENT_URI.lastPathSegment !in uri.pathSegments)
            throw IllegalArgumentException("Invalid uri: $uri to pick contact data. " +
                    "Should be uri specified by {${ContactsContract.Data.CONTENT_URI}}")
    }
}