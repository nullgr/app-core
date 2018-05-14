package com.nullgr.corelibrary.rxcontacts.engine.query

import android.annotation.SuppressLint
import com.nullgr.corelibrary.rxcontacts.domain.BaseContact
import com.nullgr.corelibrary.rxcontacts.domain.ContactEmail
import com.nullgr.corelibrary.rxcontacts.domain.ContactPhone
import com.nullgr.corelibrary.rxcontacts.domain.UserContact
import com.nullgr.corelibrary.rxcontacts.engine.ContactsLoadEngine
import io.reactivex.Observable

/**
 * Builder class that provides generalized api to build contacts selection query.
 * Can build three types for query:
 * ```
 * WHERE column_name = 'something'
 * ```
 * ```
 * WHERE column_name IN ('something','something 2','something 3')
 * ```
 * ```
 * WHERE column_name LIKE '%some%'
 * ```
 * Example usage:
 * ```
 * RxContactsProvider.with(context)
 *           .query(UserContact::class.java)
 *           .where(QueryProperties.USER_NAME)
 *           .isLike("User")
 *           .fetchAll()
 * ```
 * @see QueryProperty to understand mapping of properties to column names
 *
 * @author Grishko Nikita
 */
@SuppressLint("VisibleForTests")
class ContactsQueryBuilder<T : BaseContact> private constructor(
        private val clazz: Class<*>,
        private val contactsLoadEngine: ContactsLoadEngine) {

    private val whereSelectionBuilder: StringBuilder by lazy { StringBuilder() }
    private var property: QueryProperty? = null
    private var includePhones: Boolean = true
    private var includeEmails: Boolean = true

    companion object {
        internal fun <R : BaseContact> createQuery(clazz: Class<R>, contactsLoadEngine: ContactsLoadEngine): ContactsQueryBuilder<R> {
            return ContactsQueryBuilder(clazz, contactsLoadEngine)
        }
    }

    /**
     * Starts WHERE query string
     * @param property any of [QueryProperty] that available for searched class
     */
    fun where(property: QueryProperty): ContactsQueryBuilder<T> {
        this.property = property
        this.whereSelectionBuilder.append(PropertyToColumnNameMapper.map(property, clazz))
        return this
    }

    /**
     * Ends WHERE query string to search exact
     * @param value search param
     */
    fun isEqualTo(value: String): ContactsQueryBuilder<T> {
        this.whereSelectionBuilder.append(SelectionArgsBuilder.buildWhere(value))
        return this
    }

    /**
     * Ends WHERE query string to search by specified array
     * @param values search params
     */
    fun isIn(vararg values: String): ContactsQueryBuilder<T> {
        this.whereSelectionBuilder.append(SelectionArgsBuilder.buildIn(values))
        return this
    }

    /**
     * Ends WHERE query string with LIKE %% param
     * @param value search param
     */
    fun isLike(value: String): ContactsQueryBuilder<T> {
        this.whereSelectionBuilder.append(SelectionArgsBuilder.buildLike(value))
        return this
    }

    /**
     * Indicates that we need to join all [ContactPhone] for [UserContact].
     * By default this value is set to true. It will only matter for [UserContact]
     * @param includePhones true if need to fetch all phones of contact, false otherwise
     */
    fun includePhones(includePhones: Boolean): ContactsQueryBuilder<T> {
        this.includePhones = includePhones
        return this
    }

    /**
     * Indicates that we need to join all [ContactEmail] for [UserContact].
     * By default this value is set to true. It will only matter for [UserContact]
     * @param includeEmails true if need to fetch all email of contact, false otherwise
     */
    fun includeEmails(includeEmails: Boolean): ContactsQueryBuilder<T> {
        this.includeEmails = includeEmails
        return this
    }

    /**
     * Returns [Observable] that emits [List] of founded contacts
     */
    fun fetchAll(): Observable<List<T>> {
        return buildLoadEngineConfiguration().fetchAll()
    }

    /**
     * Returns [Observable] that emits single founded contact.
     * If nothing were found, **onComplete** will be called.
     */
    fun fetchSingle(): Observable<T> {
        @Suppress("UNCHECKED_CAST")
        return buildLoadEngineConfiguration().fetchSingle()
    }

    private fun buildLoadEngineConfiguration(): ContactsLoadEngine {
        return contactsLoadEngine
                .withIncludeAdditionalData(includePhones, includeEmails)
                .withQueryConfiguration(clazz, property, whereSelectionBuilder.toString())
    }
}