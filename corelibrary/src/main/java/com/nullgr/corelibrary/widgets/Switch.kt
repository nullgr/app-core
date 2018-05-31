package com.nullgr.corelibrary.widgets

import android.content.Context
import android.support.v7.widget.SwitchCompat
import android.util.AttributeSet
import com.nullgr.corelibrary.widgets.extensions.initTypeface

/**
 * Switch - version of [SwitchCompat] which can use custom typeface.
 * Typeface can be set by attributes
 * @attr ref com.nullgr.corelibrary.R.styleable#TextView_fontPath
 */
class Switch : SwitchCompat {

    constructor(context: Context) : super(context) {
        if (!isInEditMode) {
            initTypeface(this, context, null)
        }
    }

    constructor(context: Context, attrs: AttributeSet) : super(context, attrs) {
        if (!isInEditMode) {
            initTypeface(this, context, attrs)
        }
    }

    constructor(context: Context, attrs: AttributeSet, defStyleAttr: Int) : super(context, attrs, defStyleAttr) {
        if (!isInEditMode) {
            initTypeface(this, context, attrs)
        }
    }
}