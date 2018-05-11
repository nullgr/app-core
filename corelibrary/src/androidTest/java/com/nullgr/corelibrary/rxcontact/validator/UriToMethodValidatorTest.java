package com.nullgr.corelibrary.rxcontact.validator;

import android.net.Uri;
import android.provider.ContactsContract;
import android.support.test.runner.AndroidJUnit4;

import com.nullgr.corelibrary.rxcontacts.validator.UriToMethodValidator;

import org.junit.Test;
import org.junit.runner.RunWith;

/**
 * Created by Grishko Nikita on 01.02.18.
 */
@SuppressWarnings("KotlinInternalInJava")
@RunWith(AndroidJUnit4.class)
public class UriToMethodValidatorTest {

    @Test
    public void testCorrectUriForUserContacts() {
        Uri validUri = ContactsContract.Contacts.getLookupUri(1, "someKey");
        UriToMethodValidator.INSTANCE.validateUriToPickContact(validUri);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testNotCorrectUriForUserContacts() {
        Uri validUri = ContactsContract.CommonDataKinds.Email.CONTENT_URI;
        UriToMethodValidator.INSTANCE.validateUriToPickContact(validUri);
    }

    @Test
    public void testCorrectUriForDataKindContacts() {
        Uri validUri1 = ContactsContract.CommonDataKinds.Email.CONTENT_URI;
        UriToMethodValidator.INSTANCE.validateUriToPickPhoneOrEmailData(validUri1);

        Uri validUri2 = ContactsContract.CommonDataKinds.Phone.CONTENT_URI;
        UriToMethodValidator.INSTANCE.validateUriToPickPhoneOrEmailData(validUri2);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testNotCorrectUriForDataKindContacts() {
        Uri validUri = ContactsContract.Contacts.getLookupUri(1, "someKey");
        UriToMethodValidator.INSTANCE.validateUriToPickPhoneOrEmailData(validUri);
    }
}
