package com.nullgr.corelibrary.interactor

import com.nullgr.corelibrary.rx.applyScheduler
import com.nullgr.corelibrary.rx.schedulers.SchedulersFacade
import io.reactivex.Completable

/**
 * Abstract class for a UseCase that returns an instance of a [Completable].
 */
abstract class CompletableUseCase<in Params> protected constructor(
        private val schedulersFacade: SchedulersFacade) {

    /**
     * Builds a [Completable] which will be used when the current [CompletableUseCase] is executed.
     */
    protected abstract fun buildUseCaseObservable(params: Params? = null): Completable

    /**
     * Executes the current use case.
     */
    open fun execute(params: Params? = null): Completable {
        return buildUseCaseObservable(params).applyScheduler(schedulersFacade)
    }
}