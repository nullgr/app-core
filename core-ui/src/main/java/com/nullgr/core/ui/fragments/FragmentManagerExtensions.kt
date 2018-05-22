package com.nullgr.core.ui.fragments

import android.support.v4.app.DialogFragment
import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager

/**
 * @author il_mov.
 */
fun FragmentManager.addScreen(fragment: Fragment,
                              containerId: Int,
                              tag: String = fragment.javaClass.name,
                              addToBackStack: Boolean = true,
                              enterAnimation: Int = 0,
                              exitAnimation: Int = 0) {
    beginTransaction()
            .also {
                if (enterAnimation != 0 || exitAnimation != 0) {
                    it.setCustomAnimations(enterAnimation, 0, 0, exitAnimation)
                }
            }
            .add(containerId, fragment, tag)
            .also { if (addToBackStack) it.addToBackStack(null) }
            .commitAllowingStateLoss()
}

fun FragmentManager.replaceScreen(fragment: Fragment,
                                  containerId: Int,
                                  tag: String = fragment.javaClass.name,
                                  addToBackStack: Boolean = true,
                                  enterAnimation: Int = 0,
                                  exitAnimation: Int = 0) {
    beginTransaction()
            .also {
                if (enterAnimation != 0 || exitAnimation != 0) {
                    it.setCustomAnimations(enterAnimation, 0, 0, exitAnimation)
                }
            }
            .replace(containerId, fragment, tag)
            .also { if (addToBackStack) it.addToBackStack(null) }
            .commitAllowingStateLoss()
}

fun FragmentManager.removeScreen(fragment: Fragment, exitAnimation: Int = 0) {
    beginTransaction()
            .also { if (exitAnimation != 0) it.setCustomAnimations(0, exitAnimation) }
            .remove(fragment)
            .commitAllowingStateLoss()
}

fun FragmentManager.findCurrentScreen(containerId: Int): Fragment? {
    return if (backStackEntryCount > 0) {
        val entry = getBackStackEntryAt(backStackEntryCount - 1)
        if (entry != null) findFragmentByTag(entry.name)
        else null
    } else {
        findFragmentById(containerId)
    }
}

fun FragmentManager.back() {
    popBackStackImmediate()
}

fun FragmentManager.back(depth: Int) {
    try {
        for (i in 0 until if (depth > backStackEntryCount) backStackEntryCount else depth) {
            back()
        }
    } catch (e: Exception) {
        e.printStackTrace()
    }
}

fun FragmentManager.backToScreen(clazz: Class<*>) {
    var count = 0
    for (entry in backStackEntryCount - 1 downTo 0) {
        if (!clazz.canonicalName.equals(getBackStackEntryAt(entry).name, ignoreCase = true))
            count++
        else
            break
    }
    if (count > 0)
        back(count)
}

fun FragmentManager.clearBackStack() {
    for (i in 0..backStackEntryCount) {
        popBackStackImmediate()
    }
}

inline fun <reified T> FragmentManager.findScreen(): T? {
    return findFragmentByTag(T::class.java.name) as? T
}

fun FragmentManager.showDialog(dialog: DialogFragment,
                               tag: String = dialog.javaClass.name) {
    executePendingTransactions()
    findScreen<DialogFragment>()?.dismissAllowingStateLoss()
    dialog.show(this, tag)
    executePendingTransactions()
}