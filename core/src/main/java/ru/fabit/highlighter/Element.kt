package ru.fabit.highlighter

import android.content.Context
import android.graphics.Rect
import android.view.View
import androidx.cardview.widget.CardView

data class Element(
    val context: Context,
    val position: Rect,
    val cornerRadius: Float = 0f
)

fun View.toElement(cornerRadiusDp: Float? = null): Element {
    val pos = IntArray(2)
    getLocationOnScreen(pos)
    val w = measuredWidth
    val h = measuredHeight

    val radius = when (this) {
        is CardView -> this.radius
        else -> null
    }

    return Element(
        context,
        Rect(pos[0], pos[1], pos[0] + w, pos[1] + h),
        cornerRadiusDp ?: radius ?: 0f
    )
}