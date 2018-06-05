package com.nullgr.androidcore.interactor.domain.interactor

import com.nullgr.androidcore.interactor.domain.repository.UserRepositoryExample
import com.nullgr.core.interactor.FlowableUseCase
import com.nullgr.core.rx.schedulers.SchedulersFacade
import io.reactivex.Flowable

/**
 * @author chernyshov.
 */
class GetUserUseCase(
        private val userRepository: UserRepositoryExample,
        schedulersFacade: SchedulersFacade)
    : FlowableUseCase<String, String>(schedulersFacade) {

    override fun buildUseCaseObservable(params: String?): Flowable<String> {
        if (params == null) {
            throw IllegalArgumentException("Params is required for ${this.javaClass.name}")
        }
        return userRepository.getUser(params)
    }
}