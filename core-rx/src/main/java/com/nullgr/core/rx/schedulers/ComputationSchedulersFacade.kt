package com.nullgr.core.rx.schedulers

import io.reactivex.Scheduler
import io.reactivex.schedulers.Schedulers

/**
 * Implementation of [SchedulersFacade] that provides [Schedulers.computation]
 * for both property [subscribeOn] and [observeOn].
 *
 * @author vchernyshov
 */
class ComputationSchedulersFacade : SchedulersFacade {

    override val subscribeOn: Scheduler
        get() = Schedulers.computation()

    override val observeOn: Scheduler
        get() = Schedulers.computation()
}