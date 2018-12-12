package com.nullgr.core.security.fingerprint

import android.support.v4.hardware.fingerprint.FingerprintManagerCompat

/**
 * Callback for fingerprint authorization result.
 *
 * @author Grishko Nikita
 */
interface FingerprintResultListener {
    /**
     * This method will be called after success authorization
     * @param cryptoObject - an instance of [FingerprintManagerCompat.CryptoObject].
     * Can be null if null were passed to [FingerprintAuthenticationManager.startListening] as argument.
     */
    fun onSuccess(cryptoObject: FingerprintManagerCompat.CryptoObject?)
}