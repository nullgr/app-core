package com.nullgr.androidcore.interactor.domain.interactor

import com.nullgr.androidcore.interactor.domain.repository.UserRepositoryExample
import com.nullgr.corelibrary.interactor.ObservableUseCase
import com.nullgr.corelibrary.rx.schedulers.SchedulersFacade
import io.reactivex.Observable

/**
 * @author chernyshov.
 */
class GetUserPhotoUseCase(
        private val userRepository: UserRepositoryExample,
        schedulersFacade: SchedulersFacade)
    : ObservableUseCase<String, String>(schedulersFacade) {

    override fun buildUseCaseObservable(params: String?): Observable<String> {
        if (params == null) {
            throw IllegalArgumentException("Params is required for ${this.javaClass.name}")
        }
        return userRepository.getUserPhoto(params)
    }
}