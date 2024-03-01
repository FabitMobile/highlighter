package ru.fabit.highlighter.internal

import android.util.Log
import ru.fabit.highlighter.Highlighter

internal fun log(text: String) {
    if (Highlighter.DEBUG)
        Log.d("Highlighter", text)
}