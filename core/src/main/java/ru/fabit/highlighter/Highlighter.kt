package ru.fabit.highlighter

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Build
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import ru.fabit.highlighter.appearance.ExplanatoryNote
import ru.fabit.highlighter.appearance.Overlay
import ru.fabit.highlighter.internal.Dummy
import ru.fabit.highlighter.internal.log

fun highlight(element: Element): Highlighter {
    return Highlighter.newInstance(element).also {
        element.context.startActivity(
            Intent(element.context, Dummy::class.java).apply {
                addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            }
        )
    }
}

fun highlight(view: View): Highlighter {
    return highlight(view.toElement())
}

class Highlighter private constructor(
    private val element: Element
) {
    companion object {
        var DEBUG = false

        var overrideTransitions: Activity.() -> Unit = {
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

        private var instance: Highlighter? = null

        internal fun newInstance(element: Element): Highlighter {
            val highlighter = Highlighter(element)
            instance = highlighter
            return highlighter
        }

        internal fun bind(activity: Activity) {
            instance?.bind(activity)
        }

        internal fun onClose(context: Context) {
            if (context is Activity) {
                context.finish()
                instance = null
            }
        }
    }

    private var note: ExplanatoryNote? = null

    infix fun with(note: ExplanatoryNote) {
        log("highlight with note $note ")
        this.note = note
    }

    private fun setTheme(activity: Activity) {
        overrideTransitions(activity)
        note?.themeResId?.let {
            activity.setTheme(it)
        }
    }

    private fun bind(activity: Activity) {
        val root = Overlay(activity)
        root.highlight(element)
        root.setOnClickListener(null as? ClickListener)
        activity.setContentView(root)
        setTheme(activity)
        note?.init(root)
    }
}