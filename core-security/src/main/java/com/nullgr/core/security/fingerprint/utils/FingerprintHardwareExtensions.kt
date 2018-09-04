package com.nullgr.core.security.fingerprint.utils

import android.annotation.SuppressLint
import android.app.KeyguardManager
import android.content.Context
import android.os.Build
import android.support.v4.hardware.fingerprint.FingerprintManagerCompat


fun Context.getFingerprintManger() = FingerprintManagerCompat.from(this)

fun Context.getKeyguardManager() =
    if (isMarshmallow())
        this.getSystemService(KeyguardManager::class.java)
    else null

fun isMarshmallow() = Build.VERSION.SDK_INT >= 23

fun Context.isFingerprintHardwareDetected() = getFingerprintManger().isHardwareDetected

fun Context.hasEnrolledFingerprints() = getFingerprintManger().hasEnrolledFingerprints()

@SuppressLint("NewApi")
fun Context.isKeyguardSecure() = getKeyguardManager()?.isKeyguardSecure ?: false