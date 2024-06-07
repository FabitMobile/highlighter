package ru.fabit.highlighter

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Handler
import android.os.Looper
import android.view.View
import ru.fabit.highlighter.internal.Dummy
import ru.fabit.highlighter.internal.log

fun highlightLegacy(element: Element): Highlighter {
    return Highlighter.newInstanceForActivity(element).also {
        if (it is Highlighter.AlreadyCreated) {
            log("highlighter already created. Do nothing")
            return@also
        }
        element.context.startActivity(
            Intent(element.context, Dummy::class.java).apply {
                addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP)
            }
        )
    }
}

fun highlightLegacy(view: View): Highlighter {
    return highlightLegacy(view.toElement())
}

fun highlight(element: Element): Highlighter {
    return Highlighter.newInstanceForDecor(element).also {
        if (it is Highlighter.AlreadyCreated) {
            log("highlighter already created. Do nothing")
            return@also
        }
        Handler(Looper.getMainLooper()).post {
            val targetRoot = (element.context as Activity).window.decorView
            Highlighter.bind(targetRoot)
        }
    }
}

fun highlight(view: View): Highlighter {
    return highlight(view.toElement())
}

fun cancelHighlight(context: Context) {
    Highlighter.cancel(context)
}