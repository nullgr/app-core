package com.nullgr.corelibrary.rxcontacts.engine

import android.annotation.SuppressLint
import android.content.ContentResolver
import android.net.Uri
import android.provider.ContactsContract
import android.util.Log
import com.nullgr.corelibrary.BuildConfig
import com.nullgr.corelibrary.collections.isNotNullOrEmpty
import com.nullgr.corelibrary.rxcontacts.domain.BaseContact
import com.nullgr.corelibrary.rxcontacts.domain.ContactEmail
import com.nullgr.corelibrary.rxcontacts.domain.ContactPhone
import com.nullgr.corelibrary.rxcontacts.domain.UserContact
import com.nullgr.corelibrary.rxcontacts.engine.cursor.CursorFactory
import com.nullgr.corelibrary.rxcontacts.engine.query.PropertyToColumnNameMapper
import com.nullgr.corelibrary.rxcontacts.engine.query.QueryProperty
import com.nullgr.corelibrary.rxcontacts.engine.query.SelectionArgsBuilder
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
@SuppressLint("VisibleForTests")
internal class ContactsLoadEngine(private val contentResolver: ContentResolver) {

    private lateinit var configuration: Configuration
    private var includePhones: Boolean = true
    private var includeEmails: Boolean = true

    fun withIncludeAdditionalData(includePhones: Boolean, includeEmail: Boolean): ContactsLoadEngine {
        this.includePhones = includePhones
        this.includeEmails = includeEmail
        return this
    }

    fun withQueryConfiguration(clazz: Class<*>,
                               property: QueryProperty? = null,
                               whereSelection: String? = null): ContactsLoadEngine {
        if (BuildConfig.DEBUG) {
            Log.d(ContactsLoadEngine::class.java.simpleName, "Build where selection $whereSelection")
        }
        configuration = Configuration(clazz, property, whereSelection)
        return this
    }

    @Throws(IllegalArgumentException::class)
    fun withUriConfiguration(clazz: Class<*>,
                             specificUri: Uri): ContactsLoadEngine {
        validateUri(clazz, specificUri)
        configuration = Configuration(clazz, specificUri = specificUri)
        return this
    }

    fun <T : BaseContact> fetchAll(): Observable<List<T>> {
        return buildRxRequestForConfiguration()
    }

    fun <T : BaseContact> fetchSingle(): Observable<T> {
        return buildRxRequestForConfiguration<T>()
                .flatMap {
                    val contact = it.firstOrNull()
                    if (contact != null) Observable.just(contact)
                    else Observable.empty()
                }
    }

    @Suppress("UNCHECKED_CAST")
    private fun <T : BaseContact> buildRxRequestForConfiguration(): Observable<List<T>> {
        return when (configuration.clazz) {
            UserContact::class.java ->
                if (configuration.specificUri != null) loadUserContactsByUri() else loadUserContactsByQuery()
            ContactEmail::class.java ->
                if (configuration.specificUri != null) loadContactEmailsByUri() else loadContactEmailsByQuery()
            ContactPhone::class.java ->
                if (configuration.specificUri != null) loadContactPhonesByUri() else loadContactPhonesByQuery()
            else -> throw IllegalStateException("Unknown class ${configuration.clazz} to build rx request")
        } as Observable<List<T>>
    }

    private fun loadUserContactsByQuery(): Observable<List<UserContact>> {
        return if (configuration.property != null &&
                (configuration.property == QueryProperty.EMAIL ||
                        configuration.property == QueryProperty.PHONE ||
                        configuration.property == QueryProperty.NORMALIZED_PHONE)) {
            findContactsByEmailsOrPhones()
        } else {
            Observable.fromCallable {
                CursorFactory.getCursor(contentResolver,
                        configuration.clazz,
                        configuration.whereSelection)
            }.map {
                CursorToContactsMapper.map(it)
            }
        }.map { contactsList ->
            contactsList.forEach {
                fillContactWithData(it)
            }
            contactsList
        }
    }

