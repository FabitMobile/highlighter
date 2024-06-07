package ru.fabit.highlighter.internal

import android.util.Log
import ru.fabit.highlighter.Highlighter

private var _DEBUG = true

var Highlighter.Companion.DEBUG
    get() = _DEBUG
    set(value) {
        _DEBUG = value
    }

internal fun log(text: String) {
    if (Highlighter.DEBUG)
        Log.d("Highlighter", text)
}