package com.nullgr.core.ui.inputfilters

import android.text.InputFilter
import android.text.Spanned
import android.widget.EditText

/**
 * [InputFilter] for [EditText] which skips only allowed characters.
 * Allowed characters is any character that is not specified in the [validationString]
 *
 * @author Grishko Nikita
 */
class AllowedCharactersInputFilter(private val validationString: String) : InputFilter {
    override fun filter(source: CharSequence?, start: Int, end: Int, dest: Spanned?, dstart: Int, dend: Int): CharSequence? {
        if (source != null && end != 0 && !validationString.contains(source[end - 1])) {
            return source.subSequence(start, end - 1)
        }
        return null
    }
}