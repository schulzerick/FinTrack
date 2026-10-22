package com.estudo.fintrack.domain

import com.estudo.fintrack.domain.model.MovementType
import com.estudo.fintrack.domain.model.Transaction
import com.estudo.fintrack.domain.usecase.CalculateMonthlySummaryUseCase
import kotlinx.datetime.LocalDate
import kotlin.test.Test
import kotlin.test.assertEquals


class CalculateMonthlySummaryUseCaseTest {

    @Test
    fun calculates_income_expense_and_balance() {
        // Arrange: preparar los datos
        val transactions = listOf(
            Transaction(1, 21500, MovementType.INCOME, 1, LocalDate(2026, 9, 24)),
            Transaction(2, 4830, MovementType.EXPENSE, 2, LocalDate(2026, 9, 24)),
            Transaction(3, 6200, MovementType.EXPENSE, 3, LocalDate(2026, 9, 24)),
        )

        val useCase = CalculateMonthlySummaryUseCase()
        val summary = useCase(transactions)


        assertEquals(21500 ,summary.totalIncomeCents)
        assertEquals(11030,summary.totalExpenseCents)
        assertEquals(10470, summary.balance)


    }






    }