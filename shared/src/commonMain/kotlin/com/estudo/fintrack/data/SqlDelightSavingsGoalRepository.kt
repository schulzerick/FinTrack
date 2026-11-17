package com.estudo.fintrack.data


import app.cash.sqldelight.coroutines.asFlow
import app.cash.sqldelight.coroutines.mapToList
import com.estudo.fintrack.db.FinTrackDatabase
import com.estudo.fintrack.domain.model.SavingsGoal
import com.estudo.fintrack.domain.repository.SavingsGoalRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.withContext

class SqlDelightSavingsGoalRepository(private val database: FinTrackDatabase) : SavingsGoalRepository {

    override fun observeAll(): Flow<List<SavingsGoal>> {
        return database
            .savingsGoalEntityQueries
            .selectAll().asFlow()
            .mapToList(Dispatchers.Default)
            .map { entities -> entities.map { it.toDomain() }}
    }

    override suspend fun add(goal: SavingsGoal) {
        withContext(Dispatchers.Default) {
            database.savingsGoalEntityQueries.insert(goal.name, goal.targetCents)
        }
    }

    override suspend fun updateSaved(id: Long, savedCents: Long) {
        withContext(Dispatchers.Default) {
            database.savingsGoalEntityQueries.updateSaved(savedCents, id)
        }
    }

    override suspend fun delete(id: Long) {
        withContext(Dispatchers.Default) {
            database.savingsGoalEntityQueries.delete(id)
        }
    }
}