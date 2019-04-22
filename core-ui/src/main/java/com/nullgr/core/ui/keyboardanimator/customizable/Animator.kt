package com.nullgr.core.ui.keyboardanimator.customizable

import android.view.View

interface Animator {

    /**
     * This method will be called when the soft keyboard visibility changes.
     * Here you can animate adjustment of [view].
     *
     * @param view view that corresponds to [CustomizableKeyboardAnimator.animatedViewId]
     * @param offset offset that was caused by the soft keyboard
     * @return `true` if [offset] was consumed, `false` otherwise to let the system handle it
     */
    fun animate(view: View, offset: Int): Boolean
}