package com.nullgr.corelibrary.interactor

import com.nullgr.corelibrary.rx.schedulers.SchedulersFacade

/**
 * Abstract class for a UseCase that returns an instance of [io.reactivex.Single].
 *
 * @author vchernyshov
 */
abstract class SingleListUseCase<T, in Params> constructor(
        schedulersFacade: SchedulersFacade) :
        SingleUseCase<List<T>, Params>(schedulersFacade)