package com.nullgr.corelibrary.rxcontacts.extensions

import com.nullgr.corelibrary.rxcontacts.domain.BaseContact
import com.nullgr.corelibrary.rxcontacts.domain.ContactEmail
import com.nullgr.corelibrary.rxcontacts.domain.ContactPhone
import io.reactivex.Observable

/**
 * Extension function to check if given [List] of [BaseContact] contains contacts with given id
 * @receiver list of [BaseContact]
 * @param id - [Int] id of contact to check
 * @return true if contains and false otherwise
 */
internal infix fun List<BaseContact>.has(id: Int): Boolean = this.any { it.id == id.toLong() }

/**
 * Observable that emits empty [ArrayList] of [ContactPhone]
 */
internal val emptyPhoneContactsList: Observable<ArrayList<ContactPhone>>
        by lazy { Observable.just(arrayListOf<ContactPhone>()) }

/**
 * Observable that emits empty [ArrayList] of [ContactEmail]
 */
internal val emptyEmailContactsList: Observable<ArrayList<ContactEmail>>
        by lazy { Observable.just(arrayListOf<ContactEmail>()) }