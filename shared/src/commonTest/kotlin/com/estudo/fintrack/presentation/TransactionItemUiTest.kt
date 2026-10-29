package com.estudo.fintrack.presentation

import com.estudo.fintrack.domain.model.MovementType
import com.estudo.fintrack.domain.model.Transaction
import kotlinx.datetime.LocalDate
import kotlin.test.Test
import kotlin.test.assertEquals

class TransactionItemUiTest {

    @Test
    fun converts_expense_to_ui(){

        val transaction = Transaction(1, 4830,
            MovementType.EXPENSE, 2,
            LocalDate(2026, 10, 6), "Mercadona")

        val result = transaction.toItemUi()

        val expected = TransactionItemUi(1, "Mercadona", "-48,30 €", "06/10/2026", true)

        assertEquals(expected, result)
    }

    @Test
    fun converts_income_to_ui(){

        val transaction = Transaction(1, 215000,
            MovementType.INCOME, 2,
            LocalDate(2026, 10, 1),
            "Nómina")

        val result = transaction.toItemUi()

        val expected = TransactionItemUi(1, "Nómina", "+2150,00 €",
            "01/10/2026", false)

        assertEquals(expected, result)
    }

    @Test
    fun converts_expense_empty_to_ui(){

        val transaction = Transaction(1, 1000,
            MovementType.EXPENSE, 2,
            LocalDate(2026, 10, 1),
            "")

        val result = transaction.toItemUi()

        val expected = TransactionItemUi(1, "Sin nota", "-10,00 €",
            "01/10/2026", true)

        assertEquals(expected, result)
    }




}