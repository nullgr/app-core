package com.nullgr.core.hardware

import android.annotation.TargetApi
import android.content.Context
import android.content.pm.PackageManager
import android.location.LocationManager
import android.os.Build

/**
 * Extension function checks whether the [PackageManager.FEATURE_CAMERA] feature is available
 */
fun Context?.isCammeraSupported(): Boolean {
    return this?.packageManager?.hasSystemFeature(PackageManager.FEATURE_CAMERA) ?: false
}

/**
 * Extension function checks whether the [PackageManager.FEATURE_CAMERA_AUTOFOCUS] feature is available
 */
fun Context?.isCammeraAutoFocusSupported(): Boolean {
    return this?.packageManager?.hasSystemFeature(PackageManager.FEATURE_CAMERA_AUTOFOCUS) ?: false
}

/**
 * Extension function checks whether the [PackageManager.FEATURE_TELEPHONY] feature is available
 */
fun Context?.isTelephonySupported(): Boolean {
    return this?.packageManager?.hasSystemFeature(PackageManager.FEATURE_TELEPHONY) ?: false
}

/**
 * Extension function checks whether the [PackageManager.FEATURE_LOCATION] feature is available
 */
fun Context?.isGpsSupported(): Boolean {
    return this?.packageManager?.hasSystemFeature(PackageManager.FEATURE_LOCATION) ?: false
}

/**
 * Extension function checks whether the [PackageManager.FEATURE_FINGERPRINT] feature is available
 */
@TargetApi(Build.VERSION_CODES.M)
fun Context?.isFingerprintSupported(): Boolean {
    return this?.packageManager?.hasSystemFeature(PackageManager.FEATURE_FINGERPRINT) ?: false
}

/**
 * Extension function checks whether the [PackageManager.FEATURE_BLUETOOTH] feature is available
 */
fun Context?.isBluetoothSupported(): Boolean {
    return this?.packageManager?.hasSystemFeature(PackageManager.FEATURE_BLUETOOTH) ?: false
}

/**
 * Extension function checks whether the [Context.LOCATION_SERVICE] is available and one
 * of the [LocationManager.GPS_PROVIDER] or [LocationManager.NETWORK_PROVIDER] providers is enabled.
 */
fun Context?.isLocationEnabled(): Boolean {
    var gpsEnabled = false
    var networkEnabled = false

    val lm: LocationManager? = this?.getSystemService(Context.LOCATION_SERVICE) as LocationManager

    try {
        gpsEnabled = lm?.isProviderEnabled(LocationManager.GPS_PROVIDER) ?: false
    } catch (ignored: Exception) {
    }

    try {
        networkEnabled = lm?.isProviderEnabled(LocationManager.NETWORK_PROVIDER) ?: false
    } catch (ignored: Exception) {
    }

    return gpsEnabled || networkEnabled
}

