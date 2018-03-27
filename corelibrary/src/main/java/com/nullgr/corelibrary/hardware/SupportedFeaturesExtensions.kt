package com.nullgr.corelibrary.hardware

import android.annotation.TargetApi
import android.content.Context
import android.content.pm.PackageManager
import android.os.Build

/**
 * Created by Grishko Nikita on 01.02.18.
 */
fun Context?.isCammeraSupported(): Boolean {
    return this?.packageManager?.hasSystemFeature(PackageManager.FEATURE_CAMERA) ?: false
}

fun Context?.isCammeraAutoFocusSupported(): Boolean {
    return this?.packageManager?.hasSystemFeature(PackageManager.FEATURE_CAMERA_AUTOFOCUS) ?: false
}

fun Context?.isTelephonySupported(): Boolean {
    return this?.packageManager?.hasSystemFeature(PackageManager.FEATURE_TELEPHONY) ?: false
}

fun Context?.isGpsSupported(): Boolean {
    return this?.packageManager?.hasSystemFeature(PackageManager.FEATURE_LOCATION) ?: false
}

@TargetApi(Build.VERSION_CODES.M)
fun Context?.isFingerprintSupported(): Boolean {
    return this?.packageManager?.hasSystemFeature(PackageManager.FEATURE_FINGERPRINT) ?: false
}

fun Context?.isBluetoothSupported(): Boolean {
    return this?.packageManager?.hasSystemFeature(PackageManager.FEATURE_BLUETOOTH) ?: false
}
