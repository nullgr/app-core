package com.nullgr.androidcore.interactor.domain.interactor

import com.nullgr.androidcore.interactor.domain.repository.UserRepositoryExample
import com.nullgr.corelibrary.interactor.ObservableListUseCase
import com.nullgr.corelibrary.rx.schedulers.SchedulersFacade
import io.reactivex.Observable

/**
 * @author chernyshov.
 */
class GetUserFriendsPhotoUseCase(
        private val userRepository: UserRepositoryExample,
        schedulersFacade: SchedulersFacade)
    : ObservableListUseCase<String, String>(schedulersFacade) {

    override fun buildUseCaseObservable(params: String?): Observable<List<String>> {
        if (params == null) {
            throw IllegalArgumentException("Params is required for ${this.javaClass.name}")
        }
        return userRepository.getUserFriends(params)
    }
}