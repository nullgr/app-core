package com.nullgr.core.font

import android.content.Context
import android.graphics.Typeface
import android.support.annotation.ColorInt
import android.text.Spannable
import android.text.SpannableString
import android.text.style.AbsoluteSizeSpan
import android.text.style.ForegroundColorSpan
import android.text.style.RelativeSizeSpan

/**
 *This method create [SpannableString] from original [String] by attaching [TypefaceSpan] to it.
 *Can be used for full length or some parts of original string
 *Simple usage
 *```
 * "Some String".applyFont(context, "fonts/RobotoBold.otf")
 *```
 *@receiver [String] object. Can be nullable
 *@param context [Context]
 *@param fontFullName full path to font file in assets directory.
 *@param start start index in string, from which [TypefaceSpan] will be attached. Default value - ***null***.
 * If ***null value*** will be passed - span will starts from zero index.
 *@param end end index in string, until which [TypefaceSpan] will be attached. Default value - ***null***.
 * If ***null value*** will be passed - span's end will be in the end of the string.
 *@return [CharSequence] child. If typeface span will be attached successfully it will be an instance of [SpannableString].
 *If something went wrong, or if [context], [fontFullName] or [this]
 *receiver will have a null value - original [String] will be returned
 */
fun String?.applyFont(context: Context?,
                      fontFullName: String,
                      start: Int? = null,
                      end: Int? = null): CharSequence? {
    if (context == null) return this
    if (this.isNullOrEmpty()) return this

    context.getTypeface(fontFullName)?.let {
        return SpannableString(this).apply {
            setSpan(TypefaceSpan(it), start ?: 0, end ?: length, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)
        }
    }
    return this
}

/**
 *This method create [SpannableString] from original [String] with attaching [RelativeSizeSpan] to it.
 *Can be used for full length or some parts of original string
 *Simple usage
 *```
 * "Some String".applyRelativeSize(0.7f)
 *```
 *@receiver [String] object. Can be nullable
 *@param size [Float] proportion which will be passed to [RelativeSizeSpan]
 *@param start start index in string, from which [RelativeSizeSpan] will be attached. Default value - ***null***.
 * If ***null value*** will be passed - span will starts from zero index.
 *@param end end index in string, until which [RelativeSizeSpan] will be attached. Default value - ***null***.
 * If ***null value*** will be passed - span's end will be in the end of the string.
 *@return [CharSequence] child. If typeface span will be attached successfully it will be an instance of [SpannableString].
 *If something went wrong,  [this] receiver will have a null value - original [String] will be returned
 */
fun String?.applyRelativeSize(size: Float,
                              start: Int? = null,
                              end: Int? = null): CharSequence? {

    if (this.isNullOrEmpty()) return this

    return SpannableString(this).apply {
        setSpan(RelativeSizeSpan(size), start ?: 0, end
                ?: length, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)
    }
}

/**
 *This method create [SpannableString] from original [String] with attaching [AbsoluteSizeSpan] to it.
 *Can be used for full length or some parts of original string
 *Simple usage
 *```
 * "Some String".applyRelativeSize(0.7f)
 *```
 *@receiver [String] object. Can be nullable
 *@param pixelSize [Int] text size in physical pixels, which will be passed to [AbsoluteSizeSpan]
 *@param start start index in string, from which [AbsoluteSizeSpan] will be attached. Default value - ***null***.
 * If ***null value*** will be passed - span will starts from zero index.
 *@param end end index in string, until which [AbsoluteSizeSpan] will be attached. Default value - ***null***.
 * If ***null value*** will be passed - span's end will be in the end of the string.
 *@return [CharSequence] child. If typeface span will be attached successfully it will be an instance of [SpannableString].
 *If something went wrong,  [this] receiver will have a null value - original [String] will be returned
 */
fun String?.applyAbsoluteSize(pixelSize: Int,
                              start: Int? = null,
                              end: Int? = null): CharSequence? {

    if (this.isNullOrEmpty()) return this

    return SpannableString(this).apply {
        setSpan(AbsoluteSizeSpan(pixelSize), start ?: 0, end
                ?: length, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)
    }
}

/**
 *This method create [SpannableString] from original [String] with attaching [ForegroundColorSpan] to it.
 *Can be used for full length or some parts of original string
 *Simple usage
 *```
 * "Some String".applyColor(Color.BLUE)
 *```
 *@receiver [String] object. Can be nullable
 *@param color [Int] color which will be passed to [ForegroundColorSpan]
 *@param start start index in string, from which [ForegroundColorSpan] will be attached. Default value - ***null***.
 * If ***null value*** will be passed - span will starts from zero index.
 *@param end end index in string, until which [ForegroundColorSpan] will be attached. Default value - ***null***.
 * If ***null value*** will be passed - span's end will be in the end of the string.
 *@return [CharSequence] child. If typeface span will be attached successfully it will be an instance of [SpannableString].
 *If something went wrong,  [this] receiver will have a null value - original [String] will be returned
 */
fun String?.applyColor(@ColorInt color: Int,
                       start: Int? = null,
                       end: Int? = null): CharSequence? {

    if (this.isNullOrEmpty()) return this

    return SpannableString(this).apply {
        setSpan(ForegroundColorSpan(color), start ?: 0, end
                ?: length, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)
    }
}

/**
 * Create [SpannableString] from given [CharSequence]
 */
fun CharSequence.toSpannable(): SpannableString {
    return SpannableString(this)
}

/**
 * Create new instance of [SpanSet] for given [CharSequence]
 * @return [SpanSet]
 */
fun CharSequence?.applySpanSet(): SpanSet {
    return SpanSet(this)
}

/**
 * Simple fabric method to create new instance of [TypefaceSpan]
 * @param typeface [Typeface]
 * @return [TypefaceSpan]
 */
fun font(typeface: Typeface?): TypefaceSpan {
    return TypefaceSpan(typeface)
}

/**
 * Simple fabric method to create new instance of [RelativeSizeSpan]
 * @param proportion [Float] proportion which will be passed to [RelativeSizeSpan]
 * @return [RelativeSizeSpan]
 */
fun relativeSize(proportion: Float): RelativeSizeSpan {
    return RelativeSizeSpan(proportion)
}

/**
 * Simple fabric method to create new instance of [AbsoluteSizeSpan]
 * @param pixelSize [Int] text size in physical pixels, which will be passed to [AbsoluteSizeSpan]
 * @return [AbsoluteSizeSpan]
 */
fun absSize(pixelSize: Int): AbsoluteSizeSpan {
    return AbsoluteSizeSpan(pixelSize)
}

/**
 * Simple fabric method to create new instance of [ForegroundColorSpan]
 * @param color [Int] color which will be passed to [ForegroundColorSpan]
 * @return [ForegroundColorSpan]
 */
fun color(color: Int): ForegroundColorSpan {
    return ForegroundColorSpan(color)
}