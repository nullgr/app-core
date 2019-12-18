package com.nullgr.core.rx.contacts.extensions

import androidx.test.ext.junit.runners.AndroidJUnit4
import com.nullgr.core.rx.contacts.domain.BaseContact
import com.nullgr.core.rx.contacts.domain.UserContact
import org.junit.Assert
import org.junit.Test
import org.junit.runner.RunWith
import java.util.*

/**
 * @author Grishko Nikita
 */
@RunWith(AndroidJUnit4::class)
class ExtensionFunctionsTest {

    @Test
    fun has_AlreadyAddedId_True() {
        val contacts = ArrayList<BaseContact>()
        for (i in 0..9) {
            contacts.add(newContact(i))
        }
        Assert.assertTrue(contacts.has(5))
    }

    @Test
    fun has_ContactWithNewId_False() {
        val contacts = ArrayList<BaseContact>()
        for (i in 0..9) {
            contacts.add(newContact(i))
        }
        Assert.assertFalse(contacts.has(11))
    }

    @Test
    fun getEmptyEmailContactList_ObservableWithEmptyList_True() {
        emptyEmailContactsList
                .subscribe { contactEmails ->
                    Assert.assertTrue(contactEmails.isEmpty())
                }
    }

    @Test
    fun getEmptyPhoneContactsList_ObservableWithEmptyList_True() {
        emptyPhoneContactsList
                .subscribe { contactPhones ->
                    Assert.assertTrue(contactPhones.isEmpty())
                }
    }

    private fun newContact(index: Int): UserContact {
        return UserContact(index.toLong(),
                "Name$index",
                "Key$index",
                false, null, null,
                false, null, null)
    }
}
