package com.estudo.fintrack.domain.repository

import com.estudo.fintrack.domain.model.Transaction
import kotlinx.coroutines.flow.Flow

interface TransactionRepository {

    fun observeAll(): Flow<List<Transaction>>
    suspend fun add(transaction: Transaction)
}