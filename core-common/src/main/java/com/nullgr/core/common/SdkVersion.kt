package com.nullgr.core.common

import android.os.Build

/**
 * A simple class for defining branching depending on the current version of the SDK
 * @author Grishko Nikita
 */
class SdkVersion(val versionCode: Int) {

    inline fun higher(function: () -> Unit): SdkVersion {
        if (Build.VERSION.SDK_INT > versionCode) {
            function.invoke()
        }
        return this
    }

    inline fun higherOrEqual(function: () -> Unit): SdkVersion {
        if (Build.VERSION.SDK_INT >= versionCode) {
            function.invoke()
        }
        return this
    }

    inline fun lower(function: () -> Unit): SdkVersion {
        if (Build.VERSION.SDK_INT < versionCode) {
            function.invoke()
        }
        return this
    }

    inline fun lowerOrEqual(function: () -> Unit): SdkVersion {
        if (Build.VERSION.SDK_INT <= versionCode) {
            function.invoke()
        }
        return this
    }

    inline fun equal(function: () -> Unit): SdkVersion {
        if (Build.VERSION.SDK_INT == versionCode) {
            function.invoke()
        }
        return this
    }
}

/**
 * The factory function to create a new [SdkVersion] instance with the specified [versionCode] and calls
 * [function] with created instance as its argument.
 */
fun withVersion(versionCode: Int, function: SdkVersion.() -> Unit) {
    function.invoke(SdkVersion(versionCode))
}