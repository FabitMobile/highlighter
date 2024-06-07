package ru.fabit.sample

import ru.fabit.highlighter.ClickListener
import ru.fabit.highlighter.appearance.ExplanatoryNote

class TestExplanatoryNote(onClickListener: ClickListener): ExplanatoryNote(null, onClickListener) {
    override val themeResId = R.style.Theme_Highlighter_Test
}