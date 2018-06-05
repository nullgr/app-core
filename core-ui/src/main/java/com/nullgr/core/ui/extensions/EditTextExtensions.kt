package com.nullgr.core.ui.extensions

import android.text.InputFilter
import android.widget.EditText

/**
 * Set [InputFilter] to [EditText] which skips only allowed characters.
 * Allowed characters is any character that is not specified in the [validationString]
 */
fun EditText.applyFilterAllowedDigits(validationString: String) {
    val filter = InputFilter { inputString, start, end, _, _, _ ->
        for (i in start until end) {
            if (!validationString.contains(inputString[i])) return@InputFilter ""
        }
        null
    }
    editableText.filters = arrayOf(filter)
}

/**
 * Set [InputFilter] to [EditText] which skips only digits.
 */
fun EditText.applyFilterOnlyDigits() {
    val filter = InputFilter { source, start, end, _, _, _ ->
        if (end != 0 && !Character.isDigit(source[end - 1])) {
            return@InputFilter source.subSequence(start, end - 1)
        }
        null
    }
    editableText.filters = arrayOf(filter)
}

/**
 * Set [InputFilter] to [EditText] which allows only digits and letters.
 */
fun EditText.applyFilterOnlyLetersOrDigits() {
    val filter = InputFilter { source, start, end, _, _, _ ->
        if (end != 0 && !Character.isLetterOrDigit(source[end - 1])) {
            return@InputFilter source.subSequence(start, end - 1)
        }
        null
    }
    editableText.filters = arrayOf(filter)
}

/**
 * Sets [InputFilter] to [EditText] which will constrain edits not to make the length of the text
 * greater than the specified length.
 * @param length max length of the text
 * @see [InputFilter.LengthFilter]
 */
fun EditText.applyLengthFilter(length: Int) {
    editableText.filters = arrayOf(InputFilter.LengthFilter(length))
}

/**
 * Sets [InputFilter] to [EditText] which will capitalize all the lowercase and titlecase letters that are added
 * through edits
 * @see [InputFilter.AllCaps]
 */
fun EditText.applyAllCapsFilter() {
    editableText.filters = arrayOf(InputFilter.AllCaps())
}

/**
 * Removes all previously set [InputFilter]
 */
fun EditText.clearInputFilters() {
    filters = noFilters
}

private val noFilters: Array<InputFilter?>  by lazy { arrayOfNulls<InputFilter>(0) }