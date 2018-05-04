package com.nullgr.corelibrary.rxcontacts

import android.content.ContentResolver
import android.content.Context
import android.net.Uri
import android.provider.ContactsContract
import com.nullgr.corelibrary.collections.isNotNullOrEmpty
import com.nullgr.corelibrary.rxcontacts.domain.ContactEmail
import com.nullgr.corelibrary.rxcontacts.domain.ContactPhone
import com.nullgr.corelibrary.rxcontacts.domain.UserContact
import com.nullgr.corelibrary.rxcontacts.extensions.emptyEmailContactsList
import com.nullgr.corelibrary.rxcontacts.extensions.emptyPhoneContactsList
import com.nullgr.corelibrary.rxcontacts.mapper.CursorToContactIdsMapper
import com.nullgr.corelibrary.rxcontacts.mapper.CursorToContactsMapper
import com.nullgr.corelibrary.rxcontacts.mapper.CursorToEmailMapper
import com.nullgr.corelibrary.rxcontacts.mapper.CursorToPhonesMapper
import com.nullgr.corelibrary.rxcontacts.validator.UriToMethodValidator
import io.reactivex.Observable
import io.reactivex.rxkotlin.Observables


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

    fun fetchAll(): Observable<List<UserContact>> {
        return readContacts()
    }

    fun findContactsById(vararg contactId: String): Observable<List<UserContact>> {
        return readContacts(ContactsContract.Contacts._ID,
                @Suppress("UNCHECKED_CAST") (contactId as Array<String>))
    }

    fun findContactsByName(vararg displayNames: String): Observable<List<UserContact>> {
        return readContacts(ContactsContract.Contacts.DISPLAY_NAME,
                @Suppress("UNCHECKED_CAST") (displayNames as Array<String>))
    }

    /**
     * Note: right now works only for exact matching
     */
    fun findContactsByPhone(vararg arguments: String): Observable<List<UserContact>> {
        return readContactsByDataKind(
                CursorBuilder.SupportedDataKinds.PHONE,
                ContactsContract.CommonDataKinds.Phone.NUMBER,
                @Suppress("UNCHECKED_CAST") (arguments as Array<String>),
                ContactsContract.CommonDataKinds.Phone.CONTACT_ID
        )
    }

    fun findContactsByEmail(vararg arguments: String): Observable<List<UserContact>> {
        return readContactsByDataKind(
                CursorBuilder.SupportedDataKinds.EMAIL,
                ContactsContract.CommonDataKinds.Email.DATA,
                @Suppress("UNCHECKED_CAST") (arguments as Array<String>),
                ContactsContract.CommonDataKinds.Email.CONTACT_ID
        )
    }

    @Throws(IllegalArgumentException::class)
    fun fetchContactFromUri(contactsUri: Uri): Observable<UserContact?> {
        UriToMethodValidator.validateUriToPickContact(contactsUri)
        return Observable.fromCallable {
            cursorBuilder.buildContactsCursorBySpecificUri(contactsUri)
        }.map {
            CursorToContactsMapper.map(it)
        }.flatMap {
            if (it.isNotNullOrEmpty()) Observable.just<UserContact?>(it.first())
            else Observable.empty()
        }.map { contact ->
            if (!includePhones && !includeEmails) return@map contact

            Observables.zip(
                    (if (includePhones && contact.hasPhones) readContactPhones(contact.id.toString()) else emptyPhoneContactsList),
                    (if (includeEmails) readContactEmails(contact.id.toString()) else emptyEmailContactsList))
                    .subscribe {
                        if (it.first.isNotNullOrEmpty()) contact.phones = it.first as ArrayList<ContactPhone>
                        if (it.second.isNotNullOrEmpty()) contact.emails = it.second as ArrayList<ContactEmail>
                    }

            contact
        }
    }

    @Throws(IllegalArgumentException::class)
    fun fetchPhoneFromUri(phoneDataUri: Uri): Observable<List<ContactPhone>> {
        UriToMethodValidator.validateUriToPickPhoneOrEmailData(phoneDataUri)
        return readPhoneFromUri(phoneDataUri)
    }

    @Throws(IllegalArgumentException::class)
    fun fetchEmailFromUri(emailDataUri: Uri): Observable<List<ContactEmail>> {
        UriToMethodValidator.validateUriToPickPhoneOrEmailData(emailDataUri)
        return readEmailFromUri(emailDataUri)
    }

    private fun readContacts(selection: String? = null,
                             selectionArguments: Array<String>? = null): Observable<List<UserContact>> {
        return Observable.fromCallable {
            cursorBuilder.buildContactsCursor(selection, selectionArguments)
        }.map {
            CursorToContactsMapper.map(it)
        }.map {
            if (!includePhones && !includeEmails) return@map it

            it.forEach { contact ->
                Observables.zip(
                        (if (includePhones && contact.hasPhones) readContactPhones(contact.id.toString()) else emptyPhoneContactsList),
                        (if (includeEmails) readContactEmails(contact.id.toString()) else emptyEmailContactsList))
                        .subscribe {
                            if (it.first.isNotNullOrEmpty()) contact.phones = it.first as ArrayList<ContactPhone>
                            if (it.second.isNotNullOrEmpty()) contact.emails = it.second as ArrayList<ContactEmail>
                        }
            }
            it
        }
    }

    private fun readContactsByDataKind(dataKinds: CursorBuilder.SupportedDataKinds,
                                       selection: String,
                                       selectionArguments: Array<String>,
                                       contactIdRowName: String): Observable<List<UserContact>> {
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

    private fun readContactPhones(contactId: String): Observable<List<ContactPhone>> {
        return Observable.fromCallable {
            cursorBuilder.buildDataKindsCursorForContactId(CursorBuilder.SupportedDataKinds.PHONE, contactId)
        }.map {
            CursorToPhonesMapper.map(it)
        }
    }

    private fun readContactEmails(contactId: String): Observable<List<ContactEmail>> {
        return Observable.fromCallable {
            cursorBuilder.buildDataKindsCursorForContactId(CursorBuilder.SupportedDataKinds.EMAIL, contactId)
        }.map {
            CursorToEmailMapper.map(it)
        }
    }

    private fun readPhoneFromUri(uri: Uri): Observable<List<ContactPhone>> {
        return Observable.fromCallable {
            cursorBuilder.buildDataKindCursorBySpecificUri(uri, CursorBuilder.SupportedDataKinds.PHONE)
        }.map {
            CursorToPhonesMapper.map(it)
        }
    }

    private fun readEmailFromUri(uri: Uri): Observable<List<ContactEmail>> {
        return Observable.fromCallable {
            cursorBuilder.buildDataKindCursorBySpecificUri(uri, CursorBuilder.SupportedDataKinds.EMAIL)
        }.map {
            CursorToEmailMapper.map(it)
        }
    }
}