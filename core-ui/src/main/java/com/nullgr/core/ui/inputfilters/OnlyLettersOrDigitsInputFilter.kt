package com.nullgr.core.ui.inputfilters

import android.text.InputFilter
import android.text.Spanned
import android.widget.EditText

/**
 * [InputFilter] to [EditText] which allows only digits and letters.
 *
 * @author Grishko Nikita
 */
class OnlyLettersOrDigitsInputFilter : InputFilter {
    override fun filter(source: CharSequence?, start: Int, end: Int, dest: Spanned?, dstart: Int, dend: Int): CharSequence? {
        if (source != null && end != 0 && !Character.isLetterOrDigit(source[end - 1])) {
            return source.subSequence(start, end - 1)
        }
        return null
    }
}