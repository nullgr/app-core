package com.nullgr.corelibrary.rx.schedulers

import io.reactivex.Scheduler

/**
 * @author chernyshov.
 */
interface SchedulersFacade {

    val subscribeOn: Scheduler

    val observeOn: Scheduler

}