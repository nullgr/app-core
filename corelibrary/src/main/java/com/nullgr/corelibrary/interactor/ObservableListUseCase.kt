package com.nullgr.corelibrary.interactor

import com.nullgr.corelibrary.rx.schedulers.SchedulersFacade

/**
 * Abstract class for a UseCase that returns an instance of [io.reactivex.Observable].
 *
 * @author vchernyshov
 */
abstract class ObservableListUseCase<T, in Params> constructor(
        schedulersFacade: SchedulersFacade) :
        ObservableUseCase<List<T>, Params>(schedulersFacade)