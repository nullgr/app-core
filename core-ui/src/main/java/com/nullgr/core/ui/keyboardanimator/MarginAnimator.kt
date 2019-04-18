package com.nullgr.core.ui.keyboardanimator

import android.annotation.TargetApi
import android.os.Build
import android.support.annotation.RequiresApi
import android.transition.ChangeBounds
import android.transition.TransitionManager
import android.view.View
import android.view.ViewGroup

/**
 * Default [Animator] which adds bottom margin to the animated view.
 */
@RequiresApi(Build.VERSION_CODES.KITKAT)
@TargetApi(Build.VERSION_CODES.KITKAT)
class MarginAnimator : Animator {

    override fun animate(view: View, offset: Int): Boolean {
        val sceneRoot = view.parent as? ViewGroup
        val params = view.layoutParams as? ViewGroup.MarginLayoutParams
        if (sceneRoot != null && params != null) {
            TransitionManager.beginDelayedTransition(sceneRoot, ChangeBounds())
            view.layoutParams = params.apply { bottomMargin = offset }
            return true
        }
        return false
    }
}