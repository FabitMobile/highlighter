package ru.fabit.highlighter

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import ru.fabit.highlighter.appearance.Overlay
import ru.fabit.highlighter.internal.Dummy

private var _overrideTransitions: Activity.() -> Unit = {
    if (Build.VERSION.SDK_INT >= 34) {
        overrideActivityTransition(
            AppCompatActivity.OVERRIDE_TRANSITION_OPEN,
            android.R.anim.fade_in,
            android.R.anim.fade_out
        )
        overrideActivityTransition(
            AppCompatActivity.OVERRIDE_TRANSITION_CLOSE,
            android.R.anim.fade_in,
            android.R.anim.fade_out
        )
    } else
        overridePendingTransition(
            android.R.anim.fade_in,
            android.R.anim.fade_out
        )
}

var Highlighter.Companion.overrideTransitions: Activity.() -> Unit
    get() = _overrideTransitions
    set(value) {
        _overrideTransitions = value
    }

internal class AsActivity(element: Element) : Highlighter(element) {
    fun bind(root: Activity) {
        val overlay = Overlay(root)
        overlay.highlight(element)
        overlay.setOnClickListener(null as? ClickListener)
        root.setContentView(overlay)
        if (delay != null && note?.animateAlpha == true) {
            overlay.alpha = 0f
            overlay.animate().alpha(1f).start()
        }
        setTheme(root)
        note?.init(overlay)
    }

    private fun setTheme(activity: Activity) {
        overrideTransitions(activity)
        note?.themeResId?.let {
            activity.setTheme(it)
        }
    }

    fun cancel(context: Context) {
        context.startActivity(cancelIntent(context))
    }

    private fun cancelIntent(context: Context) = Intent(context, Dummy::class.java).apply {
        addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP)
        addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        putExtra(Dummy.EXIT_FLAG, true)
    }
}