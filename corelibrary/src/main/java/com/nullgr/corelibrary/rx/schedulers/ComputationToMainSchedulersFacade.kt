package com.nullgr.corelibrary.rx.schedulers

import io.reactivex.Scheduler
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers

/**
 * @author vchernyshov.
 */
class ComputationToMainSchedulersFacade : SchedulersFacade {

    override val subscribeOn: Scheduler
        get() = Schedulers.computation()

    override val observeOn: Scheduler
        get() = AndroidSchedulers.mainThread()
}