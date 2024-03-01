package ru.fabit.highlighter

@JvmInline
value class ClickResult private constructor(val value: Int) {
    companion object {
        val highlightedElement = ClickResult(0)
        val closeButton = ClickResult(1)
        val overlay = ClickResult(-1)
    }
}

typealias ClickListener = (ClickResult) -> Unit