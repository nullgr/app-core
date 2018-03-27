package com.nullgr.corelibrary.interactor

import com.nullgr.corelibrary.rx.applySchedulers
import com.nullgr.corelibrary.rx.schedulers.SchedulersFacade
import io.reactivex.Completable

/**
 * Abstract class for a UseCase that returns an instance of a [Completable].
 */
abstract class CompletableUseCase<T, in Params> protected constructor(
        val schedulersFacade: SchedulersFacade) {

    /**
     * Builds a [Completable] which will be used when the current [CompletableUseCase] is executed.
     */
    protected abstract fun buildUseCaseObservable(params: Params): Completable

    /**
     * Executes the current use case.
     */
    open fun execute(params: Params): Completable {
        return buildUseCaseObservable(params).applySchedulers(schedulersFacade)
    }
}