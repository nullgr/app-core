package com.nullgr.androidcore.interactor.domain.interactor

import com.nullgr.androidcore.interactor.domain.repository.UserRepositoryExample
import com.nullgr.corelibrary.interactor.SingleUseCase
import com.nullgr.corelibrary.rx.schedulers.SchedulersFacade
import io.reactivex.Single

/**
 * @author chernyshov.
 */
class GetUserPhoneUseCase(
        private val userRepository: UserRepositoryExample,
        schedulersFacade: SchedulersFacade)
    : SingleUseCase<String, String>(schedulersFacade) {

    override fun buildUseCaseObservable(params: String?): Single<String> {
        if (params == null) {
            throw IllegalArgumentException("Params is required for ${this.javaClass.name}")
        }
        return userRepository.getUserPhone(params)
    }
}