package com.estudo.fintrack

import androidx.compose.ui.window.Window
import androidx.compose.ui.window.application
import com.estudo.fintrack.data.DatabaseDriverFactory

fun main() = application {
    Window(
        onCloseRequest = ::exitApplication,
        title = "FinTrack",
    ) {
        App(DatabaseDriverFactory())
    }
}