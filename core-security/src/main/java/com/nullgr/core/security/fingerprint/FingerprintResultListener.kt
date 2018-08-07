package com.nullgr.core.security.fingerprint

import android.support.v4.hardware.fingerprint.FingerprintManagerCompat

/**
 * Created by Grishko Nikita on 01.02.18.
 */
interface FingerprintResultListener {
    fun onSuccess(cryptoObject: FingerprintManagerCompat.CryptoObject?)
}