    @SuppressLint("VisibleForTests")
    private fun findContactsByEmailsOrPhones(): Observable<List<UserContact>> {
        return Observable.fromCallable {
            CursorFactory.getCursor(contentResolver, when (configuration.property) {
                QueryProperty.EMAIL -> ContactEmail::class.java
                else -> ContactPhone::class.java
            }, configuration.whereSelection)
        }.map {
            CursorToContactIdsMapper.map(it, ContactsContract.Data.CONTACT_ID)
        }.map {
            CursorFactory.getCursor(contentResolver,
                    UserContact::class.java,
                    PropertyToColumnNameMapper.map(QueryProperty.ID, UserContact::class.java)
                            + SelectionArgsBuilder.buildIn(it.toTypedArray()))
        }.map {
            CursorToContactsMapper.map(it)
        }
    }

    private fun loadUserContactsByUri(): Observable<List<UserContact>> {
        return Observable.fromCallable {
            CursorFactory.getCursor(contentResolver, configuration.clazz, configuration.specificUri!!)
        }.map {
            CursorToContactsMapper.map(it)
        }.map { contactsList ->
            contactsList.forEach {
                fillContactWithData(it)
            }
            contactsList
        }
    }

    private fun loadContactPhonesByQuery(): Observable<List<ContactPhone>> {
        return Observable.fromCallable {
            CursorFactory.getCursor(contentResolver, configuration.clazz, configuration.whereSelection)
        }.map {
            CursorToPhonesMapper.map(it)
        }
    }

    private fun loadContactPhonesByUri(): Observable<List<ContactPhone>> {
        return Observable.fromCallable {
            CursorFactory.getCursor(contentResolver, configuration.clazz, configuration.specificUri!!)
        }.map {
            CursorToPhonesMapper.map(it)
        }
    }

    private fun loadContactEmailsByQuery(): Observable<List<ContactEmail>> {
        return Observable.fromCallable {
            CursorFactory.getCursor(contentResolver, configuration.clazz, configuration.whereSelection)
        }.map {
            CursorToEmailMapper.map(it)
        }
    }

    private fun loadContactEmailsByUri(): Observable<List<ContactEmail>> {
        return Observable.fromCallable {
            CursorFactory.getCursor(contentResolver, configuration.clazz, configuration.specificUri!!)
        }.map {
            CursorToEmailMapper.map(it)
        }
    }

    private fun fillContactWithData(contact: UserContact) {
        if (includePhones || includeEmails) {
            Observables.zip(
                    (if (includePhones && contact.hasPhones) loadContactPhonesForContactId(contact.id.toString()) else emptyPhoneContactsList),
                    (if (includeEmails) loadContactEmailsForContactId(contact.id.toString()) else emptyEmailContactsList))
                    .subscribe {
                        if (it.first.isNotNullOrEmpty()) contact.phones = it.first as ArrayList<ContactPhone>
                        if (it.second.isNotNullOrEmpty()) contact.emails = it.second as ArrayList<ContactEmail>
                    }
        }
    }

    private fun loadContactPhonesForContactId(contactId: String): Observable<List<ContactPhone>> {
        return Observable.fromCallable {
            CursorFactory.getCursor(contentResolver, ContactPhone::class.java,
                    PropertyToColumnNameMapper.map(QueryProperty.USER_CONTACT_ID, ContactPhone::class.java)
                            + SelectionArgsBuilder.buildWhere(contactId))
        }.map {
            CursorToPhonesMapper.map(it)
        }
    }

    private fun loadContactEmailsForContactId(contactId: String): Observable<List<ContactEmail>> {
        return Observable.fromCallable {
            CursorFactory.getCursor(contentResolver, ContactEmail::class.java,
                    PropertyToColumnNameMapper.map(QueryProperty.USER_CONTACT_ID, ContactEmail::class.java)
                            + SelectionArgsBuilder.buildWhere(contactId))
        }.map {
            CursorToEmailMapper.map(it)
        }
    }

    @Throws(IllegalArgumentException::class)
    private fun validateUri(clazz: Class<*>, uri: Uri) {
        when (clazz) {
            UserContact::class.java -> UriToMethodValidator.validateUriToPickContact(uri)
            ContactEmail::class.java -> UriToMethodValidator.validateUriToPickPhoneOrEmailData(uri)
            ContactPhone::class.java -> UriToMethodValidator.validateUriToPickPhoneOrEmailData(uri)
        }
    }

    private class Configuration(val clazz: Class<*>,
                                val property: QueryProperty? = null,
                                val whereSelection: String? = null,
                                val specificUri: Uri? = null)
}