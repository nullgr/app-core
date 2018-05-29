package com.nullgr.core.common

import android.os.Build

/**
 * A simple class for defining branching depending on the current version of the SDK
 * @author Grishko Nikita
 */
class SDKVersion(val versionCode: Int) {

    inline fun higher(function: () -> Unit): SDKVersion {
        if (Build.VERSION.SDK_INT > versionCode) {
            function.invoke()
        }
        return this
    }

    inline fun higherOrEqual(function: () -> Unit): SDKVersion {
        if (Build.VERSION.SDK_INT >= versionCode) {
            function.invoke()
        }
        return this
    }

    inline fun lower(function: () -> Unit): SDKVersion {
        if (Build.VERSION.SDK_INT < versionCode) {
            function.invoke()
        }
        return this
    }

    inline fun lowerOrEqual(function: () -> Unit): SDKVersion {
        if (Build.VERSION.SDK_INT <= versionCode) {
            function.invoke()
        }
        return this
    }
}

/**
 * The factory function to create a new [SDKVersion] instance with the specified [versionCode] and calls
 * [function] with created instance as its argument.
 */
fun withVersion(versionCode: Int, function: SDKVersion.() -> Unit) {
    function.invoke(SDKVersion(versionCode))
}