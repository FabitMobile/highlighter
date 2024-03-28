package ru.fabit.highlighter.internal

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import ru.fabit.highlighter.Highlighter

internal class Dummy : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        log("Dummy_${hashCode()} created")
        bindOrExit(intent)
    }

    override fun onNewIntent(intent: Intent?) {
        log("Dummy_${hashCode()} onNewIntent")
        super.onNewIntent(intent)
        bindOrExit(intent)
    }

    private fun bindOrExit(intent: Intent?) {
        if (intent?.getBooleanExtra(EXIT_FLAG, false) == true) {
            log("Dummy_${hashCode()} finish with intent")
            Highlighter.onClose(this, withIntent = true)
        } else
            Highlighter.bind(this)
    }

    override fun onDestroy() {
        super.onDestroy()
        log("Dummy_${hashCode()} destroyed")
    }

    companion object {
        const val EXIT_FLAG = "EXIT_FLAG"
    }
}