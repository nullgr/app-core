package com.nullgr.core.common

import android.os.Build

/**
 * A simple class for defining branching depending on the current version of the SDK
 * @author Grishko Nikita
 */
class SDKVersion(private val versionCode: Int) {

    fun doIfHigher(function: () -> Unit): SDKVersion {
        if (Build.VERSION.SDK_INT >= versionCode) {
            function.invoke()
        }
        return this
    }

    fun doIfLower(function: () -> Unit): SDKVersion {
        if (Build.VERSION.SDK_INT < versionCode) {
            function.invoke()
        }
        return this
    }
}

/**
 * The factory function to create a new SDKVersion instance with the specified version code
 */
fun forVersion(versionCode: Int): SDKVersion {
    return SDKVersion(versionCode)
}