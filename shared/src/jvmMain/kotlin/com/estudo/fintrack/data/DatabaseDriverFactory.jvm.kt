package com.estudo.fintrack.data

import app.cash.sqldelight.db.SqlDriver
import app.cash.sqldelight.driver.jdbc.sqlite.JdbcSqliteDriver
import com.estudo.fintrack.db.FinTrackDatabase
import java.io.File

actual class DatabaseDriverFactory {
    actual fun createDriver(): SqlDriver {
        val file = File(System.getProperty("user.home"), ".fintrack/fintrack.db")
        file.parentFile.mkdirs()
        val isNew = !file.exists()
        var driver = JdbcSqliteDriver("jdbc:sqlite:${file.absolutePath}")

        if(isNew)
            FinTrackDatabase.Schema.create(driver)


        return driver
    }
}