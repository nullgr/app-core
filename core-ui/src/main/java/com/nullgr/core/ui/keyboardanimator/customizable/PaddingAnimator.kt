package com.nullgr.core.ui.keyboardanimator.customizable

import android.annotation.TargetApi
import android.os.Build
import androidx.annotation.RequiresApi
import android.transition.ChangeBounds
import android.transition.TransitionManager
import android.view.View
import android.view.ViewGroup

/**
 * Default [Animator] which sets bottom padding to the animated view.
 */
@RequiresApi(Build.VERSION_CODES.KITKAT)
@TargetApi(Build.VERSION_CODES.KITKAT)
class PaddingAnimator : Animator {

    override fun animate(view: View, offset: Int): Boolean {
        (view.parent as? ViewGroup)?.let { sceneRoot ->
            TransitionManager.beginDelayedTransition(sceneRoot, ChangeBounds())
            view.setBottomPadding(offset)
            return true
        }
        return false
    }

    private fun View.setBottomPadding(padding: Int) = setPadding(paddingLeft, paddingTop, paddingRight, padding)
}