package com.nullgr.corelibrary.rxcontact.engine.cursor

import android.Manifest
import android.content.ContentResolver
import android.provider.ContactsContract
import android.support.test.InstrumentationRegistry
import android.support.test.rule.GrantPermissionRule
import android.support.test.runner.AndroidJUnit4
import com.nullgr.corelibrary.rxcontacts.domain.ContactEmail
import com.nullgr.corelibrary.rxcontacts.domain.ContactPhone
import com.nullgr.corelibrary.rxcontacts.domain.UserContact
import com.nullgr.corelibrary.rxcontacts.engine.cursor.CursorFactory
import org.junit.Assert
import org.junit.Assert.assertNotNull
import org.junit.BeforeClass
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

/**
 * Created by Grishko Nikita on 01.02.18.
 */
@RunWith(AndroidJUnit4::class)
class CursorFactoryTest {

    @get:Rule
    val runtimePermissionRule = GrantPermissionRule.grant(
            Manifest.permission.READ_CONTACTS,
            Manifest.permission.WRITE_CONTACTS)!!

    @Test
    fun getCursor_UserContactAndNullSelection_NotNull() {
        val cursor = CursorFactory.getCursor(contentResolver!!, UserContact::class.java, null as String?)
        assertNotNull(cursor)
    }

    @Test
    fun getCursor_ContactEmailAndEmailUri_NotNull() {
        val cursor = CursorFactory.getCursor(contentResolver!!,
                ContactEmail::class.java,
                ContactsContract.CommonDataKinds.Email.CONTENT_URI)
        assertNotNull(cursor)
    }

    @Test
    fun clazzToUri_UserContact_Equals() {
        val contactUri = CursorFactory.clazzToUri(UserContact::class.java)
        Assert.assertEquals(contactUri, ContactsContract.Contacts.CONTENT_URI)
    }

    @Test
    fun clazzToUri_ContactPhone_Equals() {
        val phoneUri = CursorFactory.clazzToUri(ContactPhone::class.java)
        Assert.assertEquals(phoneUri, ContactsContract.CommonDataKinds.Phone.CONTENT_URI)
    }

    @Test
    fun clazzToUri_ContactEmail_Equals() {
        val emailUri = CursorFactory.clazzToUri(ContactEmail::class.java)
        Assert.assertEquals(emailUri, ContactsContract.CommonDataKinds.Email.CONTENT_URI)
    }

    @Test(expected = IllegalArgumentException::class)
    fun clazzToUri_AnyOtherClass_Fails() {
        CursorFactory.clazzToUri(this.javaClass)
    }

    companion object {

        private var contentResolver: ContentResolver? = null

        @BeforeClass
        @JvmStatic
        fun init() {
            contentResolver = InstrumentationRegistry.getContext().contentResolver
        }
    }
}
