package com.nullgr.core.rx.contacts

import android.Manifest
import android.annotation.SuppressLint
import android.content.ContentResolver
import android.content.Context
import android.net.Uri
import android.provider.ContactsContract
import androidx.annotation.RequiresPermission
import com.nullgr.core.rx.contacts.domain.BaseContact
import com.nullgr.core.rx.contacts.domain.ContactEmail
import com.nullgr.core.rx.contacts.domain.ContactPhone
import com.nullgr.core.rx.contacts.domain.UserContact
import com.nullgr.core.rx.contacts.engine.ContactsLoadEngine
import com.nullgr.core.rx.contacts.engine.query.ContactsQueryBuilder
import com.nullgr.core.rx.contacts.engine.query.QueryProperty
import io.reactivex.Observable


/**
 * This class provides rx api to work with phone contacts.
 *
 * Instead of work with specific contact tables use the following abstractions:
 * - [UserContact] to work with [ContactsContract.Contacts]
 * - [ContactPhone] to work with [ContactsContract.CommonDataKinds.Phone]
 * - [ContactEmail] to work with [ContactsContract.CommonDataKinds.Email]
 *
 * To understand how this abstractions depends look to this ***simplified*** scheme
 * ```
 * Contacts table (represents by UserContacts abstraction)
 *  ---------------------------------------------------------
 * |_ID | LOOKUP_KEY | DISPLAY_NAME | STARRED | ... etc      |
 *  ---------------------------------------------------------
 * | 1  | abcdef     | John Doe     |    1    | ...          |
 *  ---------------------------------------------------------
 *
 *  * Data table (represents by ContactPhone or ContactEmail abstraction)
 *  ---------------------------------------------------------------
 * |_ID | CONTACT_ID | MIMETYPE     | DATA1          | ... etc      |
 *  ---------------------------------------------------------------
 * | 32  |     1     | PHONE NUMBER | 555-000-1111   | ...          |
 *  ---------------------------------------------------------------
 * | 33  |     1     | EMAIL        | some@gmail.com | ...          |
 *  ---------------------------------------------------------------
 *
 * ```
 * Provide methods to query contacts by params declared in [QueryProperty],
 * or by specified uri.
 *
 * Example usage:
 * ```
 * RxContactsProvider.with(context)
 *           .query(UserContact::class.java)
 *           .where(QueryProperties.USER_NAME)
 *           .isLike("User")
 *           .fetchAll()
 * ```
 * Or:
 * ```
 * RxContactsProvider.with(context)
 *           .fromUri(UserContact::class.java, contactUri)
 * ```
 * @author Grishko Nikita
 */
@SuppressLint("MissingPermission")
class RxContactsProvider private constructor(private val contactsLoadEngine: ContactsLoadEngine) {

    companion object {

        fun with(context: Context): RxContactsProvider {
            return RxContactsProvider(ContactsLoadEngine(context.contentResolver))
        }

        fun with(contentResolver: ContentResolver): RxContactsProvider {
            return RxContactsProvider(ContactsLoadEngine(contentResolver))
        }
    }

    /**
     * Fetch all contacts for given [clazz].
     * [ContactEmail] and [ContactPhone] will be automatically join to each contact
     * @param clazz one of [UserContact], [ContactPhone] or [ContactEmail]
     * @return [Observable] that emits [List] of contacts
     */
    @RequiresPermission(Manifest.permission.READ_CONTACTS)
    fun <T : BaseContact> fetchAll(clazz: Class<T>): Observable<List<T>> {
        return contactsLoadEngine
                .withIncludeAdditionalData(true, true)
                .withQueryConfiguration(clazz)
                .fetchAll()
    }

    /**
     * Fetch first contact for given [clazz].
     * [ContactEmail] and [ContactPhone] will be automatically join to it
     * @param clazz one of [UserContact], [ContactPhone] or [ContactEmail]
     * @return [Observable] that emits single contact
     */
    @RequiresPermission(Manifest.permission.READ_CONTACTS)
    fun <T : BaseContact> fetchSingle(clazz: Class<T>): Observable<T> {
        return contactsLoadEngine
                .withIncludeAdditionalData(true, true)
                .withQueryConfiguration(clazz)
                .fetchSingle()
    }

    /**
     * Fetch contacts contact for given [clazz] and specific [uri].
     * [ContactEmail] and [ContactPhone] will be automatically join to each of [UserContact]
     *
     * ***NOTE:*** before build request, uri will be validated.
     * So be sure that you use [ContactsContract.Data.CONTENT_URI] for [ContactEmail] and [ContactPhone],
     * and [ContactsContract.Contacts.CONTENT_URI] for [UserContact].
     * Otherwise [IllegalArgumentException] will be thrown
     *
     * @param clazz one of [UserContact], [ContactPhone] or [ContactEmail]
     * @return [Observable] that emits [List] of contacts
     * @throws [IllegalArgumentException]
     */
    @RequiresPermission(Manifest.permission.READ_CONTACTS)
    fun <T : BaseContact> fromUri(clazz: Class<T>, uri: Uri): Observable<List<T>> {
        return contactsLoadEngine
                .withIncludeAdditionalData(true, true)
                .withUriConfiguration(clazz, uri)
                .fetchAll()
    }

    /**
     * Starts building query for given clazz
     * @param clazz one of [UserContact], [ContactPhone] or [ContactEmail]
     * @return instance of [ContactsQueryBuilder] that provides api to build query
     */
    @RequiresPermission(Manifest.permission.READ_CONTACTS)
    fun <T : BaseContact> query(clazz: Class<T>): ContactsQueryBuilder<T> {
        return ContactsQueryBuilder.createQuery(clazz, contactsLoadEngine)
    }
}