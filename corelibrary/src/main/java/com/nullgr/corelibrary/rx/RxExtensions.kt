package com.nullgr.corelibrary.rx

import com.jakewharton.rxrelay2.Relay
import com.nullgr.corelibrary.rx.schedulers.SchedulersFacade
import io.reactivex.Completable
import io.reactivex.Flowable
import io.reactivex.Observable
import io.reactivex.Single
import io.reactivex.functions.Consumer
import io.reactivex.rxkotlin.Observables
import java.util.concurrent.TimeUnit

/**
 * @author chernyshov.
 */
fun <T> Single<T>.applySchedulers(schedulersFacade: SchedulersFacade): Single<T> {
    return subscribeOn(schedulersFacade.subscribeOn)
}

fun <T> Observable<T>.applySchedulers(schedulersFacade: SchedulersFacade): Observable<T> {
    return subscribeOn(schedulersFacade.subscribeOn)
}

fun <T> Observable<T>.applyBothSchedulers(schedulersFacade: SchedulersFacade): Observable<T> {
    return subscribeOn(schedulersFacade.subscribeOn).observeOn(schedulersFacade.observeOn)
}

fun <T> Flowable<T>.applySchedulers(schedulersFacade: SchedulersFacade): Flowable<T> {
    return subscribeOn(schedulersFacade.subscribeOn)
}

fun Completable.applySchedulers(schedulersFacade: SchedulersFacade): Completable {
    return subscribeOn(schedulersFacade.subscribeOn)
}

inline fun <T> Observable<T>.zipWithTimer(delay: Long): Observable<T> {
    return Observables.zip(this, Observable.timer(delay, TimeUnit.MILLISECONDS), { t, timerValue -> t })
}

inline fun <T> Observable<T>.bindProgress(progressConsumer: Consumer<Boolean>): Observable<T> {
    return this
            .doOnSubscribe { progressConsumer.accept(true) }
            .doFinally { progressConsumer.accept(false) }
}

inline fun <T> Observable<List<T>>.bindEmpty(emptyConsumer: Consumer<Boolean>): Observable<List<T>> {
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