package ru.fabit.sample

import android.graphics.RenderEffect
import android.graphics.Shader
import android.os.Build
import android.os.Bundle
import android.view.View
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import ru.fabit.highlighter.appearance.ExplanatoryNote
import ru.fabit.highlighter.highlight

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val text = findViewById<TextView>(R.id.textView)
        val button = findViewById<View>(R.id.button)


        button.setOnClickListener {
            applyBlur()
            highlight(button) with ExplanatoryNote {
                text.text = "showed $it"
                removeBlur()
            }
        }
    }

    fun applyBlur() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            window.decorView.setRenderEffect(
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
            window.decorView.setRenderEffect(null)
        }
    }
}