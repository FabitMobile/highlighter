package ru.fabit.highlighter

@JvmInline
value class ClickResult private constructor(val value: Int) {
    companion object {
        val highlightedElement = ClickResult(0)
        val closeButton = ClickResult(1)
        val overlay = ClickResult(-1)
        val cancel = ClickResult(-2)
    }

    override fun toString() = when (value) {
        highlightedElement.value -> "highlightedElement"
        closeButton.value -> "closeButton"
        overlay.value -> "overlay"
        cancel.value -> "cancel"
        else -> super.toString()
    }
}

typealias ClickListener = (ClickResult) -> Unit