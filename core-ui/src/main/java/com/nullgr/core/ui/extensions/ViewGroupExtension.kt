package com.nullgr.core.ui.extensions

import android.view.View
import android.view.ViewGroup

/**
 *  Extension function creates the [Iterator] to walk through the all children of a [ViewGroup],
 *  allowing to use it in `for` loops.
 *  For example:
 *  for (child in container.children()) { ... }
 *  or
 *  container.children().forEach{ ... }
 */
fun ViewGroup.children() = object : Iterator<View> {
    private var current = 0
    private val count = childCount

    override fun hasNext() = current < count

    override fun next(): View = getChildAt(current++)
}
