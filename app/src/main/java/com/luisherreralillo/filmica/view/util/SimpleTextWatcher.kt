package com.luisherreralillo.filmica.view.util

import android.text.Editable
import android.text.TextWatcher

class SimpleTextWatcher(
    val afterTextChangedCallback: ((text: Editable?) -> Unit)? = null,
    val beforeTextChangedCallback: ((text: CharSequence?, start: Int, count: Int, after: Int) -> Unit)? = null,
    val onTextChangedCallback: ((text: CharSequence?, start: Int, before: Int, count: Int) -> Unit)? = null

): TextWatcher {
    override fun afterTextChanged(s: Editable?) {
        afterTextChangedCallback?.invoke(s)
    }

    override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
        beforeTextChangedCallback?.invoke(s, start, count, after)
    }

    override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
        onTextChangedCallback?.invoke(s, start, before, count)
    }
}