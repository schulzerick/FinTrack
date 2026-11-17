package com.estudo.fintrack.data

import com.estudo.fintrack.db.BudgetEntity
import com.estudo.fintrack.domain.model.Budget

fun BudgetEntity.toDomain(): Budget{
    return Budget(id, categoryId, limitCents)
}