package com.nullgr.corelibrary.interactor

import com.nullgr.corelibrary.rx.applySchedulers
import com.nullgr.corelibrary.rx.schedulers.SchedulersFacade
import io.reactivex.Observable

/**
 * Abstract class for a UseCase that returns an instance of a [Observable].
 */
abstract class ObservableUseCase<T, in Params> protected constructor(
        private val schedulersFacade: SchedulersFacade) {

    /**
     * Builds a [Observable] which will be used when the current [ObservableUseCase] is executed.
     */
    protected abstract fun buildUseCaseObservable(params: Params? = null): Observable<T>

    /**
     * Executes the current use case.
     */
    open fun execute(params: Params? = null): Observable<T> {
        return buildUseCaseObservable(params).applySchedulers(schedulersFacade)
    }
}