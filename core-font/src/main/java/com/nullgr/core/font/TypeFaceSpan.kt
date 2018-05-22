package com.nullgr.core.font

import android.graphics.Paint
import android.graphics.Typeface
import android.text.TextPaint
import android.text.style.MetricAffectingSpan

/**
 * Span class to set custom [Typeface] to string
 * @property typeface instance of [Typeface]
 */
class TypefaceSpan(private val typeface: Typeface?) : MetricAffectingSpan() {

    override fun updateMeasureState(p: TextPaint) {
        p.typeface = typeface

        // Note: This flag is required for proper typeface rendering
        p.flags = p.flags or Paint.SUBPIXEL_TEXT_FLAG
    }

    override fun updateDrawState(tp: TextPaint) {
        tp.typeface = typeface

        // Note: This flag is required for proper typeface rendering
        tp.flags = tp.flags or Paint.SUBPIXEL_TEXT_FLAG
    }
}