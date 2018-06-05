package com.nullgr.core.interactor

import com.nullgr.core.rx.applyScheduler
import com.nullgr.core.rx.schedulers.SchedulersFacade
import io.reactivex.Single

/**
 * Abstract class for a UseCase that returns an instance of [Single].
 *
 * @author vchernyshov
 */
abstract class SingleUseCase<T, in Params> protected constructor(
        private val schedulersFacade: SchedulersFacade) {

    /**
     * Builds a [Single] which will be used when the current [SingleUseCase] is executed.
     */
    protected abstract fun buildUseCaseObservable(params: Params? = null): Single<T>

    /**
     * Executes the current use case.
     */
    open fun execute(params: Params? = null): Single<T> {
        return buildUseCaseObservable(params).applyScheduler(schedulersFacade)
    }
}