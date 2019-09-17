package com.nullgr.core.ui.keyboardanimator.customizable

import android.annotation.TargetApi
import android.os.Build
import androidx.annotation.RequiresApi
import android.view.View
import android.view.Window
import com.nullgr.core.ui.keyboardanimator.BaseKeyboardAnimator

/**
 * This animator delegates all the animation logic to [animator]. By default, [PaddingAnimator]
 * will be used as [animator] and [Window.ID_ANDROID_CONTENT] will be used as [animatedViewId].
 */
@RequiresApi(Build.VERSION_CODES.LOLLIPOP)
@TargetApi(Build.VERSION_CODES.LOLLIPOP)
class CustomizableKeyboardAnimator @JvmOverloads constructor(
        window: Window,
        private val animator: Animator = PaddingAnimator(),
        private val animatedViewId: Int = Window.ID_ANDROID_CONTENT
) : BaseKeyboardAnimator(window) {

    private val animatedView: View? by lazy(LazyThreadSafetyMode.NONE) {
        window.decorView.findViewById<View>(animatedViewId)
    }

    override val insetsListener: View.OnApplyWindowInsetsListener
        get() = WindowInsetsListener { offset ->
            animatedView?.let { view ->
                return@WindowInsetsListener animator.animate(view, offset)
            }
            return@WindowInsetsListener false
        }
}