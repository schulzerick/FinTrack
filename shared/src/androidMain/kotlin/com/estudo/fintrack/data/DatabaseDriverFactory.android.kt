package com.estudo.fintrack.data

import android.content.Context
import app.cash.sqldelight.db.SqlDriver
import app.cash.sqldelight.driver.android.AndroidSqliteDriver
import com.estudo.fintrack.db.FinTrackDatabase
import java.io.File

actual class DatabaseDriverFactory(private val context: Context) {
    actual fun createDriver(): SqlDriver {
        return AndroidSqliteDriver(FinTrackDatabase.Schema, context, "fintrack.db")
    }
}