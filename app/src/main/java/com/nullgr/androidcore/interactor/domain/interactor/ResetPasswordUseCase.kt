package com.nullgr.androidcore.interactor.domain.interactor

import com.nullgr.androidcore.interactor.domain.repository.UserRepositoryExample
import com.nullgr.core.interactor.CompletableUseCase
import com.nullgr.core.rx.schedulers.SchedulersFacade
import io.reactivex.Completable

/**
 * @author chernyshov.
 */
class ResetPasswordUseCase(
        private val userRepository: UserRepositoryExample,
        schedulersFacade: SchedulersFacade)
    : CompletableUseCase<String>(schedulersFacade) {

    override fun buildUseCaseObservable(params: String?): Completable {
        if (params == null) {
            throw IllegalArgumentException("Params is required for ${this.javaClass.name}")
        }
        return userRepository.requestPasswordReset(params)
    }
}