package com.nullgr.core.ui.keyboardanimator

import android.annotation.TargetApi
import android.os.Build
import android.support.annotation.RequiresApi
import android.view.View
import android.view.Window
import android.view.WindowManager

/**
 * Base class for keyboard animators. [WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE] will
 * be set as soft input mode for [window] to provide proper obtaining of the insets.
 */
@RequiresApi(Build.VERSION_CODES.KITKAT_WATCH)
@TargetApi(Build.VERSION_CODES.KITKAT_WATCH)
abstract class BaseKeyboardAnimator(private val window: Window) {

    /**
     * [View.OnApplyWindowInsetsListener] that encapsulates all the keyboard animation logic.
     */
    protected abstract val insetsListener: View.OnApplyWindowInsetsListener

    init {
        window.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE)
    }

    /**
     * Method to start animating the keyboard appearing. Calling this method sets [insetsListener] as
     * [View.OnApplyWindowInsetsListener] to the [Window.getDecorView].
     */
    fun start() = window.decorView.setOnApplyWindowInsetsListener(insetsListener)

    /**
     * Method to stop animating the keyboard appearing. Calling this method sets `null` as
     * [View.OnApplyWindowInsetsListener] to the [Window.getDecorView].
     */
    fun stop() = window.decorView.setOnApplyWindowInsetsListener(null)
}