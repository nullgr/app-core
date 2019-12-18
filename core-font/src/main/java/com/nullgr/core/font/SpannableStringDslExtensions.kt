package com.nullgr.core.font

import android.graphics.Typeface
import androidx.annotation.ColorInt
import android.text.Spannable
import android.text.SpannableString
import android.text.style.AbsoluteSizeSpan
import android.text.style.BackgroundColorSpan
import android.text.style.ForegroundColorSpan
import android.text.style.RelativeSizeSpan
import android.text.style.StyleSpan

/**
 * Creates [SpannableString] from `this` [CharSequence] and calls the specified function [builderFunction] with
 * value as its argument and returns [SpannableString] as [CharSequence].
 * In general, this function is designed for manipulating with SpannableString in DSL style.
 * Works nicer together with [foregroundColor], [backgroundColor], [relativeSize], [absoluteSize],
 * [typeface] and [style] functions, but in any case you can do anything with [SpannableString] in provided [builderFunction].
 * It's also easy to extend the functionality by adding your own extension functions to the SpannableString
 */
inline fun CharSequence?.withSpan(builderFunction: SpannableString.() -> Unit): CharSequence? {
    this?.let {
        val spannable = SpannableString(it.toSpannable())
        spannable.builderFunction()
        return spannable
    }
    return this
}

/**
 * Creates [ForegroundColorSpanEntry], calls the specified function [colorFunction] with
 * value as its argument, applies [ForegroundColorSpan] and returns [SpannableString] as [CharSequence].
 */
inline fun SpannableString.foregroundColor(colorFunction: ForegroundColorSpanEntry.() -> Unit): SpannableString {
    val spanEntry = ForegroundColorSpanEntry()
    spanEntry.colorFunction()

    if (spanEntry.color == null)
        throw IllegalStateException("")

    return spanEntry.toSpannable(this)
}

/**
 * Creates [BackgroundColorSpanEntry], calls the specified function [colorFunction] with
 * value as its argument, applies [BackgroundColorSpan] and returns [SpannableString] as [CharSequence].
 */
inline fun SpannableString.backgroundColor(colorFunction: BackgroundColorSpanEntry.() -> Unit): SpannableString {
    val spanEntry = BackgroundColorSpanEntry()
    spanEntry.colorFunction()

    if (spanEntry.color == null)
        throw IllegalStateException("")

    return spanEntry.toSpannable(this)
}

/**
 * Creates [AbsoluteSizeSpanEntry], calls the specified function [sizeFunction] with
 * value as its argument, applies [AbsoluteSizeSpan] and returns [SpannableString] as [CharSequence].
 */
inline fun SpannableString.absoluteSize(sizeFunction: AbsoluteSizeSpanEntry.() -> Unit): SpannableString {
    val spanEntry = AbsoluteSizeSpanEntry()
    spanEntry.sizeFunction()

    if (spanEntry.pixelSize == null)
        throw IllegalStateException("")

    return spanEntry.toSpannable(this)
}

/**
 * Creates [RelativeSizeSpanEntry], calls the specified function [sizeFunction] with
 * value as its argument, applies [RelativeSizeSpan] and returns [SpannableString] as [CharSequence].
 */
inline fun SpannableString.relativeSize(sizeFunction: RelativeSizeSpanEntry.() -> Unit): SpannableString {
    val spanEntry = RelativeSizeSpanEntry()
    spanEntry.sizeFunction()

    if (spanEntry.size == null)
        throw IllegalStateException("")

    return spanEntry.toSpannable(this)
}

/**
 * Creates [StyleSpanEntry], calls the specified function [styleFunction] with
 * value as its argument, applies [StyleSpan] and returns [SpannableString] as [CharSequence].
 */
inline fun SpannableString.style(styleFunction: StyleSpanEntry.() -> Unit): SpannableString {
    val spanEntry = StyleSpanEntry()
    spanEntry.styleFunction()

    if (spanEntry.style == null)
        throw IllegalStateException("")

    return spanEntry.toSpannable(this)
}

