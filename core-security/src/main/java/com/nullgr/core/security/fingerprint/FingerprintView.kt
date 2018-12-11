package com.nullgr.core.security.fingerprint

/**
 * Basic interface for any components that will represent fingerprint UI.
 *
 * @author Grishko Nikita
 */
interface FingerprintView {

    fun onShowFingerprintListening()

    fun onShowAuthenticationSuccess()

    fun onShowAuthenticationError(errorMessageId: Int? = null, errorMessage: String? = null)

    fun onShowAuthenticationHelp(helpMessageId: Int, helpMessage: String)

    fun onResetFingerprintUiStateAfterError()
}