package com.estudo.fintrack.data

import com.estudo.fintrack.db.TransactionEntity
import com.estudo.fintrack.domain.model.MovementType
import com.estudo.fintrack.domain.model.Transaction
import kotlinx.datetime.LocalDate
import kotlin.test.Test
import kotlin.test.assertEquals

class TransactionMapperTest {

    @Test
    fun converts_entity_to_domain_transaction(){
        val entity = TransactionEntity(
            1, 1000, "EXPENSE",
            2, "2026-10-06", "Compra semanal")

        val result = entity.toDomain()

        val expected = Transaction(1, 1000, MovementType.EXPENSE,
            2, LocalDate(2026, 10, 6), "Compra semanal")

        assertEquals(expected, result)
    }

    @Test
    fun converts_entity_to_domain_transaction_income(){
        val entity = TransactionEntity(
            1, 1000, "INCOME",
            2, "2026-10-06", "")

        val result = entity.toDomain()

        val expected = Transaction(1, 1000, MovementType.INCOME,
            2, LocalDate(2026, 10, 6), "")

        assertEquals(expected, result)
    }





}