package com.nullgr.core.security.fingerprint.rx.view

/**
 * Represents current state of fingerprint UI.
 * An instance of this class will be emits by
 * [com.nullgr.core.security.fingerprint.rx.RxFingerprintAuthenticationManger.observeViewState]
 * @property state - current state.
 * @property meta - provides additional information for some states.
 * [Meta.message] can be null so you should handle fallback scenario for this case.
 * For [State.AUTHENTICATION_ERROR] this will be an error message.
 * For [State.AUTHENTICATION_HELP] this will be a help message.
 *
 * @author Grishko Nikita
 */
data class FingerprintViewState(val state: State, val meta: Meta? = null) {

    enum class State {
        START_LISTENING,
        RESET_AFTER_ERROR,
        AUTHENTICATION_SUCCESS,
        AUTHENTICATION_ERROR,
        AUTHENTICATION_HELP
    }

    data class Meta(val id: Int?, val message: String?)
}