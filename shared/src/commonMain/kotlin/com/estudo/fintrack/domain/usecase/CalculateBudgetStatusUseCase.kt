package com.estudo.fintrack.domain.usecase

import com.estudo.fintrack.domain.model.Budget
import com.estudo.fintrack.domain.model.BudgetLevel
import com.estudo.fintrack.domain.model.BudgetStatus
import com.estudo.fintrack.domain.model.MovementType
import com.estudo.fintrack.domain.model.Transaction

class CalculateBudgetStatusUseCase {

    operator fun invoke(
        budget: Budget,
        transactions: List<Transaction>,
        year: Int,
        month: Int
    ): BudgetStatus {

        val relevant = transactions.filter {
            it.type == MovementType.EXPENSE && it.categoryId == budget.categoryId &&
            it.date.year == year && it.date.monthNumber == month}

        val spent = relevant.sumOf { it.amountCents }

        val level = if (spent >= budget.limitCents) {
             BudgetLevel.EXCEEDED
        } else if (spent * 100 >= budget.limitCents * 80) {
           BudgetLevel.WARNING
        } else {
            BudgetLevel.OK
        }

        return BudgetStatus(budget, spent, level)
    }
}