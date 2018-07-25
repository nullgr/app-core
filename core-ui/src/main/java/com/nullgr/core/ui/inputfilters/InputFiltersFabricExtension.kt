package com.nullgr.core.ui.inputfilters

import android.text.InputFilter

/**
 * Factory function that creates [AllowedCharactersInputFilter]
 */
fun allowedCharactersInputFilter(validationString: String) = AllowedCharactersInputFilter(validationString)

/**
 * Factory function that creates [OnlyDigitsInputFilter]
 */
fun digitsOnlyInputFilter() = OnlyDigitsInputFilter()

/**
 * Factory function that creates [OnlyLettersOrDigitsInputFilter]
 */
fun digitsAndLettersInputFilter() = OnlyLettersOrDigitsInputFilter()

/**
 * Factory function that creates [InputFilter.LengthFilter]
 */
fun lengthInputFilter(length: Int) = InputFilter.LengthFilter(length)

/**
 * Factory function that creates [InputFilter.AllCaps]
 */
fun allCapsFilter() = InputFilter.AllCaps()
