package com.estudo.fintrack.domain.model

data class SavingsGoal(val id: Long, val name: String, val targetCents: Long,
                       val savedCents: Long )
