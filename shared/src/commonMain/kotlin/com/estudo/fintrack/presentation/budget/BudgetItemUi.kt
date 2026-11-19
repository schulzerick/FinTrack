package com.estudo.fintrack.presentation

import com.estudo.fintrack.domain.model.BudgetLevel

data class BudgetItemUi(val id: Long, val categoryName: String,
    val spent: String, val limit: String, val progress: Float, val level: BudgetLevel)