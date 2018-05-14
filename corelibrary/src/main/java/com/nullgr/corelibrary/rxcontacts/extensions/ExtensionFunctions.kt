package com.nullgr.corelibrary.rxcontacts.extensions

import com.nullgr.corelibrary.rxcontacts.domain.BaseContact
import com.nullgr.corelibrary.rxcontacts.domain.ContactEmail
import com.nullgr.corelibrary.rxcontacts.domain.ContactPhone
import io.reactivex.Observable

/**
 * Created by Grishko Nikita on 01.02.18.
 */
internal infix fun List<BaseContact>.has(id: Int): Boolean = this.any { it.id == id.toLong() }

internal val emptyPhoneContactsList: Observable<ArrayList<ContactPhone>>
        by lazy { Observable.just(arrayListOf<ContactPhone>()) }

internal val emptyEmailContactsList: Observable<ArrayList<ContactEmail>>
        by lazy { Observable.just(arrayListOf<ContactEmail>()) }