package com.nullgr.core.ui.widgets

import android.content.Context
import android.support.v7.widget.AppCompatButton
import android.util.AttributeSet
import com.nullgr.core.ui.widgets.extensions.initTypeface

/**
 * Button - version of [AppCompatButton] which can use custom typeface.
 * Typeface can be set by attributes
 * @attr ref com.nullgr.corelibrary.R.styleable#TextView_fontPath
 */
class Button : AppCompatButton {

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