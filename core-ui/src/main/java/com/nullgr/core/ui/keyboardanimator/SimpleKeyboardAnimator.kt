package com.nullgr.core.ui.keyboardanimator

import android.annotation.TargetApi
import android.os.Build
import android.support.annotation.RequiresApi
import android.transition.ChangeBounds
import android.transition.TransitionManager
import android.view.View
import android.view.ViewGroup
import android.view.Window
import android.view.WindowManager

@RequiresApi(Build.VERSION_CODES.KITKAT_WATCH)
@TargetApi(Build.VERSION_CODES.KITKAT_WATCH)
class SimpleKeyboardAnimator(private val window: Window) {

    private val sceneRoot: ViewGroup? by lazy(LazyThreadSafetyMode.NONE) {
        window.decorView.findViewById<View>(Window.ID_ANDROID_CONTENT)?.parent as? ViewGroup
    }

    init {
        window.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE)
    }

    fun start() {
        window.decorView.setOnApplyWindowInsetsListener { decorView, insets ->
            sceneRoot?.let { TransitionManager.beginDelayedTransition(it, ChangeBounds()) }
            return@setOnApplyWindowInsetsListener decorView.onApplyWindowInsets(insets)
        }
    }

    fun stop() = window.decorView.setOnApplyWindowInsetsListener(null)
}