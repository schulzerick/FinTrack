package com.estudo.fintrack.domain.usecase

import com.estudo.fintrack.domain.model.MonthlySummary
import com.estudo.fintrack.domain.model.MovementType
import com.estudo.fintrack.domain.model.Transaction

class CalculateMonthlySummaryUseCase {

    operator fun invoke(transactions: List<Transaction>): MonthlySummary {

        val totalIncome = transactions.filter {it.type == MovementType.INCOME}.sumOf {it.amountCents}
        val totalExpense = transactions.filter {it.type == MovementType.EXPENSE}.sumOf {it.amountCents}

        return MonthlySummary(totalIncome, totalExpense)
    }

}