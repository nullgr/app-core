package com.nullgr.core.ui.keyboardanimator.simple

import android.annotation.TargetApi
import android.os.Build
import android.support.annotation.RequiresApi
import android.transition.ChangeBounds
import android.transition.TransitionManager
import android.view.View
import android.view.ViewGroup
import android.view.Window
import com.nullgr.core.ui.keyboardanimator.BaseKeyboardAnimator

/**
 * This animator starts delayed [ChangeBounds] transition before system get chance to apply insets.
 */
@RequiresApi(Build.VERSION_CODES.KITKAT_WATCH)
@TargetApi(Build.VERSION_CODES.KITKAT_WATCH)
class SimpleKeyboardAnimator(window: Window) : BaseKeyboardAnimator(window) {

    private val sceneRoot: ViewGroup? by lazy(LazyThreadSafetyMode.NONE) {
        window.decorView.findViewById<View>(Window.ID_ANDROID_CONTENT)?.parent as? ViewGroup
    }

    override val insetsListener: View.OnApplyWindowInsetsListener
        get() = View.OnApplyWindowInsetsListener { view, insets ->
            sceneRoot?.let { TransitionManager.beginDelayedTransition(it, ChangeBounds()) }
            return@OnApplyWindowInsetsListener view.onApplyWindowInsets(insets)
        }
}