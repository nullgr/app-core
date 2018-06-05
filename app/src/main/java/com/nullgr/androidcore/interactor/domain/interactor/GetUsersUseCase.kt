package com.nullgr.androidcore.interactor.domain.interactor

import com.nullgr.androidcore.interactor.domain.repository.UserRepositoryExample
import com.nullgr.core.interactor.FlowableListUseCase
import com.nullgr.core.rx.schedulers.SchedulersFacade
import io.reactivex.Flowable

/**
 * @author chernyshov.
 */
class GetUsersUseCase(
        private val userRepository: UserRepositoryExample,
        schedulersFacade: SchedulersFacade)
    : FlowableListUseCase<String, Unit>(schedulersFacade) {

    override fun buildUseCaseObservable(params: Unit?): Flowable<List<String>> {
        return userRepository.getUsers()
    }
}