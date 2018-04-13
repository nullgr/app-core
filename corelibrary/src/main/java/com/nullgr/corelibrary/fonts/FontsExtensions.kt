package com.nullgr.corelibrary.fonts

import android.content.Context
import android.graphics.Typeface
import android.support.v7.app.ActionBar
import android.util.Log
import android.widget.Toolbar
import java.lang.Exception

/**
 *Provide [Typeface] object, created from ***assets*** by using full path to font in assets.
 *Simple usage:
 *```
 * val typeface = context.getTypeface("font/RobotoBold.otf")
 *```
 *@receiver [Context]
 *@param fontFullName full path to font file in assets directory.
 *@return created [Typeface] or ***null*** if something went wrong while typeface was creating
 */
fun Context.getTypeface(fontFullName: String): Typeface? {
    try {
        return Typeface.createFromAsset(this.assets, fontFullName)
    } catch (ignored: Exception) {
        Log.e("FontsExtensions", "Error during load font $fontFullName")
    }
    return null
}

/** Set [ActionBar]'s title with custom typeface
 * @receiver [ActionBar]
 * @param context [Context]
 * @param title [String] which will be set as title
 * @param fontFullName - full path to font file in assets directory.
 */
fun ActionBar.setSpannableTitle(context: Context?, title: String, fontFullName: String) {
    this.title = title.applyFont(context, fontFullName)
}

/** Set [Toolbar]'s title with custom typeface
 * @receiver [Toolbar]
 * @param context [Context]
 * @param title [String] which will be set as title
 * @param fontFullName - full path to font file in assets directory.
 */
fun Toolbar.setSpannableTitle(context: Context?, title: String, fontFullName: String) {
    this.title = title.applyFont(context, fontFullName)
}
