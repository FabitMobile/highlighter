package ru.fabit.highlighter.internal

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import ru.fabit.highlighter.Highlighter

internal class Dummy : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        Highlighter.bind(this)
        log("Dummy_${hashCode()} created")
    }

    override fun onDestroy() {
        super.onDestroy()
        log("Dummy_${hashCode()} destroyed")
    }
}