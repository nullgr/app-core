package com.nullgr.corelibrary.rx

import com.jakewharton.rxrelay2.Relay
import com.nullgr.corelibrary.rx.schedulers.SchedulersFacade
import io.reactivex.Completable
import io.reactivex.Flowable
import io.reactivex.Observable
import io.reactivex.Single
import io.reactivex.functions.BiFunction
import io.reactivex.functions.Consumer
import java.util.concurrent.TimeUnit

/**
 * Extension function that applies [SchedulersFacade.subscribeOn] scheduler for this [Single].
 * Helps to ensure code readability.
 */
inline fun <T> Single<T>.applyScheduler(schedulersFacade: SchedulersFacade): Single<T> {
    return subscribeOn(schedulersFacade.subscribeOn)
}

/**
 * Extension function that applies [SchedulersFacade.subscribeOn] scheduler for this [Observable].
 * Helps to ensure code readability.
 */
inline fun <T> Observable<T>.applyScheduler(schedulersFacade: SchedulersFacade): Observable<T> {
    return subscribeOn(schedulersFacade.subscribeOn)
}

/**
 * Extension function that applies [SchedulersFacade.subscribeOn] scheduler for this [Flowable].
 * Helps to ensure code readability.
 */
inline fun <T> Flowable<T>.applyScheduler(schedulersFacade: SchedulersFacade): Flowable<T> {
    return subscribeOn(schedulersFacade.subscribeOn)
}

/**
 * Extension function that applies [SchedulersFacade.subscribeOn] scheduler for this [Completable].
 * Helps to ensure code readability.
 */
inline fun Completable.applyScheduler(schedulersFacade: SchedulersFacade): Completable {
    return subscribeOn(schedulersFacade.subscribeOn)
}

/**
 * Extension function that applies [SchedulersFacade.subscribeOn]
 * and [SchedulersFacade.observeOn] schedulers for this [Single].
 * Helps to ensure code readability.
 */
inline fun <T> Single<T>.applySchedulers(schedulersFacade: SchedulersFacade): Single<T> {
    return subscribeOn(schedulersFacade.subscribeOn).observeOn(schedulersFacade.observeOn)
}

/**
 * Extension function that applies [SchedulersFacade.subscribeOn]
 * and [SchedulersFacade.observeOn] schedulers for this [Observable].
 * Helps to ensure code readability.
 */
inline fun <T> Observable<T>.applySchedulers(schedulersFacade: SchedulersFacade): Observable<T> {
    return subscribeOn(schedulersFacade.subscribeOn).observeOn(schedulersFacade.observeOn)
}

/**
 * Extension function that applies [SchedulersFacade.subscribeOn]
 * and [SchedulersFacade.observeOn] schedulers for this [Flowable].
 * Helps to ensure code readability.
 */
inline fun <T> Flowable<T>.applySchedulers(schedulersFacade: SchedulersFacade): Flowable<T> {
    return subscribeOn(schedulersFacade.subscribeOn).observeOn(schedulersFacade.observeOn)
}

/**
 * Extension function that applies [SchedulersFacade.subscribeOn]
 * and [SchedulersFacade.observeOn] schedulers for this [Completable].
 * Helps to ensure code readability.
 */
inline fun Completable.applySchedulers(schedulersFacade: SchedulersFacade): Completable {
    return subscribeOn(schedulersFacade.subscribeOn).observeOn(schedulersFacade.observeOn)
}

/**
 * Extension function that zip this [Single] with [Single.timer].
 * Example usage: add delay to request to show progress some min time.
 */
inline fun <T> Single<T>.zipWithTimer(delay: Long): Single<T> {
    return Single.zip(this, Single.timer(delay, TimeUnit.MILLISECONDS), BiFunction { t1, _ -> t1 })
}

/**
 * Extension function that zip this [Observable] with [Observable.timer].
 * Example usage: add delay to request to show progress some min time.
 */
