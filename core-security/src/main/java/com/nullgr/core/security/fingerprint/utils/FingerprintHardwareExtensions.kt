package com.nullgr.core.security.fingerprint.utils

import android.annotation.SuppressLint
import android.app.KeyguardManager
import android.content.Context
import android.os.Build
import androidx.core.hardware.fingerprint.FingerprintManagerCompat

/**
 * Returns new instance of [FingerprintManagerCompat]
 */
fun Context.getFingerprintManger() = FingerprintManagerCompat.from(this)

/**
 * Returns new instance of [KeyguardManager] or null if current android version is lower than Marshmallow.
 */
fun Context.getKeyguardManager() =
    if (isMarshmallow())
        this.getSystemService(KeyguardManager::class.java)
    else null

fun isMarshmallow() = Build.VERSION.SDK_INT >= 23

/**
 * Extension function to quick check if fingerprint scanner is available.
 */
fun Context.isFingerprintHardwareDetected() = getFingerprintManger().isHardwareDetected

/**
 * Extension function to quick check if fingerprint authorization is activated on phone.
 */
fun Context.hasEnrolledFingerprints() = getFingerprintManger().hasEnrolledFingerprints()

/**
 * Extension function to quick check if is keyguard secure.
 */
@SuppressLint("NewApi")
fun Context.isKeyguardSecure() = getKeyguardManager()?.isKeyguardSecure ?: false