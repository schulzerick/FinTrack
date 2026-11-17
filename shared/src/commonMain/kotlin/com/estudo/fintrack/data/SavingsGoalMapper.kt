package com.estudo.fintrack.data

import com.estudo.fintrack.db.SavingsGoalEntity
import com.estudo.fintrack.domain.model.SavingsGoal

fun SavingsGoalEntity.toDomain(): SavingsGoal{
    return SavingsGoal(id, name, targetCents, savedCents)
}