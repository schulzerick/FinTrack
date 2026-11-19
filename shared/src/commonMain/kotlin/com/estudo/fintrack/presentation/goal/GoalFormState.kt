package com.estudo.fintrack.presentation

data class GoalFormState(
    val name: String = "",
    val targetText: String = "",
    val error: String? = null,
)