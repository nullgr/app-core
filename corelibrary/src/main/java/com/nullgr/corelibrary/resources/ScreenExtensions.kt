package com.nullgr.corelibrary.resources

import android.content.Context
import android.content.res.Resources
import android.graphics.Point
import android.view.WindowManager

/**
 * Created by Grishko Nikita on 01.02.18.
 */
fun px2dp(resources: Resources, px: Float): Float {
    return px / resources.displayMetrics.density
}

fun dp2px(resources: Resources, dp: Float): Float {
    return dp * resources.displayMetrics.density
}

fun spToPx(resources: Resources, sp: Float): Float {
    val scale = resources.displayMetrics.scaledDensity
    return sp * scale
}

fun getDisplaySize(context: Context): Pair<Int, Int> {
    val windowManager = context.getSystemService(Context.WINDOW_SERVICE) as WindowManager
    val size = Point()
    windowManager.defaultDisplay.getSize(size)
    return Pair(size.x, size.y)
}

fun ResourceProvider.px2dp(px: Float): Float {
    return px / getDensity()
}

fun ResourceProvider.dp2px(dp: Float): Float {
    return dp * getDensity()
}
