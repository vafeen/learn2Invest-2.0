package ru.surf.learn2invest.presentation.ui.main

import android.widget.TextView

internal sealed class MainActivityIntent {
    data class ProcessSplash(val textView: TextView) : MainActivityIntent()
}