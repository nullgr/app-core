package com.nullgr.core.ui.keyboardanimator

import android.annotation.TargetApi
import android.os.Build
import android.support.annotation.RequiresApi
import android.view.View
import android.view.Window

/**
 * Class that encapsulates all the keyboard animation logic.
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