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
    protected open val onClickListener: ClickListener? = null
) {
    protected open val layoutResId: Int = R.layout.explanatory_note

    open val themeResId: Int = R.style.Theme_Transparent

    open fun init(parent: Overlay) {
        LayoutInflater.from(parent.context).inflate(layoutResId, parent)

        parent.findViewById<TextView>(R.id.text_info)
            ?.setText(info ?: parent.context.getString(R.string.highlighter_info))
            ?: log("Info textView not found")

        parent.findViewById<View>(R.id.button_close)?.setOnClickListener {
            parent.onClose()
            log("ClickResult ${ClickResult.closeButton}")
            onClickListener?.invoke(ClickResult.closeButton)
        }
            ?: log("Close button not found")

        parent.setOnClickListener(onClickListener)
    }

    override fun toString(): String {
        return "ExplanatoryNote(info=$info, layoutResId=$layoutResId, themeResId=$themeResId)"
    }
}