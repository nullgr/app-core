package com.nullgr.corelibrary.rxcontacts

import android.content.ContentResolver
import android.content.Context
import android.net.Uri
import com.nullgr.corelibrary.rxcontacts.domain.BaseContact
import com.nullgr.corelibrary.rxcontacts.engine.ContactsLoadEngine
import com.nullgr.corelibrary.rxcontacts.engine.query.ContactsQueryBuilder
import io.reactivex.Observable


/**
 * Created by Grishko Nikita on 01.02.18.
 */
class RxContactsProvider private constructor(private val contactsLoadEngine: ContactsLoadEngine) {

    companion object {
        fun with(context: Context): RxContactsProvider {
            return RxContactsProvider(ContactsLoadEngine(context.contentResolver))
        }

        fun with(contentResolver: ContentResolver): RxContactsProvider {
            return RxContactsProvider(ContactsLoadEngine(contentResolver))
        }
    }

    fun <T : BaseContact> fetchAll(clazz: Class<T>): Observable<List<T>> {
        return contactsLoadEngine
                .withIncludeAdditionalData(true, true)
                .withQueryConfiguration(clazz)
                .fetchAll()
    }

    fun <T : BaseContact> fetchSingle(clazz: Class<T>): Observable<T> {
        return contactsLoadEngine
                .withIncludeAdditionalData(true, true)
                .withQueryConfiguration(clazz)
                .fetchSingle()
    }

    fun <T : BaseContact> fromUri(clazz: Class<T>, uri: Uri): Observable<List<T>> {
        return contactsLoadEngine
                .withIncludeAdditionalData(true, true)
                .withUriConfiguration(clazz, uri)
                .fetchAll()
    }

    fun <T : BaseContact> query(clazz: Class<T>): ContactsQueryBuilder<T> {
        return ContactsQueryBuilder.createQuery(clazz, contactsLoadEngine)
    }
}