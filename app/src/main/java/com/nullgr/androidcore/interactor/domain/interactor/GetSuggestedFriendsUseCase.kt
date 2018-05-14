package com.nullgr.androidcore.interactor.domain.interactor

import com.nullgr.androidcore.interactor.domain.repository.UserRepositoryExample
import com.nullgr.corelibrary.interactor.SingleListUseCase
import com.nullgr.corelibrary.rx.schedulers.SchedulersFacade
import io.reactivex.Single

/**
 * @author chernyshov.
 */
class GetSuggestedFriendsUseCase(
        private val userRepository: UserRepositoryExample,
        schedulersFacade: SchedulersFacade)
    : SingleListUseCase<String, String>(schedulersFacade) {

    override fun buildUseCaseObservable(params: String?): Single<List<String>> {
        if (params == null) {
            throw IllegalArgumentException("Params is required for ${this.javaClass.name}")
        }
        return userRepository.getSuggestedFriends(params)
    }
}