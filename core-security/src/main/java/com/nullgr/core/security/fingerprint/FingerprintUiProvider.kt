package com.nullgr.core.security.fingerprint

/**
 * Created by Grishko Nikita on 01.02.18.
 */
interface FingerprintUiProvider {

    fun showFingerprintListening()

    fun showAuthenticationSuccess()

    fun showAuthenticationError(errorMessageId: Int? = null, errorMessage: String? = null)

    fun showAuthenticationHelp(helpMessageId: Int, helpMessage: String)

    fun resetFingerprintUiStateAfterError()
}