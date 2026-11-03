package com.estudo.fintrack.data

import app.cash.sqldelight.coroutines.asFlow
import app.cash.sqldelight.coroutines.mapToList
import com.estudo.fintrack.db.FinTrackDatabase
import com.estudo.fintrack.domain.model.Category
import com.estudo.fintrack.domain.repository.CategoryRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.withContext

class SqlDelightCategoryRepository(private val database: FinTrackDatabase): CategoryRepository {
    override fun observeAll(): Flow<List<Category>> {
        return database
            .categoryEntityQueries
            .selectAll().asFlow()
            .mapToList(Dispatchers.Default)
            .map { entities -> entities.map{it.toDomain()} }
    }

    override suspend fun add(category: Category) {
        withContext(Dispatchers.Default) {
            database.categoryEntityQueries.insert(category.name)
        }

    }
}