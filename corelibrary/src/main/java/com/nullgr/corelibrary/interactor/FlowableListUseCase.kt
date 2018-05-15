package com.nullgr.corelibrary.interactor

import com.nullgr.corelibrary.rx.schedulers.SchedulersFacade

/**
 * Abstract class for a UseCase that returns an instance of [io.reactivex.Flowable].
 *
 * @author vchernyshov
 */
abstract class FlowableListUseCase<T, in Params> constructor(
        schedulersFacade: SchedulersFacade) :
        FlowableUseCase<List<T>, Params>(schedulersFacade)