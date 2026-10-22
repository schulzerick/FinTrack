package com.estudo.fintrack.domain.model

data class MonthlySummary(val totalIncomeCents: Long, val totalExpenseCents: Long){

    val balance: Long get() = totalIncomeCents - totalExpenseCents

}
