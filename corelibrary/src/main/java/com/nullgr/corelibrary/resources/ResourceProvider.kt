package com.nullgr.corelibrary.resources

import android.content.Context
import android.graphics.drawable.Drawable
import android.support.annotation.*
import android.support.v4.content.ContextCompat

/**
 * Resource provider serves to provide any resource from context.
 * Best practice is to use singleton instance provided with DI
 *
 * @property context [Context]
 * @author il_mov.
 */
class ResourceProvider(private val context: Context) {

    /**
     * @see [android.content.res.Resources.getString]
     */
    fun getString(@StringRes resId: Int): String {
        return context.resources.getString(resId)
    }

    /**
     * @see [android.content.res.Resources.getString]
     */
    fun getString(@StringRes resId: Int, vararg formatArgs: Any): String {
        return context.resources.getString(resId, *formatArgs)
    }

    /**
     * @see [android.content.res.Resources.getStringArray]
     */
    fun getStringArray(@ArrayRes resId: Int): Array<String> {
        return context.resources.getStringArray(resId)
    }

    /**
     * @see [android.content.res.Resources.getQuantityString]
     */
    fun getQuantityString(@PluralsRes resId: Int, count: Int): String {
        return context.resources.getQuantityString(resId, count)
    }

    /**
     * @see [android.content.res.Resources.getQuantityString]
     */
    fun getQuantityString(@PluralsRes resId: Int, count: Int, vararg formatArgs: Any): String {
        return context.resources.getQuantityString(resId, count, *formatArgs)
    }

    /**
     * @see [ContextCompat.getColor]
     */
    fun getColor(@ColorRes resId: Int): Int {
        return ContextCompat.getColor(context, resId)
    }

    /**
     * @see [ContextCompat.getDrawable]
     */
    fun getDrawable(@DrawableRes resId: Int): Drawable? {
        return ContextCompat.getDrawable(context, resId)
    }

    /**
     * Returns an [Int] identifier of [Drawable] res by it [String] name
     * Example:
     * ```
     * val resId = resourceProvider.getDrawableId("ic_launcher")
     * ```
     * @param name [String] name identifier of drawable res.
     * @return [Int] identifier of [Drawable] or **0** if something went wrong
     */
    fun getDrawableId(name: String?): Int {
        return if (name != null && name.isNotEmpty())
            context.resources.getIdentifier(name, "drawable", context.packageName)
        else 0
    }

    /**
     * @see [android.content.res.Resources.getDimensionPixelSize]
     */
    fun getPxSize(@DimenRes resId: Int): Int {
        return context.resources.getDimensionPixelSize(resId)
    }

    /**
     * @see [android.content.res.Resources.getBoolean]
     */
    fun getBoolean(@BoolRes resId: Int): Boolean {
        return context.resources.getBoolean(resId)
    }

    /**
     * @see [android.content.res.Resources.getInteger]
     */
    fun getInt(@IntegerRes resId: Int): Int {
        return context.resources.getInteger(resId)
    }

    /**
     * @see [android.content.res.Resources.getIntArray]
     */
    fun getIntArray(@ArrayRes resId: Int): IntArray {
        return context.resources.getIntArray(resId)
    }

    /**
     * Returns screen's density.
     * @return [Float] density
     * @see [android.util.DisplayMetrics.density]
     * */
    fun getDensity(): Float {
        return context.resources.displayMetrics.density
    }
}