package ru.fabit.highlighter

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.Handler
import android.os.Looper
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import ru.fabit.highlighter.appearance.ExplanatoryNote
import ru.fabit.highlighter.appearance.Overlay
import ru.fabit.highlighter.internal.Dummy
import ru.fabit.highlighter.internal.log
import kotlin.time.Duration

fun highlight(element: Element): Highlighter {
    return Highlighter.newInstance(element).also {
        element.context.startActivity(
            Intent(element.context, Dummy::class.java).apply {
                addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP)
            }
        )
    }
}

fun highlight(view: View): Highlighter {
    return highlight(view.toElement())
}

fun cancelHighlight(context: Context) {
    Highlighter.cancel(context)
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

        fun cancelIntent(context: Context) = Intent(context, Dummy::class.java).apply {
            addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP)
            putExtra(Dummy.EXIT_FLAG, true)
        }

        fun cancel(context: Context) {
            if (instance != null)
                context.startActivity(cancelIntent(context))
        }

        internal fun newInstance(element: Element): Highlighter {
            val highlighter = Highlighter(element)
            instance = highlighter
            return highlighter
        }

        internal fun bind(activity: Activity) {
            instance?.delay?.inWholeMilliseconds?.let {
                Handler(Looper.getMainLooper()).postDelayed({
                    log("delay over")
                    instance?.bind(activity)
                }, it)
            } ?: instance?.bind(activity)
        }

        internal fun onClose(context: Context, withIntent: Boolean = false) {
            if (withIntent)
                instance?.note?.onClickListener?.invoke(ClickResult.cancel)
            if (context is Activity) {
                context.finish()
                instance = null
            }
        }
    }

    private var note: ExplanatoryNote? = null

    private var delay: Duration? = null

    infix fun with(note: ExplanatoryNote): Highlighter {
        log("highlight with note $note")
        this.note = note
        return this
    }

    infix fun after(delay: Duration): Highlighter {
        log("highlight after delay $delay")
        this.delay = delay
        return this
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
        if (delay != null) {
            root.alpha = 0f
            root.animate().alpha(1f).start()
        }
        setTheme(activity)
        note?.init(root)
    }
}