package com.nullgr.corelibrary.rxcontacts

import android.content.ContentResolver
import android.content.Context
import android.net.Uri
import android.provider.ContactsContract
import com.nullgr.corelibrary.collections.isNotNullOrEmpty
import com.nullgr.corelibrary.rxcontacts.domain.Contact
import com.nullgr.corelibrary.rxcontacts.mapper.CursorToContactsMapper
import com.nullgr.corelibrary.rxcontacts.mapper.CursorToDataKindsMapper
import io.reactivex.Observable
import io.reactivex.rxkotlin.Observables
import java.util.*


/**
 * Created by Grishko Nikita on 01.02.18.
 */
class RxContactsProvider private constructor(private val cursorBuilder: CursorBuilder) {

    companion object {
        fun with(context: Context): RxContactsProvider {
            return RxContactsProvider(CursorBuilder(context.contentResolver))
        }

        fun with(contentResolver: ContentResolver): RxContactsProvider {
            return RxContactsProvider(CursorBuilder(contentResolver))
        }
    }

    private var includePhones = true
    private var includeEmails = true

    fun withPhones(includePhones: Boolean): RxContactsProvider {
        this.includePhones = includePhones
        return this
    }

    fun withEmails(includeEmails: Boolean): RxContactsProvider {
        this.includeEmails = includeEmails
        return this
    }

    fun fetchAll(): Observable<List<Contact>> {
        return fetchContacts()
    }

    fun findByContactId(contactId: String): Observable<Contact> {
        return fetchContacts(ContactsContract.Contacts._ID, arrayOf(contactId))
                .flatMap {
                    Observable.just(it.firstOrNull())
                }
    }

    fun findByName(displayName: String): Observable<Contact> {
        return fetchContacts(ContactsContract.Contacts.DISPLAY_NAME, arrayOf(displayName))
                .flatMap {
                    Observable.just(it.firstOrNull())
                }
    }

    fun filterByContactIds(vararg contactId: String): Observable<List<Contact>> {
        @Suppress("UNCHECKED_CAST")
        return fetchContacts(ContactsContract.Contacts._ID, contactId as Array<String>)
    }

    fun filterByNames(vararg displayNames: String): Observable<List<Contact>> {
        @Suppress("UNCHECKED_CAST")
        return fetchContacts(ContactsContract.Contacts.DISPLAY_NAME, displayNames as Array<String>)
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
            cursorBuilder.buildContactsCursor()
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
            cursorBuilder.buildDataKindsCursorForContactId(ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
                    ContactsContract.CommonDataKinds.Phone.CONTACT_ID,
                    contactId)
        }.map {
            CursorToDataKindsMapper.mapPhones(it)
        }
    }

    private fun readContactEmails(contactId: String): Observable<List<String>> {
        return Observable.fromCallable {
            cursorBuilder.buildDataKindsCursorForContactId(ContactsContract.CommonDataKinds.Email.CONTENT_URI,
                    ContactsContract.CommonDataKinds.Email.CONTACT_ID,
                    contactId)
        }.map {
            CursorToDataKindsMapper.mapEmails(it)
        }
    }

    private fun fetchContacts(selection: String? = null,
                              selectionArguments: Array<String>? = null): Observable<List<Contact>> {
        return Observable.fromCallable {
            cursorBuilder.buildContactsCursor(selection, selectionArguments)
        }.map {
            CursorToContactsMapper.map(it)
        }.map {
            if (!includePhones && !includeEmails) return@map it

            it.forEach { contact ->
                Observables.zip(
                        (if (includePhones && contact.hasPhones) readContactPhones(contact.id.toString()) else emptyStringList()),
                        (if (includeEmails) readContactEmails(contact.id.toString()) else emptyStringList()))
                        .subscribe {
                            if (it.first.isNotNullOrEmpty()) contact.phones = it.first as ArrayList<String>
                            if (it.second.isNotNullOrEmpty()) contact.emails = it.second as ArrayList<String>
                        }
            }
            it
        }
    }


    private fun emptyStringList(): Observable<List<String>> {
        return Observable.just(Collections.emptyList())
    }
}