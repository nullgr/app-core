package com.nullgr.androidcore.interactor.domain.repository

import io.reactivex.Completable
import io.reactivex.Flowable
import io.reactivex.Observable
import io.reactivex.Single

/**
 * @author vchernyshov
 */
interface UserRepositoryExample {

    fun requestPasswordReset(userId: String): Completable

    fun getUsers(): Flowable<List<String>>

    fun getUser(id: String): Flowable<String>

    fun getUserFriends(userId: String): Observable<List<String>>

    fun getUserPhoto(userId: String): Observable<String>

    fun getSuggestedFriends(userId: String): Single<List<String>>

    fun getUserPhone(userId: String): Single<String>

}