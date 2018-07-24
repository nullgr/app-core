package com.nullgr.core.ui.inputfilters

import android.text.InputFilter
import android.text.Spanned
import android.widget.EditText

/**
 * [InputFilter] for [EditText] which skips only digits.
 *
 * @author Grishko Nikita
 */
class OnlyDigitsInputFilter : InputFilter {
    override fun filter(source: CharSequence?, start: Int, end: Int, dest: Spanned?, dstart: Int, dend: Int): CharSequence? {
        if (source != null && end != 0 && !Character.isDigit(source[end - 1])) {
            return source.subSequence(start, end - 1)
        }
        return null
    }
}