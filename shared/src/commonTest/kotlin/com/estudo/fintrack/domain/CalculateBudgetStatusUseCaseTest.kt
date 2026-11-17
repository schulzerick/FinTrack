package com.estudo.fintrack.domain

import com.estudo.fintrack.domain.model.Budget
import com.estudo.fintrack.domain.model.BudgetLevel
import com.estudo.fintrack.domain.model.MovementType
import com.estudo.fintrack.domain.model.Transaction
import com.estudo.fintrack.domain.usecase.CalculateBudgetStatusUseCase
import kotlinx.datetime.LocalDate
import kotlin.test.Test

import kotlin.test.assertEquals

class CalculateBudgetStatusUseCaseTest {

    private val budget = Budget(1, 2, 30000)
    private val useCase = CalculateBudgetStatusUseCase()

    @Test
    fun no_transactions_means_nothing_spent() {

        val transactions = emptyList<Transaction>()

        val status = useCase(budget, transactions, 2026, 10)

        assertEquals(0, status.spentCents)
        assertEquals(BudgetLevel.OK, status.level)

    }

    @Test
    fun small_expense_stays_ok() {
        val transactions = listOf(
            Transaction(1, 10000, MovementType.EXPENSE, 2, LocalDate(2026, 10, 15)),
        )

        // Act
        val status = useCase(budget, transactions, 2026, 10)

        // Assert
        assertEquals(10000, status.spentCents)
        assertEquals(BudgetLevel.OK, status.level)
    }

    @Test
    fun big_expense_with_warning() {
        val transactions = listOf(
            Transaction(1, 14000, MovementType.EXPENSE, 2, LocalDate(2026, 10, 15)),
            Transaction(2, 10000, MovementType.EXPENSE, 2, LocalDate(2026, 10, 15))
        )

        // Act
        val status = useCase(budget, transactions, 2026, 10)

        // Assert
        assertEquals(24000, status.spentCents)
        assertEquals(BudgetLevel.WARNING, status.level)
    }

    @Test
    fun big_expense_with_less_80_percent() {
        val transactions = listOf(
            Transaction(1, 23999, MovementType.EXPENSE, 2, LocalDate(2026, 10, 15))
        )

        // Act
        val status = useCase(budget, transactions, 2026, 10)

        // Assert
        assertEquals(23999, status.spentCents)
        assertEquals(BudgetLevel.OK, status.level)
    }

    @Test
    fun exactly_100_percent_is_exceeded() {
        val transactions = listOf(
            Transaction(1, 30000, MovementType.EXPENSE, 2, LocalDate(2026, 10, 15))
        )

        val status = useCase(budget, transactions, 2026, 10)

        assertEquals(30000, status.spentCents)
        assertEquals(BudgetLevel.EXCEEDED, status.level)
    }

    @Test
    fun over_100_percent_is_exceeded() {
        val transactions = listOf(
            Transaction(1, 35000, MovementType.EXPENSE, 2, LocalDate(2026, 10, 15))
        )

        val status = useCase(budget, transactions, 2026, 10)

        assertEquals(35000, status.spentCents)
        assertEquals(BudgetLevel.EXCEEDED, status.level)
    }

    @Test
    fun ignores_expenses_of_other_categories() {
        val transactions = listOf(
            Transaction(1, 25000, MovementType.EXPENSE, 3, LocalDate(2026, 10, 15))
        )

        val status = useCase(budget, transactions, 2026, 10)

        assertEquals(0, status.spentCents)
        assertEquals(BudgetLevel.OK, status.level)
    }

    @Test
    fun ignores_income() {
        val transactions = listOf(
            Transaction(1, 25000, MovementType.INCOME, 2, LocalDate(2026, 10, 15))
        )

        val status = useCase(budget, transactions, 2026, 10)

        assertEquals(0, status.spentCents)
        assertEquals(BudgetLevel.OK, status.level)
    }

    @Test
    fun ignores_expenses_of_other_months() {
        val transactions = listOf(
            Transaction(1, 25000, MovementType.EXPENSE, 2, LocalDate(2026, 9, 15))
        )

        val status = useCase(budget, transactions, 2026, 10)

        assertEquals(0, status.spentCents)
        assertEquals(BudgetLevel.OK, status.level)
    }

}
