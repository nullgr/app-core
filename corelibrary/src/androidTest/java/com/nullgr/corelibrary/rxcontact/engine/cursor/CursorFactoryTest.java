package com.nullgr.corelibrary.rxcontact.engine.cursor;

import android.Manifest;
import android.content.ContentResolver;
import android.database.Cursor;
import android.net.Uri;
import android.provider.ContactsContract;
import android.support.test.InstrumentationRegistry;
import android.support.test.rule.GrantPermissionRule;
import android.support.test.runner.AndroidJUnit4;

import com.nullgr.corelibrary.rxcontacts.domain.ContactEmail;
import com.nullgr.corelibrary.rxcontacts.domain.ContactPhone;
import com.nullgr.corelibrary.rxcontacts.domain.UserContact;
import com.nullgr.corelibrary.rxcontacts.engine.cursor.CursorFactory;

import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static org.junit.Assert.assertNotNull;

/**
 * Created by Grishko Nikita on 01.02.18.
 */
@SuppressWarnings("KotlinInternalInJava")
@RunWith(AndroidJUnit4.class)
public class CursorFactoryTest {

    private static ContentResolver sContentResolver;

    @BeforeClass
    public static void init() {
        sContentResolver = InstrumentationRegistry.getContext().getContentResolver();
    }

    @Rule
    public GrantPermissionRule mRuntimePermissionRule
            = GrantPermissionRule.grant(Manifest.permission.READ_CONTACTS, Manifest.permission.WRITE_CONTACTS);

    @Test
    public void testGetCursorWithSelection() {
        Cursor cursor = CursorFactory.INSTANCE.getCursor(sContentResolver, UserContact.class, (String) null);
        assertNotNull(cursor);
    }

    @Test
    public void testGetCursorWithUri() {
        Cursor cursor = CursorFactory.INSTANCE.getCursor(sContentResolver,
                ContactEmail.class,
                ContactsContract.CommonDataKinds.Email.CONTENT_URI);
        assertNotNull(cursor);
    }

    @Test
    public void testClazzToUri() {
        Uri contactUri = CursorFactory.INSTANCE.clazzToUri(UserContact.class);
        Uri emailUri = CursorFactory.INSTANCE.clazzToUri(ContactEmail.class);
        Uri phoneUri = CursorFactory.INSTANCE.clazzToUri(ContactPhone.class);

        Assert.assertEquals(contactUri, ContactsContract.Contacts.CONTENT_URI);
        Assert.assertEquals(emailUri, ContactsContract.CommonDataKinds.Email.CONTENT_URI);
        Assert.assertEquals(phoneUri, ContactsContract.CommonDataKinds.Phone.CONTENT_URI);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testUnknownClazzToUri() {
        CursorFactory.INSTANCE.clazzToUri(this.getClass());
    }
}
