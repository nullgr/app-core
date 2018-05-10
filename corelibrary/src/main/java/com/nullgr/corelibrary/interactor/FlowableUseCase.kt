package com.nullgr.corelibrary.interactor

import com.nullgr.corelibrary.rx.applyScheduler
import com.nullgr.corelibrary.rx.schedulers.SchedulersFacade
import io.reactivex.Flowable

/**
 * Abstract class for a UseCase that returns an instance of [Flowable].
 *
 * @author vchernyshov
 */
abstract class FlowableUseCase<T, in Params> protected constructor(
        private val schedulersFacade: SchedulersFacade) {

    /**
     * Builds a [Flowable] which will be used when the current [FlowableUseCase] is executed.
     */
    protected abstract fun buildUseCaseObservable(params: Params? = null): Flowable<T>

    /**
     * Executes the current use case.
     */
    open fun execute(params: Params? = null): Flowable<T> {
        return buildUseCaseObservable(params).applyScheduler(schedulersFacade)
    }
}