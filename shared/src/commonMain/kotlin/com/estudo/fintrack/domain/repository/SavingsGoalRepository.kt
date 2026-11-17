package com.estudo.fintrack.domain.repository

import com.estudo.fintrack.domain.model.SavingsGoal
import kotlinx.coroutines.flow.Flow

interface SavingsGoalRepository {

    fun observeAll(): Flow<List<SavingsGoal>>

    suspend fun add(goal: SavingsGoal)

    suspend fun updateSaved(id: Long, savedCents: Long)

    suspend fun delete(id: Long)
}