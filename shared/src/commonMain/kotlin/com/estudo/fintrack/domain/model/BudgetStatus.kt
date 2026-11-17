package com.estudo.fintrack.domain.model

data class BudgetStatus(val budget: Budget, val spentCents: Long, val level: BudgetLevel)
