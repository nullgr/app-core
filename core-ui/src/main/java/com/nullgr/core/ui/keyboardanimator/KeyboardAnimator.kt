package com.nullgr.core.ui.keyboardanimator

import android.annotation.TargetApi
import android.os.Build
import android.support.annotation.RequiresApi
import android.view.View
import android.view.Window
import android.view.WindowManager

/**
 * Class that encapsulates all the keyboard animation logic. [WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE] will
 * be set as soft input mode for [window] to provide proper obtaining of the insets.
 */
@RequiresApi(Build.VERSION_CODES.LOLLIPOP)
@TargetApi(Build.VERSION_CODES.LOLLIPOP)
class KeyboardAnimator @JvmOverloads constructor(
        private val window: Window,
        private val animator: Animator = MarginAnimator(),
        private val animatedViewId: Int = Window.ID_ANDROID_CONTENT
) {

    private val animatedView: View? by lazy(LazyThreadSafetyMode.NONE) {
        window.decorView.findViewById<View>(animatedViewId)
    }

    init {
        window.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE)
    }

    /**
     * Method to start animating the keyboard appearing. Calling this method sets [WindowInsetsListener] as
     * [View.OnApplyWindowInsetsListener] to the [Window.getDecorView] and delegates all animating logic to [animator].
     */
    fun start() {
        window.decorView.setOnApplyWindowInsetsListener(WindowInsetsListener { offset ->
            animatedView?.let { view ->
                return@WindowInsetsListener animator.animate(view, offset)
            }
            return@WindowInsetsListener false
        })
    }

    /**
     * Method to stop animating the keyboard appearing. Calling this method sets `null` as
     * [View.OnApplyWindowInsetsListener] to the [Window.getDecorView]
     */
    fun stop() = window.decorView.setOnApplyWindowInsetsListener(null)
}