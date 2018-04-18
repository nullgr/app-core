package com.nullgr.corelibrary.rxcontacts

import android.content.ContentResolver
import android.content.Context
import android.database.Cursor
import android.net.Uri
import android.provider.ContactsContract
import com.nullgr.corelibrary.collections.isNotNullOrEmpty
import com.nullgr.corelibrary.rx.applyBothSchedulers
import com.nullgr.corelibrary.rx.schedulers.IoToMainSchedulersFacade
import com.nullgr.corelibrary.rxcontacts.domain.Contact
import com.nullgr.corelibrary.rxcontacts.mapper.CursorToContactsMapper
import com.nullgr.corelibrary.rxcontacts.mapper.CursorToDataKindsMapper
import io.reactivex.Observable
import io.reactivex.rxkotlin.Observables
import java.util.*


/**
 * Created by Grishko Nikita on 01.02.18.
 */
class RxContactsProvider private constructor(private val contentResolver: ContentResolver) {

    companion object {
        fun with(context: Context): RxContactsProvider {
            return RxContactsProvider(context.contentResolver)
        }

        fun with(contentResolver: ContentResolver): RxContactsProvider {
            return RxContactsProvider(contentResolver)
        }

        private val PROJECTION = arrayOf(ContactsContract.Contacts._ID,
                ContactsContract.Contacts.LOOKUP_KEY,
                ContactsContract.Contacts.DISPLAY_NAME_PRIMARY,
                ContactsContract.Contacts.STARRED,
                ContactsContract.Contacts.PHOTO_URI,
                ContactsContract.Contacts.PHOTO_THUMBNAIL_URI,
                ContactsContract.Contacts.HAS_PHONE_NUMBER)
    }

    @JvmOverloads
    fun fetchAll(phoneInclude: Boolean = true, emailInclude: Boolean = true): Observable<List<Contact>> {
        return Observable.fromCallable {
            buildContactsCursor()
        }.map {
            CursorToContactsMapper.map(it)
        }.map {
            if (!phoneInclude && !emailInclude) return@map it

            it.forEach { contact ->
                Observables.zip(
                        (if (phoneInclude && contact.hasPhones) readContactPhones(contact.id.toString()) else emptyList()),
                        (if (emailInclude) readContactEmails(contact.id.toString()) else emptyList()))
                        .subscribe {
                            if (it.first.isNotNullOrEmpty()) contact.phones = it.first as ArrayList<String>
                            if (it.second.isNotNullOrEmpty()) contact.emails = it.second as ArrayList<String>
                        }
            }
            it
        }
    }

    //TODO in progress
    fun findById(contactId: String): Observable<Contact> {
        return findBy(ContactsContract.Data.CONTACT_ID, contactId)
                .flatMap {
                    Observable.just(it.firstOrNull())
                }
    }
    //TODO in progress
    fun findByPhone(vararg arguments: String): Observable<List<Contact>> {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }
    //TODO in progress
    fun findByEmail(vararg arguments: String): Observable<List<Contact>> {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }
    //TODO in progress
    fun find(contactsUri: Uri): Observable<Contact?> {
        return Observable.fromCallable {
            contentResolver.query(
                    contactsUri,
                    PROJECTION,
                    null, null,
                    null)
        }.map {
            CursorToContactsMapper.map(it)
        }.flatMap {
            Observable.just(it.firstOrNull())
        }
    }

    //TODO in progress
    fun getContactPhones(contactUri: Uri): Observable<List<String>> {
        return find(contactUri)
                .flatMap { readContactPhones(it.id.toString()) }
    }


    //TODO in progress
    fun getContactEmails(contactUri: Uri): Observable<List<String>> {
        return find(contactUri)
                .flatMap { readContactEmails(it.id.toString()) }
    }


    private fun readContactPhones(contactId: String): Observable<List<String>> {
        return Observable.fromCallable {
            buildDataKindsCursor(ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
                    ContactsContract.CommonDataKinds.Phone.CONTACT_ID,
                    contactId)
        }.map {
            CursorToDataKindsMapper.mapPhones(it)
        }
    }

    private fun readContactEmails(contactId: String): Observable<List<String>> {
        return Observable.fromCallable {
            buildDataKindsCursor(ContactsContract.CommonDataKinds.Email.CONTENT_URI,
                    ContactsContract.CommonDataKinds.Email.CONTACT_ID,
                    contactId)
        }.map {
            CursorToDataKindsMapper.mapEmails(it)
        }
    }

    //TODO in progress
    private fun findBy(column: String, vararg arguments: String): Observable<List<Contact>> {
        return Observable.defer<List<Contact>> {
            Observable
                    .just(buildContactsCursor(column, arguments))
                    .map {
                        val contacts = CursorToContactsMapper.map(it)
                        contacts
                    }

        }.applyBothSchedulers(IoToMainSchedulersFacade())
    }


    private fun emptyList(): Observable<List<String>> {
        return Observable.just(Collections.emptyList())
    }

    private fun buildContactsCursor(selection: String? = null, selectionArgs: Array<out String>? = null): Cursor {
        return contentResolver.query(
                ContactsContract.Contacts.CONTENT_URI,
                PROJECTION,
                selection, selectionArgs,
                ContactsContract.Contacts._ID
        )
    }

    private fun buildDataKindsCursor(dataKindsUri: Uri, dataKindsSelectionKey: String, contactId: String): Cursor {
        return contentResolver.query(
                dataKindsUri,
                null,
                "$dataKindsSelectionKey = $contactId", null,
                null)
    }
}