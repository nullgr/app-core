package com.nullgr.core.hardware

import android.annotation.TargetApi
import android.content.Context
import android.content.pm.PackageManager
import android.location.LocationManager
import android.os.Build
import com.google.android.gms.common.ConnectionResult
import com.google.android.gms.common.GoogleApiAvailability

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


fun Context?.isGoogleServicesPresent(): Boolean {
    return try {
        GoogleApiAvailability.getInstance().isGooglePlayServicesAvailable(this) == ConnectionResult.SUCCESS
    } catch (err: Throwable) {
        false
    }
}

