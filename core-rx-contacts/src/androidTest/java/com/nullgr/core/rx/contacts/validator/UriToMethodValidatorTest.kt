package com.nullgr.core.rx.contacts.validator

import android.provider.ContactsContract
import androidx.test.ext.junit.runners.AndroidJUnit4
import org.junit.Test
import org.junit.runner.RunWith

/**
 * @author Grishko Nikita
 */
@RunWith(AndroidJUnit4::class)
class UriToMethodValidatorTest {

    @Test
    fun validateUriToPickContact_CorrectUri_Success() {
        val validUri = ContactsContract.Contacts.getLookupUri(1, "someKey")
        UriToMethodValidator.validateUriToPickContact(validUri)
    }

    @Test(expected = IllegalArgumentException::class)
    fun validateUriToPickContact_NotCorrectUri_Fails() {
        val validUri = ContactsContract.CommonDataKinds.Email.CONTENT_URI
        UriToMethodValidator.validateUriToPickContact(validUri)
    }

    @Test
    fun validateUriToPickPhoneOrEmailData_CorrectUri_Success() {
        val validUri1 = ContactsContract.CommonDataKinds.Email.CONTENT_URI
        UriToMethodValidator.validateUriToPickPhoneOrEmailData(validUri1)

        val validUri2 = ContactsContract.CommonDataKinds.Phone.CONTENT_URI
        UriToMethodValidator.validateUriToPickPhoneOrEmailData(validUri2)
    }

    @Test(expected = IllegalArgumentException::class)
    fun validateUriToPickPhoneOrEmailData_NotCorrectUri_Fails() {
        val notValidUri = ContactsContract.Contacts.getLookupUri(1, "someKey")
        UriToMethodValidator.validateUriToPickPhoneOrEmailData(notValidUri)
    }
}
