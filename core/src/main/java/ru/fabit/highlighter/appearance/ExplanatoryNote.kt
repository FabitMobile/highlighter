package ru.fabit.highlighter.appearance

import android.view.LayoutInflater
import android.view.View
import android.widget.TextView
import ru.fabit.highlighter.ClickListener
import ru.fabit.highlighter.ClickResult
import ru.fabit.highlighter.R
import ru.fabit.highlighter.internal.log

open class ExplanatoryNote(
    protected open val info: String? = null,
    open val onClickListener: ClickListener? = null
) {
    protected open val layoutResId: Int = R.layout.explanatory_note

    open val themeResId: Int? = null

    open val animateAlpha: Boolean = true

    open fun init(parent: Overlay) {
        LayoutInflater.from(parent.context).inflate(layoutResId, parent)

        val buttonClose: View? = parent.findViewById(R.id.highlighter_button_close)
        val textInfo: TextView? = parent.findViewById(R.id.highlighter_text_info)

        textInfo?.setText(info ?: parent.context.getString(R.string.highlighter_info))
            ?: log("Info textView not found")

        buttonClose?.setOnClickListener {
            parent.onClose()
            log("ClickResult ${ClickResult.closeButton}")
            onClickListener?.invoke(ClickResult.closeButton)
        }
            ?: log("Close button not found")

        Views(parent, textInfo, buttonClose).init()

        parent.setOnClickListener(onClickListener)
    }

    protected open fun Views.init() {}

    override fun toString(): String {
        return "ExplanatoryNote(info=$info, layoutResId=$layoutResId, themeResId=$themeResId)"
    }

    class Views(val parent: Overlay, val textInfo: TextView?, val buttonClose: View?)
}