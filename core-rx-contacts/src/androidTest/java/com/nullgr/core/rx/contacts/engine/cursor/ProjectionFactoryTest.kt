package com.nullgr.core.rx.contacts.engine.cursor

import android.provider.ContactsContract
import android.support.test.runner.AndroidJUnit4
import com.nullgr.core.rx.contacts.domain.ContactEmail
import com.nullgr.core.rx.contacts.domain.ContactPhone
import com.nullgr.core.rx.contacts.domain.UserContact

import junit.framework.Assert

import org.junit.Test
import org.junit.runner.RunWith

import java.util.Arrays

/**
 * @author Grishko Nikita
 */
@RunWith(AndroidJUnit4::class)
class ProjectionFactoryTest {

    private val userContactExpectedProjection = arrayOf(
            ContactsContract.Contacts._ID,
            ContactsContract.Contacts.LOOKUP_KEY,
            ContactsContract.Contacts.DISPLAY_NAME_PRIMARY,
            ContactsContract.Contacts.STARRED,
            ContactsContract.Contacts.PHOTO_URI,
            ContactsContract.Contacts.PHOTO_THUMBNAIL_URI,
            ContactsContract.Contacts.HAS_PHONE_NUMBER
    )

    private val contactPhoneExpectedProjection = arrayOf(
            ContactsContract.CommonDataKinds.Phone._ID,
            ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME,
            ContactsContract.CommonDataKinds.Phone.NUMBER,
            ContactsContract.CommonDataKinds.Phone.STARRED,
            ContactsContract.CommonDataKinds.Phone.NORMALIZED_NUMBER,
            ContactsContract.CommonDataKinds.Phone.CONTACT_ID
    )

    private val contactEmailExpectedProjection = arrayOf(
            ContactsContract.CommonDataKinds.Email._ID,
            ContactsContract.Data.DISPLAY_NAME,
            ContactsContract.CommonDataKinds.Email.STARRED,
            ContactsContract.CommonDataKinds.Email.DATA,
            ContactsContract.CommonDataKinds.Email.CONTACT_ID
    )

    @Test
    fun getProjectionForClazz_UserContact_Success() {
        val projection = ProjectionFactory.getProjectionForClazz(UserContact::class.java)
        Assert.assertNotNull(projection)
        Assert.assertTrue(Arrays.equals(userContactExpectedProjection, projection))
    }

    @Test
    fun getProjectionForClazz_ContactPhone_Success() {
        val projection = ProjectionFactory.getProjectionForClazz(ContactPhone::class.java)
        Assert.assertNotNull(projection)
        Assert.assertTrue(Arrays.equals(contactPhoneExpectedProjection, projection))
    }

    @Test
    fun getProjectionForClazz_ContactEmail_Success() {
        val projection = ProjectionFactory.getProjectionForClazz(ContactEmail::class.java)
        Assert.assertNotNull(projection)
        Assert.assertTrue(Arrays.equals(contactEmailExpectedProjection, projection))
    }

    @Test(expected = IllegalArgumentException::class)
    fun getProjectionForClazz_AnyOtherClass_Fails() {
        ProjectionFactory.getProjectionForClazz(this.javaClass)
    }
}
