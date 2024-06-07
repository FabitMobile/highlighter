package ru.fabit.highlighter

import android.app.Activity
import android.content.Context
import android.os.Handler
import android.os.Looper
import android.view.ViewGroup
import ru.fabit.highlighter.appearance.ExplanatoryNote
import ru.fabit.highlighter.internal.Dummy
import ru.fabit.highlighter.internal.log
import kotlin.time.Duration

sealed class Highlighter(
    protected val element: Element
) {
    protected var note: ExplanatoryNote? = null
    protected var delay: Duration? = null

    internal class AlreadyCreated(element: Element) : Highlighter(element)

    companion object {

        private var delayHandler: Handler? = null

        private var instance: Highlighter? = null

        val isVisible: Boolean
            get() = instance != null

        fun cancel(context: Context) {
            when (val instance = instance) {
                is AsActivity -> instance.cancel(context)
                is AsDecor -> onClose(context as Activity, withIntent = true)

                else -> {}
            }
            instance = null
            delayHandler?.removeCallbacksAndMessages(null)
            delayHandler = null
        }

        internal fun newInstanceForActivity(element: Element): Highlighter {
            if (instance is AsActivity)
                return AlreadyCreated(element)
            else
                cancel(element.context)
            val highlighter = AsActivity(element)
            this.instance = highlighter
            return highlighter
        }

        internal fun newInstanceForDecor(element: Element): Highlighter {
            if (instance is AsDecor)
                return AlreadyCreated(element)
            else
                cancel(element.context)
            val highlighter = AsDecor(element)
            this.instance = highlighter
            return highlighter
        }

        internal fun bind(root: Any) {
            instance?.let { instance ->
                instance.delay?.inWholeMilliseconds?.let { delay ->
                    log("highlight after delay ${instance.delay}")
                    delayHandler = Handler(Looper.getMainLooper())
                    delayHandler?.postDelayed({
                        log("delay over")
                        bind(instance, root)
                    }, delay)
                } ?: bind(instance, root)
            }
        }

        private fun bind(instance: Highlighter, root: Any) {
            log("highlight with note ${instance.note}")
            when (instance) {
                is AsActivity -> instance.bind(root as Activity)
                is AsDecor -> instance.bind(root as ViewGroup)

                else -> {}
            }
        }

        internal fun onClose(context: Context, withIntent: Boolean = false) {
            if (withIntent)
                instance?.note?.onClickListener?.invoke(ClickResult.cancel)
            if (instance is AsActivity && context is Dummy)
                context.finish()
            if (instance is AsDecor && context is Activity)
                (instance as AsDecor).cancel(context)
            instance = null
        }
    }

    infix fun with(note: ExplanatoryNote): Highlighter {
        this.note = note
        return this
    }

    infix fun after(delay: Duration): Highlighter {
        this.delay = delay
        return this
    }
}