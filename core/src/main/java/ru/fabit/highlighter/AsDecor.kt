package ru.fabit.highlighter

import android.app.Activity
import android.graphics.PixelFormat
import android.view.ViewGroup
import android.view.WindowManager
import androidx.core.view.children
import ru.fabit.highlighter.appearance.Overlay

internal class AsDecor(element: Element) : Highlighter(element) {
    fun bind(root: ViewGroup) {
        val params = WindowManager.LayoutParams(
            WindowManager.LayoutParams.TYPE_APPLICATION_PANEL,
            WindowManager.LayoutParams.FLAG_LAYOUT_IN_SCREEN or WindowManager.LayoutParams.FLAG_FULLSCREEN,
            PixelFormat.RGBA_8888
        )

        val context = element.context.apply {
            note?.themeResId?.let { setTheme(it) }
        }
        val overlay = Overlay(context)
        overlay.highlight(element)
        overlay.setOnClickListener(null as? ClickListener)
        if (note?.animateAlpha == true) {
            overlay.alpha = 0f
            overlay.animate().alpha(0.99f)
        } else
            overlay.alpha = 0.99f
        root.addView(overlay, params)
        note?.init(overlay)
    }

    fun cancel(context: Activity) {
        val targetRoot = context.window.decorView as ViewGroup
        targetRoot.children.lastOrNull { it is Overlay }?.let {
            if (note?.animateAlpha == true)
                it.animate().alpha(0f).withEndAction {
                    targetRoot.removeView(it)
                }
            else
                targetRoot.removeView(it)
        }
    }
}