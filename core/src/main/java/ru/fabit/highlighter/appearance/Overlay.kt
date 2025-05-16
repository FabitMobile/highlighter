package ru.fabit.highlighter.appearance

import android.app.Activity
import android.content.Context
import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.PointF
import android.graphics.PorterDuff
import android.graphics.PorterDuffXfermode
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import android.widget.RelativeLayout
import androidx.core.content.ContextCompat
import androidx.core.graphics.toRectF
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.updateLayoutParams
import ru.fabit.highlighter.ClickListener
import ru.fabit.highlighter.ClickResult
import ru.fabit.highlighter.Element
import ru.fabit.highlighter.Highlighter
import ru.fabit.highlighter.R
import ru.fabit.highlighter.internal.Dummy
import ru.fabit.highlighter.internal.afterMeasured
import ru.fabit.highlighter.internal.getDisplaySize
import ru.fabit.highlighter.internal.log

class Overlay(context: Context) : RelativeLayout(context) {
    private val size: PointF = (context as Activity).windowManager.getDisplaySize()
    private val backgroundColor =
        ContextCompat.getColor(context, R.color.highlighter_overlay_background)
    private val paintBackground = Paint().apply {
        color = backgroundColor
    }
    private val paintClear = Paint().apply {
        xfermode = PorterDuffXfermode(PorterDuff.Mode.CLEAR)
    }
    private var element: Element? = null

    private var topInset = 0f
    private var bottomInset = 0f

    private var originalMarginTop = 0
    private var originalMarginBottom = 0

    init {
        setWillNotDraw(false)
    }

    fun highlight(element: Element) {
        this.element = element
        invalidate()
    }

    fun onClose() {
        Highlighter.onClose(context)
    }

    fun setOnClickListener(listener: ClickListener?) {
        setOnTouchListener { v, event ->
            val x = event.rawX.toInt()
            val y = event.rawY.toInt()
            if (event.action == MotionEvent.ACTION_DOWN) {
                val click = if (element?.position?.contains(x, y) == true)
                    ClickResult.highlightedElement
                else
                    ClickResult.overlay
                log("ClickResult $click")
                listener?.invoke(click)
                onClose()
                v.performClick()
                true
            } else
                false
        }
    }

    override fun addView(child: View?, params: ViewGroup.LayoutParams?) {
        child?.visibility = INVISIBLE
        super.addView(child, params.apply {
            this as MarginLayoutParams
            originalMarginTop = this.topMargin
            originalMarginBottom = this.bottomMargin
            this.topMargin = originalMarginTop + (element?.position?.bottom ?: 0)
        })
        applyChildMargins(child)
    }

    private fun applyChildMargins(child: View?) {
        child?.afterMeasured {
            it.updateLayoutParams<LayoutParams> {
                topMargin -= topInset.toInt()
            }
            if (it.bottom + bottomInset + originalMarginBottom >= size.y) {
                it.updateLayoutParams<LayoutParams> {
                    addRule(ALIGN_PARENT_BOTTOM)
                    topMargin = originalMarginTop
                    bottomMargin =
                        bottomMargin + (size.y - bottomInset).toInt() - (element?.position?.top
                            ?: 0)
                }
            }
            child.visibility = VISIBLE
        }
    }

    override fun onAttachedToWindow() {
        super.onAttachedToWindow()
        if (context is Dummy) {
            val insets = WindowInsetsCompat.toWindowInsetsCompat(rootWindowInsets)
                .getInsets(WindowInsetsCompat.Type.statusBars() or WindowInsetsCompat.Type.navigationBars())
            topInset = insets.top.toFloat()
            bottomInset = insets.bottom.toFloat()
            (context as Activity).window.statusBarColor = backgroundColor
        }
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        canvas.drawRect(0f, 0f, size.x, size.y, paintBackground)
        element?.let {
            val pos = it.position.toRectF()
            pos.offset(0f, -topInset)
            canvas.drawRoundRect(pos, it.cornerRadius, it.cornerRadius, paintClear)
        }
    }
}