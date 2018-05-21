package com.nullgr.core.rx.schedulers

import io.reactivex.Scheduler

/**
 * Helper interface that provides access of client code to [Scheduler]s.
 * Contains properties [subscribeOn] and [observeOn]
 * that can be used with reactive data sources:
 * [io.reactivex.Observable], [io.reactivex.Flowable],
 * [io.reactivex.Single] and [io.reactivex.Completable]
 *
 * @author vchernyshov
 */
interface SchedulersFacade {

    val subscribeOn: Scheduler

    val observeOn: Scheduler

}