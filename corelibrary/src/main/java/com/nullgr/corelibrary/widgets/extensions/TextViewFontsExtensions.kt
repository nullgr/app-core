package com.nullgr.corelibrary.widgets.extensions

import android.content.Context
import android.util.AttributeSet
import android.util.Log
import android.widget.TextView
import com.nullgr.core.font.getTypeface
import com.nullgr.corelibrary.R
import java.lang.Exception

/**
 * Provide ability to use custom typeface for [TextView] children
 */
internal fun initTypeface(textView: TextView, context: Context, attrs: AttributeSet?) {
    val ta = context.obtainStyledAttributes(attrs, R.styleable.TextView, 0, 0)
    try {
        val fontPath = ta.getString(R.styleable.TextView_fontPath)
        if (fontPath != null && fontPath.isNotEmpty()) {
            val typeface = context.getTypeface(fontPath)
            textView.typeface = typeface
        }
    } catch (ignored: Exception) {
        Log.e("FontsExtensions", "Error during init font to TextView: $textView")
    } finally {
        ta.recycle()
    }
}