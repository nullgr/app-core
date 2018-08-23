package com.nullgr.core.security.fingerprint.rx.view

/**
 * Created by Grishko Nikita on 01.02.18.
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