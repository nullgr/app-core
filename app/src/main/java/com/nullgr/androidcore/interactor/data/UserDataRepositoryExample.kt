package com.nullgr.androidcore.interactor.data

import com.nullgr.androidcore.interactor.domain.repository.UserRepositoryExample
import io.reactivex.Completable
import io.reactivex.Flowable
import io.reactivex.Observable
import io.reactivex.Single
import java.util.concurrent.TimeUnit

/**
 * @author chernyshov.
 */
class UserDataRepositoryExample : UserRepositoryExample {

    override fun requestPasswordReset(userId: String): Completable = Completable.complete().delay(DELAY_TIME_SECONDS, TimeUnit.SECONDS)

    override fun getUsers(): Flowable<List<String>> = Flowable.fromCallable {
        listOf(
                "User 1",
                "User 2",
                "User 3",
                "User 4",
                "User 5"
        )
    }.delay(DELAY_TIME_SECONDS, TimeUnit.SECONDS)

    override fun getUser(id: String): Flowable<String> = Flowable.fromCallable {
        "User $id"
    }.delay(DELAY_TIME_SECONDS, TimeUnit.SECONDS)

    override fun getUserFriends(userId: String): Observable<List<String>> = Observable.fromCallable {
        listOf(
                "Friend 1 of user with id $userId",
                "Friend 2 of user with id $userId",
                "Friend 3 of user with id $userId",
                "Friend 4 of user with id $userId",
                "Friend 5 of user with id $userId"
        )
    }.delay(DELAY_TIME_SECONDS, TimeUnit.SECONDS)

    override fun getUserPhoto(userId: String): Observable<String> = Observable.fromCallable {
        "Photo of user with id $userId"
    }.delay(DELAY_TIME_SECONDS, TimeUnit.SECONDS)

    override fun getSuggestedFriends(userId: String): Single<List<String>> = Single.fromCallable {
        listOf(
                "Suggested friend 1 of user with id $userId",
                "Suggested friend 2 of user with id $userId",
                "Suggested friend 3 of user with id $userId",
                "Suggested friend 4 of user with id $userId",
                "Suggested friend 5 of user with id $userId"
        )
    }.delay(DELAY_TIME_SECONDS, TimeUnit.SECONDS)

    override fun getUserPhone(userId: String): Single<String> = Single.fromCallable {
        "Phone of user with id $userId"
    }.delay(DELAY_TIME_SECONDS, TimeUnit.SECONDS)

    companion object {
        const val DELAY_TIME_SECONDS = 2L
    }
}