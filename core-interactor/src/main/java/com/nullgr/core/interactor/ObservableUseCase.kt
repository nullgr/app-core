package com.nullgr.core.interactor

import com.nullgr.core.rx.applyScheduler
import com.nullgr.core.rx.schedulers.SchedulersFacade
import io.reactivex.Observable

/**
 * Abstract class for a UseCase that returns an instance of [Observable].
 *
 * @author vchernyshov
 */
abstract class ObservableUseCase<T, in Params> protected constructor(
    private val schedulersFacade: SchedulersFacade
) {

    /**
     * Builds a [Observable] which will be used when the current [ObservableUseCase] is executed.
     */
    protected abstract fun buildUseCaseObservable(params: Params? = null): Observable<T>

    /**
     * Executes the current use case.
     */
    open fun execute(params: Params? = null): Observable<T> =
        buildUseCaseObservable(params).applyScheduler(schedulersFacade)
}