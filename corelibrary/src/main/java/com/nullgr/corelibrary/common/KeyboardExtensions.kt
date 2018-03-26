package com.nullgr.corelibrary.common

import android.app.Activity
import android.content.Context
import android.view.View
import android.view.inputmethod.InputMethodManager

/**
 * Created by Grishko Nikita on 01.02.18.
 */
fun Activity?.hideKeyboard() {
    this?.let {
        val inputManager = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager?
        if (currentFocus != null) {
            inputManager?.hideSoftInputFromWindow(currentFocus.windowToken, InputMethodManager.HIDE_NOT_ALWAYS)
        }
    }
}

fun View?.hideKeyboard() {
    this?.let {
        val inputManager = context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager?
        inputManager?.hideSoftInputFromWindow(windowToken, InputMethodManager.HIDE_NOT_ALWAYS)
    }
}

fun Activity?.showKeyboard() {
    this?.let {
        val inputManager = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager?
        if (currentFocus != null) {
            inputManager?.showSoftInputFromInputMethod(currentFocus!!.windowToken, InputMethodManager.SHOW_IMPLICIT)
        }
    }
}

fun View?.showKeyboard() {
    this?.let {
        val inputManager = context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager?
        inputManager?.showSoftInput(it, InputMethodManager.SHOW_IMPLICIT)
    }
}