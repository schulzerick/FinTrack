package com.estudo.fintrack.presentation

import com.estudo.fintrack.domain.model.Category
import com.estudo.fintrack.domain.model.MovementType

data class TransactionFormState(
    val amountText: String = "",
    val type: MovementType = MovementType.EXPENSE,
    val categoryId: Long? = null,
    val note: String = "",
    val categories: List<Category> = emptyList(),
    val error: String? = null
)


