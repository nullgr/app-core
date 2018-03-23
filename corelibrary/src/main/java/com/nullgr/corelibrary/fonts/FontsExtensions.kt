package com.nullgr.corelibrary.fonts

import android.content.Context
import android.graphics.Typeface
import android.support.v7.app.ActionBar
import android.text.Spannable
import android.text.SpannableString
import android.util.AttributeSet
import android.util.Log
import android.util.LruCache
import android.widget.TextView
import com.nullgr.corelibrary.R
import java.lang.Exception

/**
 * Init and provide custom fonts
 */
fun init(textView: TextView, context: Context, attrs: AttributeSet?) {
    val ta = context.obtainStyledAttributes(attrs, R.styleable.TextView, 0, 0)
    try {
        val fontPath = ta.getString(R.styleable.TextView_fontPath)
        if (fontPath != null && fontPath.isNotEmpty()) {
            val typeface = getTypeface(context, fontPath)
            textView.typeface = typeface
        }
    } catch (ignored: Exception) {
        Log.d("FontsExtensions", "Error during init font to TextView: $textView")
    } finally {
        ta.recycle()
    }
}

fun getTypeface(context: Context, fontFullName: String): Typeface? {
    var typeface: Typeface? = FontsCache[fontFullName]
    try {
        typeface = Typeface.createFromAsset(context.assets, fontFullName)
        FontsCache[fontFullName] = typeface
    } catch (ignored: Exception) {
        Log.d("FontsExtensions", "Error during load font $fontFullName")
    }

    return typeface
}

fun String?.applyFont(context: Context?, fontName: String): CharSequence? {
    if (context == null) return this
    if (this.isNullOrEmpty()) return this

    getTypeface(context, fontName)?.let {
        val spannableString = SpannableString(this)
        spannableString.setSpan(TypefaceSpan(context, it), 0, spannableString.length, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)
        return spannableString
    }

    return this
}

fun ActionBar.setSpannableTitle(context: Context?, title: String, fontName: String) {
    this.title = title.applyFont(context, fontName)
}

object FontsCache {
    private val lruCache = LruCache<String, Typeface>(8)

    operator fun get(name: String): Typeface {
        return lruCache[name]
    }

    operator fun set(name: String, typeface: Typeface) {
        lruCache.put(name, typeface)
    }
}