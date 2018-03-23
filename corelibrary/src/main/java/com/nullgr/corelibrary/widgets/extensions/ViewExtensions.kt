package com.nullgr.corelibrary.widgets.extensions

import android.view.View
import android.view.ViewGroup
import android.view.ViewTreeObserver

/**
 * Created by Grishko Nikita on 01.02.18.
 */
fun <T : View> T.show() {
    this.visibility = View.VISIBLE
}

fun <T : View> T.hide() {
    this.visibility = View.GONE
}

fun <T : View> T.disappear() {
    this.visibility = View.INVISIBLE
}

fun <T : View> T.toggleView(show: Boolean) {
    if (show) this.show()
    else this.hide()
}

fun <T : View> T.toggleVisibilityState(state: Boolean,
                                       defaultTrueState: Int = View.VISIBLE,
                                       defaultFalseState: Int = View.GONE) {
    visibility = if (state) defaultTrueState else defaultFalseState
}

fun View.toggleSelected() {
    isSelected = !isSelected
}

fun ViewGroup.alphaChilds(alpha: Float) {
    if (childCount > 0) {
        for (i in 0..childCount) {
            getChildAt(i)?.alpha = alpha
        }
    }
}

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

fun hideViews(vararg views: View?) {
    views.forEach {
        it?.hide()
    }
}

fun showViews(vararg views: View?) {
    views.forEach {
        it?.show()
    }
}

fun disappearViews(vararg views: View?) {
    views.forEach {
        it?.show()
    }
}

fun toggleViews(state: Boolean, vararg views: View?) {
    views.forEach {
        it?.toggleView(state)
    }
}

fun toggleViewsVisibilityState(state: Boolean, vararg views: View?,
                               defaultTrueState: Int = View.VISIBLE,
                               defaultFalseState: Int = View.GONE) {
    views.forEach {
        it?.toggleVisibilityState(state, defaultTrueState, defaultFalseState)
    }
}

fun enableViews(isEnable: Boolean, vararg views: View?) {
    views.forEach {
        it?.isEnabled = isEnable
    }
}
