package com.nullgr.core.ui.utils

import android.text.Editable
import android.text.TextWatcher

/**
 * Shorter version of [TextWatcher] for those cases when needed only [TextWatcher.onTextChanged]
 *
 * @author Grishko Nikita
 */
abstract class SimpleTextWatcher : TextWatcher {

    override fun afterTextChanged(s: Editable?) {
    }

    override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
    }
}