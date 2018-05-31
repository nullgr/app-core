package com.nullgr.corelibrary.common

import android.os.Build

/**
 * Created by Grishko Nikita on 01.02.18.
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

fun forVersion(versionCode: Int): SDKVersion {
    return SDKVersion(versionCode)
}