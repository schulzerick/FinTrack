package com.estudo.fintrack.data

import app.cash.sqldelight.coroutines.asFlow
import app.cash.sqldelight.coroutines.mapToList
import com.estudo.fintrack.db.FinTrackDatabase
import com.estudo.fintrack.domain.model.Transaction
import com.estudo.fintrack.domain.repository.TransactionRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.withContext

class SqlDelightTransactionRepository(private val database: FinTrackDatabase): TransactionRepository {
    override fun observeAll(): Flow<List<Transaction>> {
        return database
            .transactionEntityQueries
            .selectAll().asFlow()
            .mapToList(Dispatchers.Default)
            .map { entities -> entities.map{it.toDomain()} }
    }

    override suspend fun add(transaction: Transaction) {
        withContext(Dispatchers.Default) {
            database.transactionEntityQueries.insert(transaction.amountCents,
                transaction.type.name, transaction.categoryId,
                transaction.date.toString(), transaction.note)
        }

    }
}