package ru.fabit.highlighter.internal

import android.app.Activity
import android.content.Context
import android.content.ContextWrapper
import android.graphics.Point
import android.graphics.PointF
import android.os.Build
import android.view.View
import android.view.ViewTreeObserver
import android.view.WindowManager
import androidx.core.graphics.toPointF

internal fun View.afterMeasured(block: (View) -> Unit) {
    var isCalcHeight = false
    viewTreeObserver.addOnGlobalLayoutListener(object : ViewTreeObserver.OnGlobalLayoutListener {
        override fun onGlobalLayout() {
            if (!isCalcHeight) {
                isCalcHeight = true
                viewTreeObserver.removeOnGlobalLayoutListener(this)
                block(this@afterMeasured)
            }
        }
    })
}

internal fun WindowManager.getDisplaySize(): PointF {
    return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
        val rect = currentWindowMetrics.bounds
        Point(rect.width(), rect.height())
    } else {
        val size = Point()
        defaultDisplay.getRealSize(size)
        size
    }.toPointF()
}

internal fun Context?.findActivity(): Activity? {
    if (this == null) {
        return null
    } else if (this is ContextWrapper) {
        return if (this is Activity) {
            this
        } else {
            baseContext.findActivity()
        }
    }

    return null
}