inline fun <T> Observable<T>.zipWithTimer(delay: Long): Observable<T> {
    return Observable.zip(this, Observable.timer(delay, TimeUnit.MILLISECONDS), BiFunction { t1, _ -> t1 })
}

/**
 * Extension function that zip this [Flowable] with [Flowable.timer].
 * Example usage: add delay to request to show progress some min time.
 */
inline fun <T> Flowable<T>.zipWithTimer(delay: Long): Flowable<T> {
    return Flowable.zip(this, Flowable.timer(delay, TimeUnit.MILLISECONDS), BiFunction { t1, _ -> t1 })
}

/**
 * Extension function that applies side effect operators
 * [Single.doOnSubscribe] and [Single.doFinally] to add progress visibility handling.
 * Helps to ensure code readability.
 */
inline fun <T> Single<T>.bindProgress(progressConsumer: Consumer<Boolean>): Single<T> {
    return this
            .doOnSubscribe { progressConsumer.accept(true) }
            .doFinally { progressConsumer.accept(false) }
}

/**
 * Extension function that applies side effect operators
 * [Observable.doOnSubscribe] and [Observable.doFinally] to add progress visibility handling.
 * Helps to ensure code readability.
 */
inline fun <T> Observable<T>.bindProgress(progressConsumer: Consumer<Boolean>): Observable<T> {
    return this
            .doOnSubscribe { progressConsumer.accept(true) }
            .doFinally { progressConsumer.accept(false) }
}

/**
 * Extension function that applies side effect operators
 * [Flowable.doOnSubscribe] and [Flowable.doFinally] to add progress visibility handling.
 * Helps to ensure code readability.
 */
inline fun <T> Flowable<T>.bindProgress(progressConsumer: Consumer<Boolean>): Flowable<T> {
    return this
            .doOnSubscribe { progressConsumer.accept(true) }
            .doFinally { progressConsumer.accept(false) }
}

/**
 * Extension function that applies side effect operators
 * [Completable.doOnSubscribe] and [Completable.doFinally] to add progress visibility handling.
 * Helps to ensure code readability.
 */
inline fun Completable.bindProgress(progressConsumer: Consumer<Boolean>): Completable {
    return this
            .doOnSubscribe { progressConsumer.accept(true) }
            .doFinally { progressConsumer.accept(false) }
}

/**
 * Extension function that applies side effect operators
 * [Single.doOnSuccess] and [Single.doOnError] to add empty state handling.
 * Helps to ensure code readability.
 */
inline fun <T> Single<List<T>>.bindEmpty(emptyConsumer: Consumer<Boolean>): Single<List<T>> {
    return this
            .doOnSuccess { emptyConsumer.accept(it.isEmpty()) }
            .doOnError { emptyConsumer.accept(false) }
}

/**
 * Extension function that applies side effect operators
 * [Observable.doOnNext] and [Observable.doOnError] to add empty state handling.
 * Helps to ensure code readability.
 */
inline fun <T> Observable<List<T>>.bindEmpty(emptyConsumer: Consumer<Boolean>): Observable<List<T>> {
    return this
            .doOnNext { emptyConsumer.accept(it.isEmpty()) }
            .doOnError { emptyConsumer.accept(false) }
}

/**
 * Extension function that applies side effect operators
 * [Flowable.doOnNext] and [Flowable.doOnError] to add empty state handling.
 * Helps to ensure code readability.
 */
inline fun <T> Flowable<List<T>>.bindEmpty(emptyConsumer: Consumer<Boolean>): Flowable<List<T>> {
    return this
            .doOnNext { emptyConsumer.accept(it.isEmpty()) }
            .doOnError { emptyConsumer.accept(false) }
}

/**
 * Convenience to get this [Relay] as an [Consumer].
 * Helps to ensure code readability.
 */
inline fun <T> Relay<T>.asConsumer(): Consumer<T> {
    return this
}

/**
 * Convenience to get this [Relay] as an [Observable].
 * Helps to ensure code readability.
 */
inline fun <T> Relay<T>.asObservable(): Observable<T> {
    return this.hide()
}