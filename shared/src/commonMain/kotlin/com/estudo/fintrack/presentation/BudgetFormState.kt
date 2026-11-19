package com.estudo.fintrack.presentation

import com.estudo.fintrack.domain.model.Category

data class BudgetFormState(
    val limitText: String = "",
    val categoryId: Long? = null,
    val categories: List<Category> = emptyList(),
    val error: String? = null,
)