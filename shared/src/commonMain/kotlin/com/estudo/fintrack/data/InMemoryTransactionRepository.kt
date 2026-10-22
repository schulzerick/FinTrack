package com.estudo.fintrack.data

import com.estudo.fintrack.domain.model.MovementType
import com.estudo.fintrack.domain.model.Transaction
import com.estudo.fintrack.domain.repository.TransactionRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.datetime.LocalDate

class InMemoryTransactionRepository : TransactionRepository {

    private val _transactions = MutableStateFlow(listOf(
        Transaction(1, 21500, MovementType.INCOME, 1, LocalDate(2026, 10, 6)),
        Transaction(2, 4830, MovementType.EXPENSE, 2, LocalDate(2026, 10, 6)),
        Transaction(3, 6200, MovementType.EXPENSE, 3, LocalDate(2026, 10, 6)),
        ))

    override fun observeAll(): Flow<List<Transaction>> {
        return _transactions
    }

    override suspend fun add(transaction: Transaction) {
        _transactions.update {current -> current + transaction}
    }
}