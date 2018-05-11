package com.nullgr.corelibrary.rxcontact.extensions;

import android.support.test.runner.AndroidJUnit4;

import com.nullgr.corelibrary.rxcontacts.domain.BaseContact;
import com.nullgr.corelibrary.rxcontacts.domain.ContactEmail;
import com.nullgr.corelibrary.rxcontacts.domain.ContactPhone;
import com.nullgr.corelibrary.rxcontacts.domain.UserContact;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.functions.Consumer;

import static com.nullgr.corelibrary.rxcontacts.extensions.ExtensionFunctionsKt.getEmptyEmailContactsList;
import static com.nullgr.corelibrary.rxcontacts.extensions.ExtensionFunctionsKt.getEmptyPhoneContactsList;
import static com.nullgr.corelibrary.rxcontacts.extensions.ExtensionFunctionsKt.has;

/**
 * Created by Grishko Nikita on 01.02.18.
 */
@SuppressWarnings("KotlinInternalInJava")
@RunWith(AndroidJUnit4.class)
public class ExtensionFunctionsTest {

    @Test
    public void testHasFunction() {
        List<BaseContact> contacts = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            contacts.add(newContact(i));
        }

        Assert.assertTrue(has(contacts, 5));
        Assert.assertFalse(has(contacts, 11));
    }

    @Test
    public void testEmptyEmailContactsList() {
        getEmptyEmailContactsList()
                .subscribe(new Consumer<ArrayList<ContactEmail>>() {
                    @Override
                    public void accept(ArrayList<ContactEmail> contactEmails) {
                        Assert.assertTrue(contactEmails != null && contactEmails.isEmpty());
                    }
                });
    }

    @Test
    public void testEmptyPhoneContactsList() {
        getEmptyPhoneContactsList()
                .subscribe(new Consumer<ArrayList<ContactPhone>>() {
                    @Override
                    public void accept(ArrayList<ContactPhone> contactPhones) {
                        Assert.assertTrue(contactPhones != null && contactPhones.isEmpty());
                    }
                });
    }

    private UserContact newContact(int index) {
        return new UserContact(index,
                "Name" + index,
                "Key" + index,
                false,
                null,
                null,
                false,
                null,
                null);
    }
}
