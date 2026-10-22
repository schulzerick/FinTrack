package com.estudo.fintrack.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.compose.viewModel
import com.estudo.fintrack.domain.model.MovementType
import com.estudo.fintrack.domain.model.Transaction
import com.estudo.fintrack.domain.repository.TransactionRepository
import com.estudo.fintrack.domain.usecase.CalculateMonthlySummaryUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import kotlinx.datetime.LocalDate

class SummaryViewModel (private val repository: TransactionRepository): ViewModel() {


    fun addTestExpense() {
        viewModelScope.launch {
            repository.add(Transaction
                (0, 1000, MovementType.EXPENSE,
                2, LocalDate(2026, 10, 6)))
        }
    }

    val uiState: StateFlow<SummaryUiState> = repository.observeAll()
        .map { transactions -> buildState(transactions) }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5_000),
            initialValue = buildState(emptyList()),
        )


    private fun buildState(transactions: List<Transaction>): SummaryUiState{

        val summary = CalculateMonthlySummaryUseCase()(transactions)

        val income = formatCents(summary.totalIncomeCents)
        val expense = formatCents(summary.totalExpenseCents)
        val balance = formatCents(summary.balance)

        return SummaryUiState(
            income = income,
            expense = expense,
            balance = balance,
        )

    }
}