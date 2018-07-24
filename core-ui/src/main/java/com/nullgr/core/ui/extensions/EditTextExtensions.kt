package com.nullgr.core.ui.extensions

import android.text.InputFilter
import android.text.method.PasswordTransformationMethod
import android.widget.EditText
import com.nullgr.core.ui.inputfilters.AllowedCharactersInputFilter
import com.nullgr.core.ui.inputfilters.OnlyDigitsInputFilter
import com.nullgr.core.ui.inputfilters.OnlyLettersOrDigitsInputFilter
import com.nullgr.core.ui.utils.SimpleTextWatcher

/**
 * Set [InputFilter] to [EditText] which skips only allowed characters.
 * Allowed characters is any character that is not specified in the [validationString]
 */
fun EditText.applyFilterAllowedDigits(validationString: String) {
    editableText.filters = arrayOf(AllowedCharactersInputFilter(validationString))
}

/**
 * Set [InputFilter] to [EditText] which skips only digits.
 */
fun EditText.applyFilterOnlyDigits() {
    editableText.filters = arrayOf(OnlyDigitsInputFilter())
}

/**
 * Set [InputFilter] to [EditText] which allows only digits and letters.
 */
fun EditText.applyFilterOnlyLetersOrDigits() {
    editableText.filters = arrayOf(OnlyLettersOrDigitsInputFilter())
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

/**
 * Extension that provide ability to add few input filters inside one function
 */
fun EditText.applyFilters(bindFunction: ArrayList<InputFilter>.() -> Unit) {
    editableText.filters = arrayListOf<InputFilter>().apply {
        bindFunction()
    }.toTypedArray()
}

/**
 * Sets [SimpleTextWatcher] and provides its result throw the given [function]
 * Note that each calling of this method would create and add new instance of [SimpleTextWatcher]
 */
inline fun EditText.listenTextChanges(crossinline function: (text: CharSequence?) -> Unit) {
    this.addTextChangedListener(object : SimpleTextWatcher() {
        override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
            function(s)
        }
    })
}

/**
 * Sets [PasswordTransformationMethod] to [EditText.setTransformationMethod]
 */
fun EditText.makeSecure() {
    this.transformationMethod = PasswordTransformationMethod.getInstance()
}

/**
 * Put value to [EditText.setTransformationMethod] -
 * if it was ***null*** then [PasswordTransformationMethod] or ***null*** in other case
 */
fun EditText.toggleSecure() {
    if (this.transformationMethod == null) transformationMethod = PasswordTransformationMethod.getInstance()
    else this.transformationMethod = null
}

private val noFilters: Array<InputFilter?>  by lazy { arrayOfNulls<InputFilter>(0) }