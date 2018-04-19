package com.nullgr.corelibrary.rxcontacts

import android.content.ContentResolver
import android.content.Context
import android.net.Uri
import android.provider.ContactsContract
import com.nullgr.corelibrary.collections.isNotNullOrEmpty
import com.nullgr.corelibrary.rxcontacts.domain.Contact
import com.nullgr.corelibrary.rxcontacts.mapper.CursorToContactIdsMapper
import com.nullgr.corelibrary.rxcontacts.mapper.CursorToContactsMapper
import com.nullgr.corelibrary.rxcontacts.mapper.CursorToDataKindsMapper
import com.nullgr.corelibrary.rxcontacts.validator.UriToMethodValidator
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
        return readContacts()
    }

    fun findContactsById(vararg contactId: String): Observable<List<Contact>> {
        return readContacts(ContactsContract.Contacts._ID,
                @Suppress("UNCHECKED_CAST") (contactId as Array<String>))
    }

    fun findContactsByName(vararg displayNames: String): Observable<List<Contact>> {
        return readContacts(ContactsContract.Contacts.DISPLAY_NAME,
                @Suppress("UNCHECKED_CAST") (displayNames as Array<String>))
    }

    /**
     * Note: right now works only for exact matching
     */
    fun findContactsByPhone(vararg arguments: String): Observable<List<Contact>> {
        return readContactsByDataKind(
                CursorBuilder.SupportedDataKinds.PHONE,
                ContactsContract.CommonDataKinds.Phone.NUMBER,
                @Suppress("UNCHECKED_CAST") (arguments as Array<String>),
                ContactsContract.CommonDataKinds.Phone.CONTACT_ID
        )
    }

    fun findContactsByEmail(vararg arguments: String): Observable<List<Contact>> {
        return readContactsByDataKind(
                CursorBuilder.SupportedDataKinds.EMAIL,
                ContactsContract.CommonDataKinds.Email.DATA,
                @Suppress("UNCHECKED_CAST") (arguments as Array<String>),
                ContactsContract.CommonDataKinds.Email.CONTACT_ID
        )
    }

    @Throws(IllegalArgumentException::class)
    fun fetchContactFromUri(contactsUri: Uri): Observable<Contact?> {
        UriToMethodValidator.validateUriToPickContact(contactsUri)
        return Observable.fromCallable {
            cursorBuilder.buildContactsCursorBySpecificUri(contactsUri)
        }.map {
            CursorToContactsMapper.map(it)
        }.flatMap {
            if (it.isNotNullOrEmpty()) Observable.just<Contact?>(it.first())
            else Observable.empty()
        }.map { contact ->
            if (!includePhones && !includeEmails) return@map contact

            Observables.zip(
                    (if (includePhones && contact.hasPhones) readContactPhones(contact.id.toString()) else emptyStringList()),
                    (if (includeEmails) readContactEmails(contact.id.toString()) else emptyStringList()))
                    .subscribe {
                        if (it.first.isNotNullOrEmpty()) contact.phones = it.first as ArrayList<String>
                        if (it.second.isNotNullOrEmpty()) contact.emails = it.second as ArrayList<String>
                    }

            contact
        }
    }

    //TODO still not correct
    @Throws(IllegalArgumentException::class)
    fun fetchPhoneFromUri(phoneDataUri: Uri): Observable<List<String>> {
        UriToMethodValidator.validateUriToPickPhoneOrEmailData(phoneDataUri)
        return readContactPhones(phoneDataUri.lastPathSegment.toString())
    }

    //TODO still not correct
    @Throws(IllegalArgumentException::class)
    fun fetchEmailFromUri(emailDataUri: Uri): Observable<List<String>> {
        UriToMethodValidator.validateUriToPickPhoneOrEmailData(emailDataUri)
        return readContactEmails(emailDataUri.lastPathSegment.toString())
    }

    private fun readContacts(selection: String? = null,
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

    private fun readContactsByDataKind(dataKinds: CursorBuilder.SupportedDataKinds,
                                       selection: String,
                                       selectionArguments: Array<String>,
                                       contactIdRowName: String): Observable<List<Contact>> {
        return Observable.fromCallable {
            cursorBuilder.buildGeneralDataKindsCursor(
                    dataKinds,
                    selection,
                    selectionArguments
            )
        }.map {
            CursorToContactIdsMapper.map(it, contactIdRowName)
        }.flatMap {
            findContactsById(*it.toTypedArray())
        }
    }

    private fun readContactPhones(contactId: String): Observable<List<String>> {
        return Observable.fromCallable {
            cursorBuilder.buildDataKindsCursorForContactId(CursorBuilder.SupportedDataKinds.PHONE, contactId)
        }.map {
            CursorToDataKindsMapper.mapPhones(it)
        }
    }

    private fun readContactEmails(contactId: String): Observable<List<String>> {
        return Observable.fromCallable {
            cursorBuilder.buildDataKindsCursorForContactId(CursorBuilder.SupportedDataKinds.EMAIL, contactId)
        }.map {
            CursorToDataKindsMapper.mapEmails(it)
        }
    }

    private fun emptyStringList(): Observable<List<String>> {
        return Observable.just(Collections.emptyList())
    }
}