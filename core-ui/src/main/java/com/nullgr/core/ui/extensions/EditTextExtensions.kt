package com.nullgr.core.ui.extensions

import android.text.Editable
import android.text.InputFilter
import android.text.TextWatcher
import android.text.method.PasswordTransformationMethod
import android.widget.EditText
import com.nullgr.core.ui.inputfilters.AllowedCharactersInputFilter
import com.nullgr.core.ui.inputfilters.OnlyDigitsInputFilter
import com.nullgr.core.ui.inputfilters.OnlyLettersOrDigitsInputFilter

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
 * Add [TextWatcher] for edit text and provides result of [TextWatcher.onTextChanged]
 * through the given [onTextChanged] function.
 * ***Note*** that each calling of this method would create and add new instance of [TextWatcher]
 *
 * @return created [TextWatcher]
 */
fun EditText.doOnTextChanged(
    onTextChanged: (text: CharSequence?, start: Int, before: Int, count: Int) -> Unit
) = addTextChangedListener(onTextChanged = onTextChanged)

/**
 * Add [TextWatcher] for edit text and provides result of [TextWatcher.beforeTextChanged]
 * through the given [beforeTextChanged] function.
 * ***Note*** that each calling of this method would create and add new instance of [TextWatcher]
 *
 * @return created [TextWatcher]
 */
fun EditText.doBeforeTextChanged(
    beforeTextChanged: (s: CharSequence?, start: Int, count: Int, after: Int) -> Unit
) = addTextChangedListener(beforeTextChanged = beforeTextChanged)

/**
 * Add [TextWatcher] for edit text and provides result of [TextWatcher.afterTextChanged]
 * through the given [afterTextChanged] function.
 * ***Note*** that each calling of this method would create and add new instance of [TextWatcher]
 *
 * @return created [TextWatcher]
 */
fun EditText.doAfterTextChanged(
    afterTextChanged: (editable: Editable?) -> Unit
) = addTextChangedListener(afterTextChanged = afterTextChanged)

/**
 * Add [TextWatcher] for edit text and provides result of [TextWatcher.beforeTextChanged], [TextWatcher.afterTextChanged],
 * [TextWatcher.onTextChanged] through the given functions.
 * ***Note*** that each calling of this method would create and add new instance of [TextWatcher]
 *
 * @return created [TextWatcher]
 */
fun EditText.addTextChangedListener(
    onTextChanged: ((text: CharSequence?, start: Int, before: Int, count: Int) -> Unit)? = null,
    afterTextChanged: ((editable: Editable?) -> Unit)? = null,
    beforeTextChanged: ((s: CharSequence?, start: Int, count: Int, after: Int) -> Unit)? = null
): TextWatcher {
    val watcher = object : TextWatcher {
        override fun afterTextChanged(s: Editable?) {
            afterTextChanged?.invoke(s)
        }

        override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            beforeTextChanged?.invoke(s, start, count, after)
        }

        override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
            onTextChanged?.invoke(s, start, before, count)
        }
    }
    addTextChangedListener(watcher)
    return watcher
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