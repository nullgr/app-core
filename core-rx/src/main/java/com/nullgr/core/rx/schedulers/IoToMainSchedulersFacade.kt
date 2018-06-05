package com.nullgr.core.rx.schedulers

import io.reactivex.Scheduler
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers

/**
 * Implementation of [SchedulersFacade] that provides [Schedulers.io] for [subscribeOn] and
 * [AndroidSchedulers.mainThread] for [observeOn].
 *
 * @author vchernyshov
 */
class IoToMainSchedulersFacade : SchedulersFacade {

    override val subscribeOn: Scheduler
        get() = Schedulers.io()

    override val observeOn: Scheduler
        get() = AndroidSchedulers.mainThread()
}