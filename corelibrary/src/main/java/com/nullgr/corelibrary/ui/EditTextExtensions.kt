package com.nullgr.corelibrary.ui

import android.text.InputFilter
import android.widget.EditText

/**
 * Created by Grishko Nikita on 01.02.18.
 */
fun EditText.applyFilterAllowedDigits(validationString: String) {
    val filter = InputFilter { inputString, start, end, dest, dstart, dend ->
        for (i in start until end) {
            if (!validationString.contains(inputString[i])) return@InputFilter ""
        }
        null
    }
    editableText.filters = arrayOf(filter)
}

fun EditText.applyFilterOnlyDigits() {
    val filter = InputFilter { source, start, end, dest, dstart, dend ->
        if (end != 0 && !Character.isDigit(source[end - 1])) {
            return@InputFilter source.subSequence(start, end - 1)
        }
        null
    }
    editableText.filters = arrayOf(filter)
}

fun EditText.applyFilterOnlyLetersOrDigits() {
    val filter = InputFilter { source, start, end, dest, dstart, dend ->
        if (end != 0 && !Character.isLetterOrDigit(source[end - 1])) {
            return@InputFilter source.subSequence(start, end - 1)
        }
        null
    }
    editableText.filters = arrayOf(filter)
}

fun EditText.applyLengthFilter(length: Int) {
    editableText.filters = arrayOf(InputFilter.LengthFilter(length))
}

fun EditText.applyAllCapsFilter() {
    editableText.filters = arrayOf(InputFilter.AllCaps())
}