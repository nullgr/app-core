package com.nullgr.corelibrary.rxcontact.engine.cursor;

import android.provider.ContactsContract;
import android.support.test.runner.AndroidJUnit4;

import com.nullgr.corelibrary.rxcontacts.domain.ContactEmail;
import com.nullgr.corelibrary.rxcontacts.domain.ContactPhone;
import com.nullgr.corelibrary.rxcontacts.domain.UserContact;
import com.nullgr.corelibrary.rxcontacts.engine.cursor.ProjectionFactory;

import junit.framework.Assert;

import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.Arrays;

/**
 * Created by Grishko Nikita on 01.02.18.
 */
@SuppressWarnings("KotlinInternalInJava")
@RunWith(AndroidJUnit4.class)
public class ProjectionFactoryTest {

    private String[] userContactExpectedProjection = {ContactsContract.Contacts._ID,
            ContactsContract.Contacts.LOOKUP_KEY,
            ContactsContract.Contacts.DISPLAY_NAME_PRIMARY,
            ContactsContract.Contacts.STARRED,
            ContactsContract.Contacts.PHOTO_URI,
            ContactsContract.Contacts.PHOTO_THUMBNAIL_URI,
            ContactsContract.Contacts.HAS_PHONE_NUMBER};

    private String[] contactPhoneExpectedProjection = {ContactsContract.CommonDataKinds.Phone._ID,
            ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME,
            ContactsContract.CommonDataKinds.Phone.NUMBER,
            ContactsContract.CommonDataKinds.Phone.STARRED,
            ContactsContract.CommonDataKinds.Phone.NORMALIZED_NUMBER,
            ContactsContract.CommonDataKinds.Phone.CONTACT_ID};

    private String[] contactEmailExpectedProjection = {ContactsContract.CommonDataKinds.Email._ID,
            ContactsContract.Data.DISPLAY_NAME,
            ContactsContract.CommonDataKinds.Email.STARRED,
            ContactsContract.CommonDataKinds.Email.DATA,
            ContactsContract.CommonDataKinds.Email.CONTACT_ID};

    @Test
    public void getProjectionForClazz_UserContact_Success() {
        String[] projection = ProjectionFactory.INSTANCE.getProjectionForClazz(UserContact.class);
        Assert.assertNotNull(projection);
        Assert.assertTrue(Arrays.equals(userContactExpectedProjection, projection));
    }

    @Test
    public void getProjectionForClazz_ContactPhone_Success() {
        String[] projection = ProjectionFactory.INSTANCE.getProjectionForClazz(ContactPhone.class);
        Assert.assertNotNull(projection);
        Assert.assertTrue(Arrays.equals(contactPhoneExpectedProjection, projection));
    }

    @Test
    public void getProjectionForClazz_ContactEmail_Success() {
        String[] projection = ProjectionFactory.INSTANCE.getProjectionForClazz(ContactEmail.class);
        Assert.assertNotNull(projection);
        Assert.assertTrue(Arrays.equals(contactEmailExpectedProjection, projection));
    }

    @Test(expected = IllegalArgumentException.class)
    public void getProjectionForClazz_AnyOtherClass_Fails() {
        ProjectionFactory.INSTANCE.getProjectionForClazz(this.getClass());
    }
}
