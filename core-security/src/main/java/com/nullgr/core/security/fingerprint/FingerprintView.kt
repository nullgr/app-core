package com.nullgr.core.security.fingerprint

/**
 * Created by Grishko Nikita on 01.02.18.
 */
interface FingerprintView {

    fun onShowFingerprintListening()

    fun onShowAuthenticationSuccess()

    fun onShowAuthenticationError(errorMessageId: Int? = null, errorMessage: String? = null)

    fun onShowAuthenticationHelp(helpMessageId: Int, helpMessage: String)

    fun onResetFingerprintUiStateAfterError()
}