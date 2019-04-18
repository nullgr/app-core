package com.nullgr.core.ui.keyboardanimator

import android.annotation.TargetApi
import android.os.Build
import android.support.annotation.RequiresApi
import android.view.View
import android.view.WindowInsets
import kotlin.math.min

/**
 * [View.OnApplyWindowInsetsListener] that encapsulates logic for detecting the soft keyboard height.
 */
@RequiresApi(Build.VERSION_CODES.LOLLIPOP)
@TargetApi(Build.VERSION_CODES.LOLLIPOP)
class WindowInsetsListener(private val onOffsetChanged: (Int) -> Boolean) : View.OnApplyWindowInsetsListener {

    private var previousOffset: Int = 0

    override fun onApplyWindowInsets(decorView: View, insets: WindowInsets): WindowInsets {
        var result = insets
        val offset = when {
            insets.systemWindowInsetBottom < insets.stableInsetBottom -> insets.systemWindowInsetBottom
            else -> insets.systemWindowInsetBottom - insets.stableInsetBottom
        }
        if (offset != previousOffset && onOffsetChanged(offset)) {
            previousOffset = offset
            result = insets.consumeBottomInset()
        }
        return decorView.onApplyWindowInsets(result)
    }

    private fun WindowInsets.consumeBottomInset(): WindowInsets =
            replaceSystemWindowInsets(
                    systemWindowInsetLeft,
                    systemWindowInsetTop,
                    systemWindowInsetRight,
                    min(systemWindowInsetBottom, stableInsetBottom)
            )
}