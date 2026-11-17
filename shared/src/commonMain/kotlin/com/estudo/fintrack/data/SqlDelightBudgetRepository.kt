package com.estudo.fintrack.data

import app.cash.sqldelight.coroutines.asFlow
import app.cash.sqldelight.coroutines.mapToList
import com.estudo.fintrack.db.FinTrackDatabase
import com.estudo.fintrack.domain.model.Budget
import com.estudo.fintrack.domain.repository.BudgetRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.withContext

class SqlDelightBudgetRepository(private val database: FinTrackDatabase) : BudgetRepository {

    override fun observeAll(): Flow<List<Budget>> {
        return database
            .budgetEntityQueries
            .selectAll().asFlow()
            .mapToList(Dispatchers.Default)
            .map { entities -> entities.map{it.toDomain()} }
    }

    override suspend fun add(budget: Budget) {
        withContext(Dispatchers.Default) {
            database.budgetEntityQueries.insert(budget.categoryId, budget.limitCents)
        }
    }

    override suspend fun updateLimit(id: Long, limitCents: Long) {
        withContext(Dispatchers.Default) {
            database.budgetEntityQueries.updateLimit(limitCents, id)
        }
    }

    override suspend fun delete(id: Long) {
        withContext(Dispatchers.Default) {
            database.budgetEntityQueries.delete(id)
        }
    }
}