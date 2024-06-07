package ru.fabit.sample

import android.graphics.RenderEffect
import android.graphics.Shader
import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import ru.fabit.highlighter.appearance.ExplanatoryNote
import ru.fabit.highlighter.cancelHighlight
import ru.fabit.highlighter.highlight
import kotlin.time.Duration.Companion.seconds

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val text = findViewById<TextView>(R.id.textView)
        val button = findViewById<View>(R.id.button)

        button.setOnClickListener {
            highlightButton(button, text)
        }
    }

    fun highlightButton(button: View, text: TextView) {
        applyBlur()
        highlight(button) with TestExplanatoryNote {
            text.text = "showed $it"
            removeBlur()
        }
    }

    fun highlightButtonAndThenCancel(button: View, text: TextView) {
        applyBlur()
        highlight(button) with ExplanatoryNote {
            text.text = "showed $it"
            removeBlur()
        }
        Handler(Looper.getMainLooper()).postDelayed({
            removeBlur()
            cancelHighlight(this)
        }, 5000)
    }

    fun highlightButtonAfterSecond(button: View, text: TextView) {
        applyBlur()
        highlight(button) after 1.seconds with ExplanatoryNote {
            text.text = "showed $it"
            removeBlur()
        }
    }

    fun highlightButtonAndThenText(button: View, text: TextView) {
        applyBlur()
        highlight(button) with ExplanatoryNote {
            text.text = "showed $it"
            removeBlur()
        }
        Handler(Looper.getMainLooper()).postDelayed({
            removeBlur()
            highlight(text) with ExplanatoryNote {
                text.text = "showed $it"
            }
        }, 5000)
    }

    fun highlightButtonAfterSecondAndThenText(button: View, text: TextView) {
        applyBlur()
        highlight(button) after 1.seconds with ExplanatoryNote {
            text.text = "showed $it"
            removeBlur()
        }
        Handler(Looper.getMainLooper()).postDelayed({
            removeBlur()
            highlight(text) with ExplanatoryNote {
                text.text = "showed $it"
            }
        }, 5000)
    }

    fun applyBlur() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            (window.decorView as ViewGroup).getChildAt(0).setRenderEffect(
                RenderEffect.createBlurEffect(
                    5f,
                    5f,
                    Shader.TileMode.CLAMP
                )
            )
        }
    }

    fun removeBlur() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            (window.decorView as ViewGroup).getChildAt(0).setRenderEffect(null)
        }
    }
}