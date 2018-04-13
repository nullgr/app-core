package com.nullgr.corelibrary.rx.schedulers

import io.reactivex.Scheduler
import io.reactivex.schedulers.Schedulers

/**
 * @author chernyshov.
 */
class ComputationSchedulersFacade : SchedulersFacade {

    override val subscribeOn: Scheduler
        get() = Schedulers.computation()

    override val observeOn: Scheduler
        get() = Schedulers.computation()
}