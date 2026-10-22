package com.estudo.fintrack.domain.model

import kotlinx.datetime.LocalDate

data class Transaction(
    val id: Long,
    val amountCents: Long,
    val type: MovementType,
    val categoryId: Long,
    val date: LocalDate,
    val note: String = ""
)
