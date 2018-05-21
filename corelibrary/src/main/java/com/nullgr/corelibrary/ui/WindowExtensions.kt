package com.nullgr.corelibrary.ui

import android.os.Build
import android.support.annotation.ColorRes
import android.support.v4.content.ContextCompat
import android.support.v4.view.ViewCompat
import android.view.View
import android.view.ViewGroup
import android.view.Window
import android.view.WindowManager

/**
 * @author chernyshov.
 */
fun Window.applyInsetsToContentView(fitsSystemWindows: Boolean) {
    (findViewById<View>(Window.ID_ANDROID_CONTENT) as ViewGroup).getChildAt(0)?.let {
        it.fitsSystemWindows = fitsSystemWindows
        ViewCompat.requestApplyInsets(it)
    }
}

fun Window.setStatusBarColor(@ColorRes color: Int, windowLightStatusBar: Boolean) {
    clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS)
    addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
        var flags = decorView.systemUiVisibility
        flags = if (!windowLightStatusBar) flags.or(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR) else flags.and(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR.inv())
        decorView.systemUiVisibility = flags
    }
    statusBarColor = ContextCompat.getColor(context, color)
}