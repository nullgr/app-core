package com.nullgr.corelibrary.interactor

import com.nullgr.corelibrary.rx.schedulers.SchedulersFacade
import io.reactivex.Observable

/**
 * Abstract class for a UseCase that returns an instance of a [Observable].
 */
abstract class ObservableListUseCase<T, in Params> constructor(
        schedulersFacade: SchedulersFacade) :
        ObservableUseCase<List<T>, Params>(schedulersFacade)