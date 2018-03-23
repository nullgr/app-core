package com.nullgr.corelibrary.widgets

import android.content.Context
import android.support.v7.widget.SwitchCompat
import android.util.AttributeSet
import com.nullgr.corelibrary.fonts.init

/**
 * Button which can use custom font
 */
class Switch : SwitchCompat {

    constructor(context: Context) : super(context) {
        if (!isInEditMode) {
            init(this, context, null)
        }
    }

    constructor(context: Context, attrs: AttributeSet) : super(context, attrs) {
        if (!isInEditMode) {
            init(this, context, attrs)
        }
    }

    constructor(context: Context, attrs: AttributeSet, defStyleAttr: Int) : super(context, attrs, defStyleAttr) {
        if (!isInEditMode) {
            init(this, context, attrs)
        }
    }
}