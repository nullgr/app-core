package com.nullgr.core.ui.animation

import android.os.Build
import android.view.View
import android.view.ViewAnimationUtils
import com.nullgr.core.common.withVersion
import com.nullgr.core.ui.extensions.disappear
import com.nullgr.core.ui.extensions.show


/**
 * Simple extension function, that provides cross fade animation between to views
 * @receiver Currently visible [View]
 * @param other [View] thar will be visible after animation finish
 */
infix fun <T : View> T.crossFadeTo(other: T) {
    this.animate().alpha(0f)?.doOnEnd {
        this.disappear()
        this.alpha = 1f
        other.alpha = 0f
        other.show()
        other.animate().alpha(1f)
    }
}

/**
 * Simple extension function, that provides reveal animation between to views.
 * Can be used for [Build.VERSION_CODES] before [Build.VERSION_CODES.LOLLIPOP].
 * @receiver Currently visible [View]
 * @param other [View] thar will be visible after animation finish
 */
infix fun <T : View> T.revealTo(other: T) {
    withVersion(Build.VERSION_CODES.LOLLIPOP) {
        higherOrEqual {
            val width = this@revealTo.measuredWidth
            val height = this@revealTo.measuredHeight
            val maxRadius = Math.max(width, height).toFloat()
            val reveal = ViewAnimationUtils.createCircularReveal(other, width / 2, height / 2, 0f, maxRadius)
            other.show()
            this@revealTo.disappear()
            reveal.start()
        }
        lower {
            this@revealTo.disappear()
            other.show()
        }
    }
}