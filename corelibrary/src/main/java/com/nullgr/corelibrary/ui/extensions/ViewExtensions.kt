package com.nullgr.corelibrary.ui.extensions

import android.view.View
import android.view.ViewGroup
import android.view.ViewTreeObserver

/**
 * Changing [View] visibility to [View.VISIBLE]
 */
fun <T : View> T.show() {
    this.visibility = View.VISIBLE
}

/**
 * Changing [View] visibility to [View.GONE]
 */
fun <T : View> T.hide() {
    this.visibility = View.GONE
}

/**
 * Changing [View] visibility to [View.INVISIBLE]
 */
fun <T : View> T.disappear() {
    this.visibility = View.INVISIBLE
}

/**
 * Changing [View] visibility to [View.VISIBLE] or [View.GONE] depends on [show] param
 * @param show [Boolean] flag
 */
fun <T : View> T.toggleView(show: Boolean) {
    if (show) this.show()
    else this.hide()
}

/**
 * Changing [View] visibility to one of two values of visibility passed as [defaultTrueState] and [defaultFalseState],
 * depends on [state] param
 * @param state [Boolean] flag, which indicates what state will be set
 * @param defaultTrueState [View] visibility which will be set when true pass as [state]
 * @param defaultFalseState [View] visibility which will be set when false passed as [state]
 */
fun <T : View> T.toggleVisibilityState(state: Boolean,
                                       defaultTrueState: Int = View.VISIBLE,
                                       defaultFalseState: Int = View.GONE) {
    visibility = if (state) defaultTrueState else defaultFalseState
}

/**
 * Change [View.setSelected] to inverse value
 */
fun View.toggleSelected() {
    isSelected = !isSelected
}

/**
 * Set [View.setAlpha] to all child of [ViewGroup]
 */
fun ViewGroup.alphaChilds(alpha: Float) {
    if (childCount > 0) {
        for (i in 0..childCount) {
            getChildAt(i)?.alpha = alpha
        }
    }
}

/**
 * Invoke given function after view is measured
 */
inline fun <T : View> T.afterMeasured(crossinline f: T.() -> Unit) {
    viewTreeObserver.addOnGlobalLayoutListener(object : ViewTreeObserver.OnGlobalLayoutListener {
        override fun onGlobalLayout() {
            if (measuredWidth > 0 && measuredHeight > 0) {
                viewTreeObserver.removeOnGlobalLayoutListener(this)
                f()
            }
        }
    })
}

/**
 * Calling [hide] for array of [View]
 * @param views array of views
 */
fun hideViews(vararg views: View?) {
    views.forEach {
        it?.hide()
    }
}

/**
 * Calling [show] for array of [View]
 * @param views array of views
 */
fun showViews(vararg views: View?) {
    views.forEach {
        it?.show()
    }
}

/**
 * Calling [disappear] for array of [View]
 * @param views array of views
 */
fun disappearViews(vararg views: View?) {
    views.forEach {
        it?.disappear()
    }
}

/**
 * Calling [toggleView] for array of [View]
 * @param views array of views
 */
fun toggleViews(state: Boolean, vararg views: View?) {
    views.forEach {
        it?.toggleView(state)
    }
}

/**
 * Calling [toggleVisibilityState] for array of [View]
 * @param views array of views
 */
fun toggleViewsVisibilityState(state: Boolean, vararg views: View?,
                               defaultTrueState: Int = View.VISIBLE,
                               defaultFalseState: Int = View.GONE) {
    views.forEach {
        it?.toggleVisibilityState(state, defaultTrueState, defaultFalseState)
    }
}
/**
 * Calling [View.setEnabled] for array of [View]
 * @param views array of views
 */
fun enableViews(isEnable: Boolean, vararg views: View?) {
    views.forEach {
        it?.isEnabled = isEnable
    }
}
