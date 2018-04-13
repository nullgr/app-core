package com.nullgr.corelibrary.resources

import android.content.Context
import android.content.res.Resources
import android.graphics.Point
import android.view.WindowManager

/**
 * Convert physical pixels to dpi
 * @receiver [Float] value of physical pixels
 * @param resources instance of [Resources]
 * @return [Float] value in dpi
 */
fun Float.pxTodp(resources: Resources): Float {
    return this / resources.displayMetrics.density
}

/**
 * Convert dpi to physical pixels
 * @receiver [Float] value of dpi
 * @param resources instance of [Resources]
 * @return [Float] value in physical pixels
 */
fun Float.dpTopx(resources: Resources): Float {
    return this * resources.displayMetrics.density
}

/**
 * Convert physical pixels to dpi
 * @receiver [Float] value of physical pixels
 * @param context instance of [Context]
 * @return [Float] value in dpi
 */
fun Float.pxTodp(context: Context): Float {
    return this / context.resources.displayMetrics.density
}

/**
 * Convert dpi to physical pixels
 * @receiver [Float] value of dpi
 * @param context instance of [Context]
 * @return [Float] value in physical pixels
 */
fun Float.dpTopx(context: Context): Float {
    return this * context.resources.displayMetrics.density
}

/**
 * Convert sp to physical pixels
 * @receiver [Float] value of sp
 * @param resources instance of [Resources]
 * @return [Float] value in physical pixels
 */
fun Float.spToPx(resources: Resources): Float {
    val scale = resources.displayMetrics.scaledDensity
    return this * scale
}

/**
 * Convert sp to physical pixels
 * @receiver [Float] value of sp
 * @param context instance of [Context]
 * @return [Float] value in physical pixels
 */
fun Float.spToPx(context: Context): Float {
    val scale = context.resources.displayMetrics.scaledDensity
    return this * scale
}

/**
 * Gets the size of the display, in pixels.
 * @param context instance of [Context]
 * @return [Pair] object where [Pair.first] is **widthPixels** and [Pair.second] is **heightPixels**
 * @see [android.view.Display.getSize]
 */
fun getDisplaySize(context: Context): Pair<Int, Int> {
    val windowManager = context.getSystemService(Context.WINDOW_SERVICE) as WindowManager
    val size = Point()
    windowManager.defaultDisplay.getSize(size)
    return size.x to size.y
}
