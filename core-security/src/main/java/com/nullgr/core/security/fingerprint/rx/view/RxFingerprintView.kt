package com.nullgr.core.security.fingerprint.rx.view

import com.nullgr.core.security.fingerprint.FingerprintView
import io.reactivex.functions.Consumer

/**
 * Internal representation of [FingerprintView] for [com.nullgr.core.security.fingerprint.rx.RxFingerprintAuthenticationManger]
 * @author Grishko Nikita
 */
internal class RxFingerprintView(private val stateConsumer: Consumer<FingerprintViewState>) : FingerprintView {

    override fun onShowFingerprintListening() {
        stateConsumer.accept(FingerprintViewState(FingerprintViewState.State.START_LISTENING))
    }

    override fun onShowAuthenticationSuccess() {
        stateConsumer.accept(FingerprintViewState(FingerprintViewState.State.AUTHENTICATION_SUCCESS))
    }

    override fun onShowAuthenticationError(errorMessageId: Int?, errorMessage: String?) {
        stateConsumer.accept(
            FingerprintViewState(FingerprintViewState.State.AUTHENTICATION_ERROR,
                FingerprintViewState.Meta(errorMessageId, errorMessage))
        )
    }

    override fun onShowAuthenticationHelp(helpMessageId: Int, helpMessage: String) {
        stateConsumer.accept(
            FingerprintViewState(FingerprintViewState.State.AUTHENTICATION_HELP,
                FingerprintViewState.Meta(helpMessageId, helpMessage))
        )
    }

    override fun onResetFingerprintUiStateAfterError() {
        stateConsumer.accept(FingerprintViewState(FingerprintViewState.State.RESET_AFTER_ERROR))
    }
}