package com.nullgr.corelibrary.rx.schedulers

import io.reactivex.Scheduler
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers

/**
 * @author chernyshov.
 */
class IoToMainSchedulersFacade : SchedulersFacade {

    override val subscribeOn: Scheduler
        get() = Schedulers.io()

    override val observeOn: Scheduler
        get() = AndroidSchedulers.mainThread()
}