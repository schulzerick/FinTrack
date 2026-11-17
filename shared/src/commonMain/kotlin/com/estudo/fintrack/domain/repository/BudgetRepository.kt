package com.estudo.fintrack.domain.repository

import com.estudo.fintrack.domain.model.Budget
import kotlinx.coroutines.flow.Flow

interface BudgetRepository {

    fun observeAll(): Flow<List<Budget>>
    suspend fun add(budget: Budget)

    suspend fun updateLimit(id: Long, limitCents: Long)

    suspend fun delete(id: Long)

}