/**
 * Creates [TypefaceSpanEntry], calls the specified function [typefaceFunction] with
 * value as its argument, applies [TypefaceSpan] and returns [SpannableString] as [CharSequence].
 */
inline fun SpannableString.typeface(typefaceFunction: TypefaceSpanEntry.() -> Unit): SpannableString {
    val spanEntry = TypefaceSpanEntry()
    spanEntry.typefaceFunction()

    if (spanEntry.typeface == null)
        throw IllegalStateException("")

    return spanEntry.toSpannable(this)
}

/**
 * Domain entity designed to work together with [backgroundColor] .
 * Wraps logic of creating and setting [BackgroundColorSpan]
 */
class BackgroundColorSpanEntry : BaseSpanEntry {
    @ColorInt
    var color: Int? = null
    override var from: Int? = null
    override var to: Int? = null
    override var toText: String? = null
    override var flag: Int? = null

    override fun getSpan() = BackgroundColorSpan(color!!)
}

/**
 * Domain entity designed to work together with  [foregroundColor].
 * Wraps logic of creating and setting [ForegroundColorSpan]
 */
class ForegroundColorSpanEntry : BaseSpanEntry {
    @ColorInt
    var color: Int? = null
    override var from: Int? = null
    override var to: Int? = null
    override var toText: String? = null
    override var flag: Int? = null

    override fun getSpan() = ForegroundColorSpan(color!!)
}

/**
 * Domain entity designed to work together with [typeface] function
 * Wraps logic of creating and setting [TypefaceSpan]
 */
class TypefaceSpanEntry : BaseSpanEntry {
    var typeface: Typeface? = null
    override var from: Int? = null
    override var to: Int? = null
    override var toText: String? = null
    override var flag: Int? = null

    override fun getSpan() = TypefaceSpan(typeface!!)
}

/**
 * Domain entity designed to work together with [relativeSize] function
 * Wraps logic of creating and setting [RelativeSizeSpan]
 */
class RelativeSizeSpanEntry : BaseSpanEntry {
    var size: Float? = null
    override var from: Int? = null
    override var to: Int? = null
    override var toText: String? = null
    override var flag: Int? = null

    override fun getSpan() = RelativeSizeSpan(size!!)
}

/**
 * Domain entity designed to work together with [absoluteSize] function
 * Wraps logic of creating and setting [AbsoluteSizeSpan]
 */
class AbsoluteSizeSpanEntry : BaseSpanEntry {
    var pixelSize: Int? = null
    override var from: Int? = null
    override var to: Int? = null
    override var toText: String? = null
    override var flag: Int? = null

    override fun getSpan() = AbsoluteSizeSpan(pixelSize!!)
}

/**
 * Domain entity designed to work together with [style] function
 * Wraps logic of creating and setting [StyleSpan]
 */
class StyleSpanEntry : BaseSpanEntry {
    var style: Int? = null
    override var from: Int? = null
    override var to: Int? = null
    override var toText: String? = null
    override var flag: Int? = null

    override fun getSpan() = StyleSpan(this.style!!)
}

interface BaseSpanEntry {
    var from: Int?
    var to: Int?
    val toText: String?
    val flag: Int?

    fun getSpan(): Any

    fun getSpanIndexes(origin: SpannableString): Pair<Int, Int> {
        var startIndex = from ?: 0
        var endIndex = to ?: origin.length

        toText?.let {
            val startIndexOfSubString = origin.indexOf(it)
            if (startIndexOfSubString >= 0) {
                startIndex = startIndexOfSubString
                endIndex = startIndex + it.length
            }
        }
        return startIndex to endIndex
    }

    fun toSpannable(spannableString: SpannableString): SpannableString {
        val spanIndexPair = getSpanIndexes(spannableString)
        spannableString.setSpan(getSpan(), spanIndexPair.first, spanIndexPair.second,
                this.flag ?: Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)
        return spannableString
    }
}