package com.nullgr.corelibrary.rxcontacts.engine.query

import android.annotation.SuppressLint
import com.nullgr.corelibrary.rxcontacts.domain.BaseContact
import com.nullgr.corelibrary.rxcontacts.engine.ContactsLoadEngine
import io.reactivex.Observable

/**
 * Created by Grishko Nikita on 01.02.18.
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

    fun where(property: QueryProperty): ContactsQueryBuilder<T> {
        this.property = property
        this.whereSelectionBuilder.append(PropertyToColumnNameMapper.map(property, clazz))
        return this
    }

    fun isEqualTo(value: String): ContactsQueryBuilder<T> {
        this.whereSelectionBuilder.append(SelectionArgsBuilder.buildWhere(value))
        return this
    }

    fun isIn(vararg values: String): ContactsQueryBuilder<T> {
        this.whereSelectionBuilder.append(SelectionArgsBuilder.buildIn(values))
        return this
    }

    fun isLike(value: String): ContactsQueryBuilder<T> {
        this.whereSelectionBuilder.append(SelectionArgsBuilder.buildLike(value))
        return this
    }

    fun includePhones(includePhones: Boolean): ContactsQueryBuilder<T> {
        this.includePhones = includePhones
        return this
    }

    fun includeEmails(includeEmails: Boolean): ContactsQueryBuilder<T> {
        this.includeEmails = includeEmails
        return this
    }

    fun fetchAll(): Observable<List<T>> {
        return buildLoadEngineConfiguration().fetchAll()
    }

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