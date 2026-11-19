package com.estudo.fintrack.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.estudo.fintrack.domain.repository.BudgetRepository
import com.estudo.fintrack.domain.repository.CategoryRepository
import com.estudo.fintrack.domain.repository.TransactionRepository
import com.estudo.fintrack.domain.usecase.CalculateBudgetStatusUseCase
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlin.time.Clock
import kotlinx.datetime.TimeZone
import kotlinx.datetime.todayIn

class BudgetViewModel(
    private val budgetRepository: BudgetRepository,
    transactionRepository: TransactionRepository,
    categoryRepository: CategoryRepository,
) : ViewModel() {

    private val calculateStatus = CalculateBudgetStatusUseCase()

    val uiState: StateFlow<BudgetUiState> = combine(
        budgetRepository.observeAll(),
        transactionRepository.observeAll(),
        categoryRepository.observeAll(),
    ) { budgets, transactions, categories ->

        val today = Clock.System.todayIn(TimeZone.currentSystemDefault())

        val items = budgets.map { budget ->
            val status = calculateStatus(budget, transactions, today.year, today.monthNumber)
            val categoryName = categories.find{it.id == budget.categoryId}?.name ?: "Sin categoría"
            val progress = minOf(status.spentCents.toFloat() / budget.limitCents, 1f)

                BudgetItemUi(
                    id = budget.id,
                    categoryName = categoryName,
                    spent = formatCents(status.spentCents),
                    limit = formatCents(budget.limitCents),
                    progress = progress,
                    level = status.level,
                )
        }

        BudgetUiState(items)
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5_000),
        initialValue = BudgetUiState(emptyList()),
    )
}