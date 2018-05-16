package com.nullgr.corelibrary.ui.toast

import android.content.Context
import android.widget.Toast

/**
 * Make simple text [Toast] from [String] with [Toast.LENGTH_SHORT] and show it
 * @param context [Context]
 * @receiver [String] message for Toast
 */
fun String?.showToast(context: Context?) {
    if (this != null && context != null)
        makeTextToast(context, this, Toast.LENGTH_SHORT).show()
}

/**
 * Make simple text [Toast] from [String] with [Toast.LENGTH_SHORT] and show it
 * @param message message for Toast
 * @receiver [Context]
 */
fun Context?.showToast(message: String?) {
    if (this != null && message != null)
        makeTextToast(this, message, Toast.LENGTH_SHORT).show()
}

/**
 * Make simple text [Toast] from [String] with [Toast.LENGTH_LONG] and show it
 * @param message message for Toast
 * @receiver [Context]
 */
fun Context?.showLongToast(message: String?) {
    if (this != null && message != null)
        makeTextToast(this, message, Toast.LENGTH_LONG).show()
}

/**
 * Make simple text [Toast] from [String] with [Toast.LENGTH_LONG] and show it
 * @param context [Context]
 * @receiver [String] message for Toast
 */
fun String?.showLongToast(context: Context?) {
    if (this != null && context != null)
        makeTextToast(context, this, Toast.LENGTH_LONG).show()
}

private fun makeTextToast(context: Context, message: String, length: Int): Toast =
        Toast.makeText(context, message, length)
